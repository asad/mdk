/*
 *     This file is part of Metabolic Network Builder
 *
 *     Metabolic Network Builder is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.metabolomes.identifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import uk.ac.ebi.metabolomes.core.gene.GeneProduct;
import uk.ac.ebi.metabolomes.core.gene.GeneProteinProduct;
import uk.ac.ebi.metabolomes.resource.Resource;

/**
 * IdentifierFactory.java
 *
 *
 * @author johnmay
 * @date May 6, 2011
 */
public class IdentifierFactory {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( IdentifierFactory.class );

    /**
     * Builds an identifier given the accession
     * Uses the identifier parse method to validate ids (slower)
     * @param resource
     * @param accession
     */
    public static AbstractIdentifier getIdentifier( Resource resource , String accession ) {

        Constructor constructor = resource.getIdentifierConstructor();
        if ( constructor != null ) {
            try {
                return ( AbstractIdentifier ) constructor.newInstance( accession , true );
            } catch ( InstantiationException ex ) {
                ex.printStackTrace();
            } catch ( IllegalAccessException ex ) {
                ex.printStackTrace();
            } catch ( IllegalArgumentException ex ) {
                ex.printStackTrace();
            } catch ( InvocationTargetException ex ) {
                ex.printStackTrace();
            }
        }
        return new GenericIdentifier( accession );
    }

    public static AbstractIdentifier getUncheckedIdentifier( Resource resource , String accession ) {

        Constructor constructor = resource.getIdentifierConstructor();
        if ( constructor != null ) {
            try {
                return ( AbstractIdentifier ) constructor.newInstance( accession , false );
            } catch ( InstantiationException ex ) {
                ex.printStackTrace();
            } catch ( IllegalAccessException ex ) {
                ex.printStackTrace();
            } catch ( IllegalArgumentException ex ) {
                ex.printStackTrace();
            } catch ( InvocationTargetException ex ) {
                ex.printStackTrace();
            }
        }
        return new GenericIdentifier( accession );
    }

    public static void main( String[] args ) {

        GeneProduct product = new GeneProteinProduct();
        UniProtIdentifier id = ( UniProtIdentifier ) IdentifierFactory.getIdentifier( Resource.UNIPROT , "QYEUEE.1" );
        System.out.println( "Created Id:" + id );

        // multiple id parsing
        String ids = "gi|254777906|sp|B9E8Z7.1|DNAA_MACCJ";
        List idList = IdentifierFactory.getIdentifiers( ids );
        for ( Object object : idList ) {
            System.out.println( object.getClass() + " " + object );
        }

    }

    /**
     * Builds a list of identifiers from a string that may
     * or maynot contain multiple identifiers
     * atm: handle gi|39327|sp|398339 etc..
     * @param idsString
     * @return
     */
    public static List<AbstractIdentifier> getIdentifiers( String idsString ) {

        List<AbstractIdentifier> hitIdentifiers = new ArrayList<AbstractIdentifier>();

        if ( idsString.contains( ID_SEPERATOR ) ) {

            ListIterator<String> it = Arrays.asList( idsString.split( ID_ESCAPED_SEPERATOR ) ).listIterator();

            // db identifiers , gi,sp,tr etc..
            while ( it.hasNext() ) {

                String dbid = it.next();

                if ( dbid.length() <= DBID_MAX_LENGTH ) {
                    Resource r = Resource.getResource( dbid );

                    if ( r != Resource.UNKNOWN && r != Resource.GENERAL ) {
                        hitIdentifiers.add( IdentifierFactory.getIdentifier( r , it.next() ) );
                    }
                    else if ( r == Resource.GENERAL && it.hasNext() ) {
                        dbid = it.next();
                        r = Resource.getResource( dbid );
                        if ( r != Resource.UNIPROT ) {
                            hitIdentifiers.add( IdentifierFactory.getIdentifier( r , it.next() ) );
                        } else {
                            it.previous();
                        }
                    }

                } else {

                    hitIdentifiers.add( new GenericIdentifier( dbid ) );
                }
            }
        } else {
            hitIdentifiers.add( new GenericIdentifier( idsString ) );
        }

        return hitIdentifiers;
    }
    private static final String ID_SEPERATOR = "|";
    private static final String ID_ESCAPED_SEPERATOR = "\\|";
    private static final int DBID_MAX_LENGTH = 3;
}