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
package uk.ac.ebi.mdk.apps;

import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * CommandLineMain.java – EMBL-EBI – MetabolicDevelopmentKit – Jun 1, 2011
 * Abstract object for when a main is runnable on the common line. The object effectively provides
 * a wrapper for command option processing utilising the Apache Commons CLI library
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public abstract class CommandLineMain
        extends ArrayList<Option> {

    private static final Logger            LOGGER  = Logger.getLogger(CommandLineMain.class);
    private              Options           options = new Options();
    private              CommandLineParser parser  = new PosixParser();
    private              CommandLine       cmdLine = null;

    public abstract void setupOptions();

    /**
     * Main processing method
     */
    public abstract void process();

    public final void process(String[] args) {

        setupOptions();

        for (Option opt : this) {
            options.addOption(opt);
        }
        options.addOption(new Option("h", "help", false, "print the help section"));


        try {
            cmdLine = parser.parse(options, args);
        } catch (ParseException ex) {
            //  showHelp();
            LOGGER.error("There was a problem parsing command line options: " + ex.getMessage());
        }

        if (cmdLine.hasOption('h') ||
                cmdLine.hasOption("help")) {
            showHelp();
        }

        process();

    }

    public CommandLine getCommandLine() {
        return cmdLine;
    }

    /**
     * Check whether a value/flag is set
     *
     * @param option
     *
     * @return
     */
    public boolean has(String option) {
        return getCommandLine().hasOption(option);
    }

    /**
     * Access a command line value
     *
     * @param option
     * @param defaultValue
     *
     * @return
     */
    public String get(String option, String defaultValue) {
        return getCommandLine().getOptionValue(option, defaultValue);
    }

    /**
     * Convenience method for accessing a file from the parsed options. Note the method does not check if
     * the file exists
     *
     * @param option
     *
     * @return
     *
     * @throws IllegalArgumentException
     */
    public File getFile(String option) throws IllegalArgumentException {

        if (getCommandLine().hasOption(option)) {
            return new File(getCommandLine().getOptionValue(option));
        } else {
            System.err.println("Missing required file parameter '" + option + "'");
            showHelp();
        }

        // not reached
        return new File("");

    }

    /**
     * Convenience method for accessing a file from the parsed options. Note the method does not check if
     * the file exists
     *
     * @param option
     *
     * @return
     *
     * @throws IllegalArgumentException
     */
    public File getFile(String option, String defaultFilePrefix, String defaultExtension) throws IllegalArgumentException {

        if (getCommandLine().hasOption(option)) {
            return new File(getCommandLine().getOptionValue(option));
        } else {
            try {
                return File.createTempFile(defaultFilePrefix, defaultExtension);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        throw new InvalidParameterException("Could not get file option");
    }

    public void showHelp() {
        for (Object obj : toArray(new Option[0])) {
            Option opt = (Option) obj;
            System.out.println(String.format("    [%s|%s]\n        %s \n", opt.getOpt(), opt.getLongOpt(),
                                             opt.getDescription()));
        }
        System.exit(0);
    }
}
