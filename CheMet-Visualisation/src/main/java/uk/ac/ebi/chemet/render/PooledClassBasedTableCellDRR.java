/**
 * PooledClassBasedListCellDRR.java
 *
 * 2011.12.12
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
package uk.ac.ebi.chemet.render;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.TableCellRenderer;
import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.render.list.renderers.PoolBasedListRenderer;
import uk.ac.ebi.chemet.render.list.renderers.PoolBasedTableRenderer;

/**
 *          PooledClassBasedListCellDRR - 2011.12.12 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class PooledClassBasedTableCellDRR
        extends ClassBasedTableCellDDR {

    private static final Logger LOGGER = Logger.getLogger(PooledClassBasedTableCellDRR.class);
    List<PoolBasedTableRenderer> pools = new ArrayList<PoolBasedTableRenderer>();

    @Override
    public void setRenderer(Class<?> aClass,
                            TableCellRenderer renderer) {

        if (renderer instanceof PoolBasedTableRenderer) {
            pools.add((PoolBasedTableRenderer) renderer);
        }

        super.setRenderer(aClass, renderer);

    }

    public void checkIn(Object object) {
        for (PoolBasedTableRenderer pool : pools) {
            pool.checkIn(object);
        }
    }
}