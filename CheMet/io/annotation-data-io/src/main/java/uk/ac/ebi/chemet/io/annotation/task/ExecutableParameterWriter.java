package uk.ac.ebi.chemet.io.annotation.task;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.task.ExecutableParameter;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.chemet.io.annotation.AnnotationWriter;

import java.io.DataOutput;
import java.io.IOException;

/**
 * ParameterWriter - 10.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class ExecutableParameterWriter implements AnnotationWriter<ExecutableParameter> {

    private static final Logger LOGGER = Logger.getLogger(ExecutableParameterWriter.class);

    DataOutput out;

    public ExecutableParameterWriter(DataOutput out){
        this.out = out;
    }

    @Override
    public void write(ExecutableParameter annotation) throws IOException {

        out.writeUTF(annotation.getShortDescription());
        out.writeUTF(annotation.getLongDescription());
        out.writeUTF(annotation.getValue().getAbsolutePath());

    }
}