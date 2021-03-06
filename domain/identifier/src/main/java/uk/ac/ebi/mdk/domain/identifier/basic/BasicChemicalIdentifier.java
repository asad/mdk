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

package uk.ac.ebi.mdk.domain.identifier.basic;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.preference.type.IncrementalPreference;
import uk.ac.ebi.caf.utility.preference.type.StringPreference;
import uk.ac.ebi.mdk.ResourcePreferences;
import uk.ac.ebi.mdk.domain.identifier.AbstractChemicalIdentifier;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


/**
 *          BasicChemicalIdentifier – 2011.09.14 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@Brief("Chemical")
@Description("A basic auto-incrementing identifier for chemical compounds")
public class BasicChemicalIdentifier
        extends AbstractChemicalIdentifier {

    private static final Logger LOGGER = Logger.getLogger(BasicChemicalIdentifier.class);

    private String shortDesc;


    public BasicChemicalIdentifier() {
        super();
    }


    public BasicChemicalIdentifier(String accession) {
        super(accession);
    }

    // TODO: Remove short description and make a 'DynamicIdentifier'
    public BasicChemicalIdentifier(String accession, String shortDescription) {
        super(accession);
        this.shortDesc = shortDescription;
    }

    private static String nextAccession(){
        StringPreference format = ResourcePreferences.getInstance().getPreference("BASIC_CHEM_ID_FORMAT");
        IncrementalPreference ticker = ResourcePreferences.getInstance().getPreference("BASIC_CHEM_ID_TICK");
        return String.format(format.get(), ticker.get());
    }
    

    public static BasicChemicalIdentifier nextIdentifier() {
        return new BasicChemicalIdentifier(nextAccession());
    }


    /**
     * Sets the shortDescription of the class. This allows handling of chemical
     * resources that either don't have a MIRIAM entry or a CheMet identifier
     * class
     * @param shortDesc short description, for example CAS
     */
    public void setShortDescription(String shortDesc) {
        this.shortDesc = shortDesc;
    }


    /**
     * @inheritDoc
     */
    @Override
    public BasicChemicalIdentifier newInstance() {
        return new BasicChemicalIdentifier();
    }


    /**
     * Returns the short description as set ({@see setShortDescription(String)})
     * or if no description is set the super class method is called.
     * @return
     */
    @Override
    public String getShortDescription() {
        return this.shortDesc != null ? this.shortDesc : super.getShortDescription();
    }


    /**
     * @inheritDoc
     */
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);

        if (in.readBoolean()) {
            this.shortDesc = in.readUTF();
        }
    }


    /**
     * @inheritDoc
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        if (this.shortDesc != null) {
            out.writeBoolean(true);
            out.writeUTF(shortDesc);
        }
        out.writeBoolean(false);
    }


    public static void main(String[] args) {
        System.out.println(BasicChemicalIdentifier.nextIdentifier());
        System.out.println(BasicChemicalIdentifier.nextIdentifier());
    }
}
