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
package uk.ac.ebi.mdk.ui.render.table;

import com.google.common.base.Joiner;
import uk.ac.ebi.caf.utility.TextUtility;

import javax.swing.*;
import java.util.Collection;


/**
 * AnnotationCellRenderer – 2011.09.29 <br>
 * A simple annotation cell renderer that joins the toString() values
 * of a collection of annotations using a comma and space ', '.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class AnnotationCellRenderer
        extends DefaultRenderer {

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
    public JLabel getComponent(JTable table, Object value, int row, int column) {
        String text = value instanceof Collection
                      ? Joiner.on(token).join((Collection) value)
                      : value.toString();


        this.setText(html ? TextUtility.html(text) : text);
        this.setToolTipText(html ? TextUtility.html(text) : text);

        return this;
    }

}
