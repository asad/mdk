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

package uk.ac.ebi.mdk.ui.component.service;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.mdk.service.ResourceLoader;
import uk.ac.ebi.mdk.service.location.LocationFactory;

import javax.swing.*;
import java.awt.*;

/**
 * LoaderGroupFactory - 29.02.2012 <br/>
 * <p/>
 * Provides a panel which groups several resource loaders together
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class LoaderGroupFactory {

    private static final Logger LOGGER = Logger.getLogger(LoaderGroupFactory.class);

    private final Window window;
    private final LocationFactory factory;
    
    public LoaderGroupFactory(Window window, LocationFactory factory) {
        this.window = window;
        this.factory = factory;
    }

    public Box createGroup(String name, ResourceLoader... loaders) {
        Box box = Box.createVerticalBox();
        box.setOpaque(true);
        box.setBackground(Color.WHITE);

        JLabel label = LabelFactory.newLabel(name, LabelFactory.Size.HUGE);
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setBorder(Borders.createEmptyBorder("4dlu, 4dlu, 4dlu, 4dlu"));
        box.add(DefaultComponentFactory.getInstance().createSeparator(label));
        box.add(Box.createVerticalStrut(10));
        for (ResourceLoader loader : loaders) {
            box.add(new LoaderRow(loader, window, factory));
        }
        box.add(Box.createVerticalStrut(10));
        return box;
    }


}
