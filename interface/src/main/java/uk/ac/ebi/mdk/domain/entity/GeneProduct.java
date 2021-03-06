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
package uk.ac.ebi.mdk.domain.entity;

import org.biojava3.core.sequence.template.AbstractCompound;
import org.biojava3.core.sequence.template.Sequence;
import uk.ac.ebi.mdk.domain.entity.collection.Genome;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.List;


/**
 * GeneProduct – 2011.09.12 <br> Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public interface GeneProduct extends AnnotatedEntity {

    /**
     * Access the name of the gene product
     *
     * @return
     */
    public String getName();


    /**
     * Returns the sequence of the gene product
     *
     * @return a Sequence composed of either AminoAcidCompoundSet or
     *         RNACompoundSet
     */
    public List<? extends Sequence> getSequences();


    /**
     * Add a sequence for the product
     *
     * @param sequence
     */
    public boolean addSequence(Sequence<? extends AbstractCompound> sequence);


    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException;


    public void writeExternal(ObjectOutput out) throws IOException;


    public void readExternal(ObjectInput in, Genome genome) throws IOException, ClassNotFoundException;


    public void writeExternal(ObjectOutput out, Genome genome) throws IOException;


    /**
     * Returns a new instance of the current gene-product type
     *
     * @return
     */
    @Override
    public GeneProduct newInstance();
}
