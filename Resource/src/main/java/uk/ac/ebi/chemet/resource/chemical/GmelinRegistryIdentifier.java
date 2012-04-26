
/**
 * KEGGCompoundIdentifier.java
 *
 * 2011.08.16
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.chemet.resource.chemical;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.chemet.resource.util.MIRIAMEntry;
import uk.ac.ebi.resource.IdentifierMetaInfo;
import uk.ac.ebi.resource.Synonyms;


/**
 *
 * @name    KEGGCompoundIdentifier – 2011.08.16
 *          An identifier for KEGG Compound
 *
 * @version $Rev$ : Last Changed $Date$
 *
 * @author  pmoreno
 * @author  $Author$ (this version)
 *
 */
@Brief("Gmelin")
@Description("The Gmelin database is a large database of organometallic and inorganic compounds updated quarterly")
@Synonyms("Gmelin Registry Number")
public class GmelinRegistryIdentifier
  extends ChemicalIdentifier {

    private static final Logger LOGGER = Logger.getLogger(GmelinRegistryIdentifier.class);
    private static final IdentifierMetaInfo DESCRIPTION = IDENTIFIER_LOADER.getMetaInfo(
      GmelinRegistryIdentifier.class);


    public GmelinRegistryIdentifier() {
    }


    public GmelinRegistryIdentifier(String accession) {
        super(accession);
    }


    /**
     * @inheritDoc
     */
    @Override
    public GmelinRegistryIdentifier newInstance() {
        return new GmelinRegistryIdentifier();
    }



    /**
     * @inheritDoc
     */
    @Override
    public String getShortDescription() {
        return DESCRIPTION.brief;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getLongDescription() {
        return DESCRIPTION.description;
    }


    /**
     * @inheritDoc
     */
    @Override
    public MIRIAMEntry getResource() {
        return DESCRIPTION.resource;
    }


}

