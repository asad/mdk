
/**
 * GeneProduct.java
 *
 * 2011.09.12
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

import uk.ac.ebi.interfaces.entities.GeneProduct;
import java.util.Map;


/**
 *          GeneProduct – 2011.09.12 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface Multimer
  extends GeneProduct {

 
    /**
     *
     * Mutator to add subunit to the complex. The name the protein is use as the name of subunit
     *
     */
    public boolean addSubunit(GeneProduct product);


    /**
     *
     * Remove a subunit from the product
     *
     * @param product
     * @return
     */
    public boolean removeSubunit(GeneProduct product);





}
