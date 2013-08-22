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
package uk.ac.ebi.mdk.tool.domain;

import java.io.StringReader;
import net.sf.jniinchi.INCHI_RET;
import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.inchi.InChIToStructure;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV3000Reader;
import org.openscience.cdk.silent.AtomContainer;
import org.openscience.cdk.tools.AtomTypeTools;
import uk.ac.ebi.mdk.domain.identifier.InChI;

/**
 * Simplify building of molecules from several formats.
 * @author  johnmay
 * @deprecated subtleties in loading, causes more issues then it solves
 */
@Deprecated
public class CDKMoleculeBuilder {

    private static final Logger LOGGER = Logger.getLogger( CDKMoleculeBuilder.class );
    private static IChemObjectBuilder CHEM_OBJECT_BUILDER = DefaultChemObjectBuilder.getInstance();
    private static InChIGeneratorFactory inChIGeneratorFactory;
    private static AtomTypeTools typeTools = new AtomTypeTools();

    private CDKMoleculeBuilder() {
        typeTools = new AtomTypeTools();

        try {
            inChIGeneratorFactory = InChIGeneratorFactory.getInstance();

        } catch ( CDKException ex ) {
            ex.printStackTrace();
        }
    }

    private static class CDKMoleculeBuilderHolder {

        private static CDKMoleculeBuilder INSTANCE = new CDKMoleculeBuilder();
    }

    public static CDKMoleculeBuilder getInstance() {
        return CDKMoleculeBuilderHolder.INSTANCE;
    }

    /**
     * Given and InChI string and a molFile string try and build a IMolecule object
     * @param inchi
     * @param molFile
     * @return
     */
    public IAtomContainer build( InChI inchi , String molFile ) {

        // Todo smiles
        IAtomContainer molecule = null;

        if ( inchi.getInchi() != null ) {
            molecule = buildFromInChI( inchi );
            if ( molecule != null ) {
                return molecule;
            }
        }

        if ( molFile.isEmpty() == Boolean.FALSE ) {
            molecule = buildFromMolFileV2000( new MDLV2000Reader( new StringReader( molFile ) ) );
            if ( molecule != null ) {
                try {
                    typeTools.assignAtomTypePropertiesToAtom( molecule );
                } catch ( Exception ex ) {
                }
                return molecule;
            }

            molecule = buildFromMolFileV3000( new MDLV3000Reader( new StringReader( molFile ) ) );
            if ( molecule != null ) {
                try {
                    typeTools.assignAtomTypePropertiesToAtom( molecule );
                } catch ( Exception ex ) {
                }
                return molecule;
            }
        }

        return null;
    }

    /**
     * Builds an IMolecule from an InChI String
     * @param inchi
     * @return
     */
    public IAtomContainer buildFromInChI( InChI inchi ) {

        try {
            InChIToStructure inchiToStructure =
                             inChIGeneratorFactory.getInChIToStructure( inchi.getInchi() ,
                                                                        CHEM_OBJECT_BUILDER );

            String inchiString = inchi.getInchi();

            if ( inchiString.isEmpty() ) {
                return null;
            }

            INCHI_RET ret = inchiToStructure.getReturnStatus();
            if ( ret == INCHI_RET.WARNING ) {
                // Structure generated, but with warning message
                LOGGER.debug( "InChI warning: " + inchiToStructure.getMessage() );
            } else if ( ret != INCHI_RET.OKAY ) {
                LOGGER.warn( "Structure generation failed" );
                return null;
            }

            IAtomContainer result = inchiToStructure.getAtomContainer();
            IAtomContainer molecule = new AtomContainer( result );
            return molecule;

        } catch ( CDKException ex ) {
            LOGGER.error( "Structure generation failed" + ex.getMessage() );
        }

        return null;

    }

    /**
     * Builds an IMolecule from a V2000 mol file
     * @param reader
     * @return
     */
    public IAtomContainer buildFromMolFileV2000( MDLV2000Reader reader ) {

        IAtomContainer template = new AtomContainer();

        try {
            return reader.read( template );
        } catch ( CDKException ex ) {
            // do nothing
            LOGGER.error( ex.getMessage() );
        }
        return null;
    }

    /**
     * Builds an IMolecule from a V3000 mol file
     * @param reader
     * @return
     */
    public IAtomContainer buildFromMolFileV3000( MDLV3000Reader reader ) {
        IAtomContainer template = new AtomContainer();
        try {
            return reader.read( template );
        } catch ( CDKException ex ) {
            // do nothing
            LOGGER.error( ex.getMessage() );

        }
        return null;
    }

}
