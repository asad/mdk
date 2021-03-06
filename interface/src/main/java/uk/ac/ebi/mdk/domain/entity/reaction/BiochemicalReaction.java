/*
 * Copyright (C) 2013 EMBL-EBI
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
package uk.ac.ebi.mdk.domain.entity.reaction;

import uk.ac.ebi.mdk.domain.entity.GeneProduct;

import java.util.Collection;

/**
 * Describes a biochemical reaction which binds enzyme modifiers ({@link
 * GeneProduct}s) to a {@link MetabolicReaction}.
 *
 * @author Pablo Moreno <pablacious at users.sf.net>
 */
public interface BiochemicalReaction extends MetabolicReaction {

    /**
     * Adds the gene product 'g' as modifier for the reaction.
     *
     * @param g add a modifier
     * @return true if the modifier was added.
     */
    public boolean addModifier(GeneProduct g);

    /**
     * Removes gene product 'p' as from the list of modifiers registered with
     * the reaction.
     *
     * @param g remove a modifier
     * @return whether the modifier was removed, false = no pressent
     */
    public boolean removeModifier(GeneProduct g);

    /**
     * Returns the underlying metabolic reaction.
     *
     * @return the delegated reaction
     */
    public MetabolicReaction getMetabolicReaction();

    /**
     * access collection of modifiers (enzymes) of this reaction.
     *
     * @return immutable collection of modifiers
     */
    public Collection<GeneProduct> getModifiers();
}
