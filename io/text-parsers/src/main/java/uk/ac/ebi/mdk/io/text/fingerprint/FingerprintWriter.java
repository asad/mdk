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

package uk.ac.ebi.mdk.io.text.fingerprint;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.BitSet;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A writer for the bits of a fingerprint (represented as a BitSet).
 *
 * <blockquote><pre>
 * Writer            writer   = ...;
 * Class<?>          c        = HybridizationFingerprinter.class;
 * FingerprintWriter fpWriter = new FingerPrintWriter(writer, c, 1024);
 *
 * for(IBitFingerprint fp : fps){
 *     fpWriter.write(fp.asBitSet());
 * }
 *
 * fpWriter.closer();
 * </pre></blockquote>
 *
 * @author Pablo Moreno
 * @author John May
 * @see FingerprintReader
 */
public class FingerprintWriter implements Closeable {

    BufferedWriter writer;

    /**
     * Create a write for the specified fingerprint class and length.
     *
     * @param writer output
     * @param c      fingerprint class, mainly for consistency checks
     * @param length the length of the fingerprints
     * @throws IOException              low-level io exception
     * @throws NullPointerException     if the writer or the class was null
     * @throws IllegalArgumentException if the length was zero of less
     */
    public FingerprintWriter(Writer writer, Class<?> c, int length) throws
                                                                    IOException {

        // check arguments
        checkNotNull(writer,      "writer was null");
        checkNotNull(c,           "class was null");
        checkArgument(length > 0, "length should be greater then 0");

        this.writer = new BufferedWriter(writer);

        // initialise
        this.writer.write(c.getSimpleName());
        this.writer.write("\n");
        this.writer.write(Integer.toString(length));
        this.writer.write("\n");
    }

    /**
     * Write the provided fingerprint as text.
     *
     * @param fp a fingerprint
     * @throws IOException low-level IO exception
     */
    public void write(BitSet fp) throws IOException {
        this.writer.write(fp.toString());
        this.writer.write("\n");
    }

    /**
     * @inheritDoc
     */
    @Override public void close() throws IOException {
        this.writer.close();
    }
}
