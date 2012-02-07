/**
 * Reaction.java
 *
 * 2012.02.07
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
package uk.ac.ebi.interfaces.entities;

import java.util.List;
import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.reaction.Direction;
import uk.ac.ebi.interfaces.reaction.Participant;
import uk.ac.ebi.interfaces.reaction.ReactionType;


/**
 *
 *          Reaction 2012.02.07
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Interface describes a reaction
 *
 */
public interface Reaction<P extends Participant<?, ?>>
        extends AnnotatedEntity {

    public List<P> getReactants();


    public List<P> getProducts();


    public boolean addReactant(P participant);


    public boolean addProduct(P participant);


    public boolean removeReactant(P participant);


    public boolean removeProduct(P participant);


    public int getReactantCount();


    public int getProductCount();


    public int getParticipantCount();


    /**
     * Transport, Generic, Exchange, Metabolic (default)
     */
    public ReactionType getType();


    public void setType(ReactionType type);


    public Direction getDirection();


    public void setDirection(Direction direction);
}
