package uk.ac.ebi.io.service.loader.location;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * GZIPRemoteLocation.java - 20.02.12 <br/>
 * GZIPRemoteLocation defines a remote resource location of a compressed stream, i.e. on an FTP server or
 * HTML page
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class GZIPSystemLocation
       extends SystemLocation {

    private InputStream stream;

    public GZIPSystemLocation(String key, File location) {
        super(key, location);
    }

    /**
     * Open a gzip stream to the remote resource. This first opens the URLConnection and then the stream
     *
     * @return the opened stream
     * @throws java.io.IOException
     */
    public InputStream open() throws IOException {
        if (stream == null) {
            stream     = new GZIPInputStream(new FileInputStream(getLocation()));
        }
        return stream;
    }

    /**
     * Close the open stream to the remote resource
     *
     * @throws java.io.IOException
     */
    public void close() throws IOException {
        if (stream != null) {
            stream.close();
            stream = null;
        }
    }


}
