/**
 * Subsystem.java
 *
 * 2011.09.26
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
package uk.ac.ebi.annotation;

import uk.ac.ebi.annotation.base.AbstractStringAnnotation;
import org.apache.log4j.Logger;

/**
 *          Subsystem – 2011.09.26 <br>
 *          Class holding the annotation of a gene locus
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class Locus
        extends AbstractStringAnnotation {

    private static final Logger LOGGER = Logger.getLogger(Locus.class);

    public Locus() {
    }

    public Locus(String locus) {
        super(locus);
    }

    public Locus getInstance() {
        return new Locus();
    }

    public Locus getInstance(String locus) {
        return new Locus(locus);
    }

    /**
     * Determines whether the locus tag defines multiple loci (as a multimeric
     * protein)
     */
    public boolean containsMultiple() {
        return getValue().contains("+");
    }
}
