/**
 * BiochemicalReaction.java
 *
 * 2011.08.08
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
package uk.ac.ebi.chemet.entities.reaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ebi.metabolomes.core.gene.OldGeneProduct;

/**
 * @name    BiochemicalReaction
 * @date    2011.08.08
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   Class extends Reaction but adds the option to add a modifier (i.e. enzyme)
 *
 */
public class BiochemicalReaction<M , S , C>
        extends Reaction<M , S , C> {

    private static final Logger LOGGER = Logger.getLogger( BiochemicalReaction.class );
    private List<OldGeneProduct> modifiers;

    public BiochemicalReaction( List<OldGeneProduct> modifiers ) {
        this.modifiers = modifiers;
    }

    public List<OldGeneProduct> getModifiers() {
        return Collections.unmodifiableList( modifiers );
    }

    public void addModifier( OldGeneProduct modifier ) {
        this.modifiers.add( modifier );
    }
}
