/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package uk.ac.ebi.metabolomes.run;

import uk.ac.ebi.chemet.io.external.RunnableTask;
import uk.ac.ebi.metabolomes.descriptor.observation.BlastParamType;
import uk.ac.ebi.metabolomes.descriptor.observation.JobParamType;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import org.biojava3.core.sequence.AccessionID;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.io.FastaWriterHelper;
import uk.ac.ebi.metabolomes.core.gene.OldGeneProductCollection;
import uk.ac.ebi.metabolomes.core.gene.GeneProteinProduct;
import uk.ac.ebi.metabolomes.io.homology.BlastXML;
import uk.ac.ebi.metabolomes.descriptor.observation.JobParameters;


/**
 * BlastHomologySearch.java
 *
 *
 * @author johnmay
 * @date Apr 27, 2011
 */
@Deprecated
public class OldBlastHomologySearch extends RunnableTask {

    private static final org.apache.log4j.Logger logger =
                                                 org.apache.log4j.Logger.getLogger(
      OldBlastHomologySearch.class);
    private Process process;
    private File seqFile;
    private File xmlFile;
    private String command = "";
    // controller words for setting up job


    /**
     * Constructor for a blast homology search
     * with the bare minimum information
     * @param peptides
     * @param program
     * @param database
     */
    public OldBlastHomologySearch(JobParameters params) {

        super(params);
        addParameter(BlastParamType.OUTPUT_MODE, "7");

    }


    public void writeSequences() {
        // write peptides to file
        try {

            seqFile = File.createTempFile("blast-input", ".fa");
            xmlFile = new File(seqFile.getPath() + ".hits.xml");

            addParameter(BlastParamType.INPUT_FILE, seqFile);
            addParameter(BlastParamType.OUTPUT_FILE, xmlFile);
            addParameter(BlastParamType.NUMBER_CPU, 4);

            Collection<ProteinSequence> sequenceSubset = new ArrayList<ProteinSequence>();

            OldGeneProductCollection gpc = (OldGeneProductCollection) super.getJobParameters().get(
              JobParamType.GENE_PRODUCT_COLLECTION);

            for( GeneProteinProduct product : gpc.getProteinProducts() ) {
                ProteinSequence ps = new ProteinSequence(product.getSequence());
                ps.setAccession(new AccessionID(product.getIdentifier().toString()));
                sequenceSubset.add(ps);
            }
            FastaWriterHelper.writeProteinSequence(seqFile, sequenceSubset);
        } catch( IOException ex ) {
            setErrorStatus();
            ex.printStackTrace();
        } catch( Exception ex ) {
            setErrorStatus();
            ex.printStackTrace();
        }
    }


    public void prerun() {

        writeSequences();

        JobParameters parameters = getJobParameters();

        // build the command
        StringBuilder commandBuilder = new StringBuilder();
        commandBuilder.append(parameters.get(BlastParamType.PROGRAM));
        commandBuilder.append(parameters.getAsArgument(BlastParamType.EXPECTED_VALUE_THRESHOLD));
        commandBuilder.append(parameters.getAsArgument(BlastParamType.MATRIX));
        commandBuilder.append(parameters.getAsArgument(BlastParamType.DATABASE));
        commandBuilder.append(parameters.getAsArgument(BlastParamType.INPUT_FILE));
        commandBuilder.append(parameters.getAsArgument(BlastParamType.OUTPUT_FILE));
        commandBuilder.append(parameters.getAsArgument(BlastParamType.OUTPUT_MODE));
        commandBuilder.append(parameters.getAsArgument(BlastParamType.NUMBER_CPU));

        command = commandBuilder.toString();
        logger.info("running:" + command);

    }


    /**
     * The main runnable method
     */
    public void run() {

        // if completed or in error don't run
        if( isCompleted() ) {
            return;
        }

        try {
            logger.debug("command: " + command);
            process = Runtime.getRuntime().exec(command);
            setRunningStatus();
            process.waitFor();
            setCompletedStatus();
        } catch( InterruptedException ex ) {
            setErrorStatus();
            logger.error("Error when waiting for process", ex);
        } catch( IOException ex ) {
            setErrorStatus();
            logger.error("Error executing cmds: " + command, ex);
        }

    }


    /**
     * Loads the observations from the output
     */
    public void postrun() {

        logger.debug("loading homology observations");

        // parse xmlFile and load them into the peptides objects
        // Document blastXML = XMLHelper.buildDocument( xmlFile );
        System.out.println(xmlFile);
        BlastXML blastXML = new BlastXML(xmlFile);

        // need to provide the GeneProductCollect to loadProteinHomologies
        // so the observations are loaded into those peptides and
        // subsequently the project they are assoicated with
        JobParameters params = getJobParameters();
        blastXML.loadProteinHomologyObservations((OldGeneProductCollection) params.get(
          JobParamType.GENE_PRODUCT_COLLECTION),
                                                 params);
    }


    /**
     * Return the process object of the command
     * @return Process object for accessing exit status, Standard Out/Error
     */
    public Process getProcess() {
        return process;
    }


    @Override
    public String getTaskDescription() {
        return "Blast Homology Search";
    }


    @Override
    public String getTaskCommand() {
        return command;
    }


}
