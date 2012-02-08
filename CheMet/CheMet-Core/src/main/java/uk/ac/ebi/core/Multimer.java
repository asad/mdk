/**
 * Multimer.java
 *
 * 2011.10.24
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
package uk.ac.ebi.core;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.biojava3.core.sequence.template.AbstractCompound;
import org.biojava3.core.sequence.template.Sequence;
import uk.ac.ebi.interfaces.Gene;
import uk.ac.ebi.interfaces.entities.GeneProduct;
import uk.ac.ebi.interfaces.Genome;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.resource.protein.BasicProteinIdentifier;

/**
 *          Multimer - 2011.10.24 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class Multimer extends AbstractAnnotatedEntity implements GeneProduct {

    private static final Logger LOGGER = Logger.getLogger(Multimer.class);
    public static final String BASE_TYPE = "Multimer";
    private List<GeneProduct> subunits = new ArrayList();

    public Multimer() {
    }

    public Multimer(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }

    /**
     * Creates a multimeric gene product from existing products. Note the copies
     * are shallow thus changing the names of the product within the multimer
     * will change the names of the products and vise-versa.
     * @param product
     */
    public Multimer(GeneProduct... subunits) {

        super(BasicProteinIdentifier.nextIdentifier(), null, null);


        // make an aggregated name

        StringBuilder idBuilder = new StringBuilder();
        StringBuilder nameBuilder = new StringBuilder();
        StringBuilder abbrBuilder = new StringBuilder();

        for (int i = 0; i < subunits.length; i++) {
            GeneProduct subunit = subunits[i];
            addSubunit(subunit);

            idBuilder.append(subunit.getAccession()).append(i + 1 < subunits.length
                                                            ? " + " : "");
            nameBuilder.append(subunit.getName()).append(i + 1 < subunits.length
                                                         ? " + " : "");
            abbrBuilder.append(subunit.getAbbreviation()).append(i + 1 < subunits.length
                                                                 ? " + " : "");

        }

        setName(nameBuilder.toString());
        setAbbreviation(abbrBuilder.toString());

    }

    public final boolean addSubunit(GeneProduct subunit) {
        return subunits.add(subunit);
    }


    /**
     * Returns a collection (list) of all genes of the subunits
     * @return
     */
    public Collection<Gene> getGenes() {
        List<Gene> genes = new ArrayList();
        for (GeneProduct product : subunits) {
            genes.addAll(product.getGenes());
        }
        return genes;
    }

    /**
     * Throws an UnsupportedOperationException as the call to add a gene to a
     * multimeric product is ambiguous
     */
    public boolean addGene(Gene gene) {
        throw new UnsupportedOperationException("Unable to add a gene to a multimer");
    }

    public List<? extends Sequence> getSequences() {
        List<Sequence> sequences = new ArrayList();
        for (GeneProduct product : subunits) {
            sequences.addAll(product.getSequences());
        }
        return sequences;
    }

    public boolean addSequence(Sequence<? extends AbstractCompound> sequence) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void readExternal(ObjectInput in, Genome genome) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void writeExternal(ObjectOutput out, Genome genome) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public GeneProduct newInstance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
