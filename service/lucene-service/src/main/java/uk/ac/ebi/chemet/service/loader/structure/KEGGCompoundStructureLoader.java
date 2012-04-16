package uk.ac.ebi.chemet.service.loader.structure;

import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Reader;
import uk.ac.ebi.chemet.service.index.structure.KEGGCompoundStructureIndex;
import uk.ac.ebi.chemet.service.loader.AbstractSingleIndexResourceLoader;
import uk.ac.ebi.chemet.service.loader.writer.DefaultStructureIndexWriter;
import uk.ac.ebi.service.location.ResourceDirectoryLocation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * KEGGCompoundStructureLoader - 20.02.2012 <br/>
 * <p/>
 * Load the KEGG mol directory into a lucene index.
 * The KEGG Compound identifier is provided by the name of the file
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class KEGGCompoundStructureLoader extends AbstractSingleIndexResourceLoader {

    /**
     * Creates the loader with the {@see KEGGCompoundStructureIndex} and no default location.
     * The KEGG mol directory used to be available on the FTP site before a subscription
     * was required but now the it is only available to subscribers. Therefore the resource
     * can be loaded from a local file only
     */
    public KEGGCompoundStructureLoader() {
        super(new KEGGCompoundStructureIndex());

        addRequiredResource("KEGG Mol files",
                            "a directory containing '.mol' files named with KEGG Compound Id (i.e. kegg/compound/mol/C00009.mol)",
                            ResourceDirectoryLocation.class);

    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() throws IOException {

        // get the directory of files
        ResourceDirectoryLocation location = getLocation("KEGG Mol files");
        DefaultStructureIndexWriter writer = new DefaultStructureIndexWriter(getIndex());
        MDLV2000Reader mdlReader = new MDLV2000Reader();

        for (File file : location.list()) {

            if(isCancelled()) break;

            try {
                mdlReader.setReader(new FileInputStream(file));
                IAtomContainer molecule = mdlReader.read(new Molecule());
                writer.write(file.getName().substring(0, 6), molecule);
                mdlReader.close();
            } catch (Exception ex) {
                // TODO: log error
                ex.printStackTrace();
            }


        }

        writer.close();

    }


}