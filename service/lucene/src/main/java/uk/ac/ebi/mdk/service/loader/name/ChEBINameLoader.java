/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.service.loader.name;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.preference.type.BooleanPreference;
import uk.ac.ebi.mdk.service.ProgressListener;
import uk.ac.ebi.mdk.service.ServicePreferences;
import uk.ac.ebi.mdk.service.exception.MissingLocationException;
import uk.ac.ebi.mdk.service.index.name.ChEBINameIndex;
import uk.ac.ebi.mdk.service.loader.AbstractChEBILoader;
import uk.ac.ebi.mdk.service.loader.location.RemoteLocation;
import uk.ac.ebi.mdk.service.loader.writer.DefaultNameIndexWriter;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * ChEBINameLoader - 28.02.2012 <br/> <p/> Load ChEBI Names into a searchable
 * lucene index. Some generalisations are made to reduce the complexity of the
 * provided data. The loader treats systematic names as synonyms. When an entry
 * has multiple IUPAC names the first one is index and any others are added as
 * synonyms. <p/> The preferred name for each entry is fetched from ChEBI
 * Compounds resource first, if no preferred name exists then the name from the
 * ChEBI Names resource is used. <p/> This loader will load into {@see
 * ChEBINameIndex}.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ChEBINameLoader extends AbstractChEBILoader {

    private static final Logger LOGGER = Logger
            .getLogger(ChEBINameLoader.class);

    public ChEBINameLoader() throws IOException {
        super(new ChEBINameIndex());


        // we use the ZIP location so it's slightly smaller to download
        // note: ChEBI Compounds as already required by the super class, but as they have the same
        //       name the resource is collapsed down to a single entry
        addRequiredResource("ChEBI Compounds",
                            "compound.tsv flatfile from ChEBI, specifying preferred names, secondary and primary accessions",
                            ResourceFileLocation.class,
                            new RemoteLocation("ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/compounds.tsv"));
        addRequiredResource("ChEBI Names",
                            "name.tsv flatfile from ChEBI, specifying names and their tpyes (e.g. IUPAC/Synonym)",
                            ResourceFileLocation.class,
                            new RemoteLocation("ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/names.tsv"));

    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() throws IOException {

        // get the preferred names first
        Map<String, String> preferredNameMap = getPreferredNameMap();

        ResourceFileLocation location = getLocation("ChEBI Names");
        CSVReader csv = new CSVReader(new InputStreamReader(location.open()), '\t', '\0');

        List<String> header = Arrays.asList(csv.readNext());
        int nameIndex = header.indexOf("NAME");
        int typeIndex = header.indexOf("TYPE");
        int accessionIndex = header.indexOf("COMPOUND_ID");
        int langIndex = header.indexOf("LANGUAGE");

        // what we treat as synonyms
        Set<String> synonymType = new HashSet<String>(Arrays.asList("SYSTEMATIC NAME", "SYNONYM"));

        Multimap<String, String> synonyms = HashMultimap.create();
        Multimap<String, String> iupac = HashMultimap.create();
        Multimap<String, String> inn = HashMultimap.create();
        Multimap<String, String> brand = HashMultimap.create();


        fireProgressUpdate("preloading primary identifiers...");
        createMap();
        fireProgressUpdate("done");

        int count = 0;
        String[] row = null;
        while (!isCancelled() && (row = csv.readNext()) != null) {

            if (row.length < header.size()) {
                LOGGER.error("malformed entry: " + Joiner.on(", ").join(row));
                continue;
            }

            String accession = getPrimaryIdentifier(row[accessionIndex]);
            String name = row[nameIndex];
            String type = row[typeIndex];
            String lang = row[langIndex];

            // only keep latin and english compound names
            if (!lang.equals("en") && !lang.equals("la")) {
                continue;
            }

            /* SYNONYM
             * Type matches anything we are considering a synonym
             */
            if (synonymType.contains(type))
                synonyms.put(accession, name);

            /* PREFERRED NAME
             * If there is already a preferred name in the map, check
             * whether the preferred name matches the 'NAME'. If it
             * doesn't add the 'NAME' to the synonym map
             */
            if (type.equals("NAME")) {
                if (preferredNameMap.containsKey(accession)) {
                    String preferredName = preferredNameMap.get(accession);
                    if (!preferredName.equals(name)) {
                        synonyms.put(accession, name);
                    }
                } else {
                    preferredNameMap.put(accession, name);
                }
            }

            /* IUPAC
             * Add the iupac name, if there is already an iupac
             * name and they are not matches, add the iupac name
             * to the synonym's list
             */
            if (type.equals("IUPAC NAME")) {
                if (!iupac.containsKey(accession)) {
                    iupac.put(accession, name);
                } else {
                    if (!iupac.get(accession).contains(name))
                        synonyms.put(accession, name);
                }

            }

            if (type.equals("BRAND NAME")) {
                if (!brand.containsKey(accession)) {
                    brand.put(accession, name);
                } else {
                    if (!brand.get(accession).contains(name))
                        synonyms.put(accession, name);
                }
            }

            if (type.equals("INN")) {
                if (!inn.containsKey(accession)) {
                    inn.put(accession, name);
                } else {
                    if (!inn.get(accession).contains(name))
                        synonyms.put(accession, name);
                }
            }

            // update progress (note this is only first step take have the progress on the file)
            if (++count % 150 == 0)
                fireProgressUpdate(location.progress() * 0.5);

        }

        // if not cancelled write the index
        if (!isCancelled()) {

            // get all the accessions to iterate through
            Set<String> accessions = new HashSet<String>(preferredNameMap
                                                                 .size() * 2);
            accessions.addAll(synonyms.keySet());
            accessions.addAll(iupac.keySet());
            accessions.addAll(preferredNameMap.keySet());
            accessions.addAll(brand.keySet());
            accessions.addAll(inn.keySet());

            DefaultNameIndexWriter writer = new DefaultNameIndexWriter(getIndex());

            double size = accessions.size();
            count = 0;

            // for each accession write the name's to the index
            for (String accession : accessions) {

                if (isActive(accession)) {

                    String preferredName =
                            preferredNameMap.containsKey(accession)
                            ? preferredNameMap.get(accession) : "";
                    String iupacName = iupac.containsKey(accession)
                                       ? iupac.get(accession).iterator().next()
                                       : "";
                    String brandName = brand.containsKey(accession)
                                       ? brand.get(accession).iterator().next()
                                       : "";
                    String innName = inn.containsKey(accession)
                                     ? inn.get(accession).iterator().next()
                                     : "";

                    writer.write(accession, preferredName, iupacName, brandName, innName, synonyms
                            .get(accession));


                }

                if (++count % 150 == 0)
                    fireProgressUpdate(0.5 + ((count / size) * 0.5));

            }

            writer.close();

        }

        location.close();

    }

    /**
     * Builds a map of preferred names from the compound file
     *
     * @return
     * @throws IOException
     * @throws MissingLocationException
     */
    private Map<String, String> getPreferredNameMap() throws IOException,
                                                             MissingLocationException {

        Map<String, String> preferredNames = new HashMap<String, String>();

        // need to build the map first as it uses the same resource
        createMap();

        ResourceFileLocation location = getLocation("ChEBI Compounds");
        CSVReader csv = new CSVReader(new InputStreamReader(location.open()), '\t');

        List<String> header = Arrays.asList(csv.readNext());
        int nameIndex = header.indexOf("NAME");
        int accessionIndex = header.indexOf("CHEBI_ACCESSION");

        Pattern NULL_PATTERN = Pattern.compile("null");

        String[] row = null;
        while ((row = csv.readNext()) != null) {
            String name = row[nameIndex];
            String accession = row[accessionIndex];

            if (NULL_PATTERN.matcher(name).matches())
                continue;

            preferredNames.put(getPrimaryIdentifier(accession), name);

        }

        location.close();

        return preferredNames;
    }

    public static void main(String[] args) throws Exception {
        ChEBINameLoader loader = new ChEBINameLoader();
        if (loader.canBackup()) loader.backup();

        long t0 = System.nanoTime();
        boolean update = loader.canUpdate();
        long t1 = System.nanoTime();

        BooleanPreference p = ServicePreferences.getInstance().getPreference("PROXY_SET");
        p.put(true);

        System.out.println((t1 - t0) / 1e6);
        loader.addProgressListener(new ProgressListener() {
            @Override public void progressed(String message) {
                System.out.print("\r" + message);
            }
        });

        System.out.println(update);

        if(update)
            loader.update();
    }

}
