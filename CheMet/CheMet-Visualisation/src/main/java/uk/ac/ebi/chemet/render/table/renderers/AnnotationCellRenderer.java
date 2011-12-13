/**
 * BasicAnnotationCellRenderer.java
 *
 * 2011.09.29
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
package uk.ac.ebi.chemet.render.table.renderers;

import com.google.common.base.Joiner;
import java.awt.Component;
import java.util.Collection;
import javax.swing.JLabel;
import javax.swing.JTable;
import uk.ac.ebi.chemet.render.ViewUtilities;
import uk.ac.ebi.chemet.render.factory.LabelFactory;
import uk.ac.ebi.chemet.render.list.renderers.TableCellRenderingPool;
import uk.ac.ebi.interfaces.Annotation;

/**
 *          AnnotationCellRenderer – 2011.09.29 <br>
 *          A simple annotation cell renderer that joins the toString() values 
 *          of a collection of annotations using a comma and space ', '.
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class AnnotationCellRenderer
        extends TableCellRenderingPool<JLabel, Object> {

    private boolean html = false;
    private String token = ", ";

    public AnnotationCellRenderer() {
    }

    public AnnotationCellRenderer(boolean html,
                                  String token) {
        this.html = html;
        this.token = token;
    }

    @Override
    public boolean setup(JLabel component,
                         Object value) {
        String text = value instanceof Collection
                      ? Joiner.on(token).join((Collection) value)
                      : value.toString();


        component.setText(html ? ViewUtilities.htmlWrapper(text) : text);

        return true;

    }

    @Override
    public JLabel create() {
        return LabelFactory.newLabel("empty");
    }

    @Override
    public void expire(JLabel component) {
        // do nothing
    }
}