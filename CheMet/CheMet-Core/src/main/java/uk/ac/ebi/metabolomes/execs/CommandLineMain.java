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
package uk.ac.ebi.metabolomes.execs;

import java.util.ArrayList;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * CommandLineMain.java – EMBL-EBI – MetabolicDevelopmentKit – Jun 1, 2011
 * Abstract object for when a main is runnable on the common line. The object effectively provides
 * a wrapper for command option processing utilising the Apache Commons CLI library
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public abstract class CommandLineMain
        extends ArrayList<Option> {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( CommandLineMain.class );
    private Options options = new Options();
    private CommandLineParser parser = new PosixParser();
    private CommandLine cmd = null;

    public abstract void setupOptions();

    public abstract void process();

    public CommandLineMain( String[] args ) {
        setupOptions();

        for ( Option opt : this ) {
            options.addOption( opt );
        }
        options.addOption( new Option( "h" , "help" , false , "print the help section" ) );


        try {
            cmd = parser.parse( options , args );
        } catch ( ParseException ex ) {
            //  printHelp();
            logger.error( "There was a problem parsing command line options: " + ex.getMessage() );
        }

        if (  cmd.hasOption( 'h' )
             || cmd.hasOption( "help" ) ) {
            printHelp();
        }
    }

    public CommandLine getCmd() {
        return cmd;
    }

    public void printHelp() {
        for ( Object obj : options.getOptions().toArray( new Option[ 0 ] ) ) {
            Option opt = ( Option ) obj;
            System.out.println( String.format( "  -%s|--%-30s " , opt.getOpt() , opt.getLongOpt() ) + opt.getDescription() );
        }
        System.exit( 0 );
    }
}