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

package uk.ac.ebi.mdk.domain.annotation;

import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.lang.annotation.Unique;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.entity.Metabolite;

/**
 * Virtual - 20.03.2012 <br/>
 * <p/>
 * Serves to mark metabolites that are lumped/average composition and not 'real' entities
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@Unique
@Context(value = {Metabolite.class, MetabolicReaction.class})
@Brief("Virtual")
@Description("Indicate that a metabolite or reactions is not a discrete entity and rather it is " +
                     "an average of many entities. It is common practise to include such metabolites" +
                     "as DNA/RNA/Fatty Acid Composition in biomass reactions")

public class Virtual extends AbstractAnnotation
        implements Flag {

    // the number of atom's at which a metabolite "may" be lumped
    private static final int LUMPED_THRESHOLD = 500;

    private Virtual() {
    }

    private static class LumpedHolder {
        private static Virtual INSTANCE = new Virtual();
    }

    public static Virtual getInstance() {
        return LumpedHolder.INSTANCE;
    }

    /**
     * Checks whether the formula for a metabolite contains more then the threshold number
     * of atom's (currently 500)
     *
     * @param entity the entity to test a match for
     *
     * @return
     */
    @Override
    public boolean matches(AnnotatedEntity entity) {

        // if the entity is a metabolite we can check the formula for large number's of atoms
        if (entity instanceof Metabolite
                && entity.hasAnnotation(MolecularFormula.class)) {

            MolecularFormula annotation = entity.getAnnotations(MolecularFormula.class).iterator().next();
            IMolecularFormula formula = annotation.getFormula();

            if (formula != null && MolecularFormulaManipulator.getAtomCount(formula) > LUMPED_THRESHOLD) {
                return true;
            }

        }

        return false;

    }

    @Override
    public String toString() {
        return getShortDescription();
    }

    @Override
    public Virtual newInstance() {
        return LumpedHolder.INSTANCE;
    }
}
