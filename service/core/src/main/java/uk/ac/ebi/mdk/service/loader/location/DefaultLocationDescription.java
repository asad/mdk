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

package uk.ac.ebi.mdk.service.loader.location;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.service.location.LocationDescription;
import uk.ac.ebi.mdk.service.location.ResourceLocation;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * DefaultLocationDescription - 23.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author: johnmay $ (this version)
 * @version $Rev: 1719 $
 */
public class DefaultLocationDescription implements LocationDescription {

    private static final Logger LOGGER = Logger.getLogger(DefaultLocationDescription.class);

    private String key;
    private String name;
    private String description;
    private Class<? extends ResourceLocation> c;
    private ResourceLocation defaultLocation;


    // key making pattern removes all non-alphanumeric characters that are between alphanumeric chars
    private static final Pattern ALPHANUMERIC = Pattern.compile("(?<=[A-z0-9]+)[^A-z0-9]+(?=[A-z0-9]+)");
    // remove excess characters (at prefix/suffix)
    private static final Pattern ALPHANUMERIC_DOT = Pattern.compile("[^A-z0-9\\.]+");

    public <T extends ResourceLocation> DefaultLocationDescription(String name,
                                                                   String description,
                                                                   Class<T> c,
                                                                   T defaultLocation) {
        this.name = name;
        this.description = description;
        this.c = c;
        this.defaultLocation = defaultLocation;
        this.key = createKey(name);
    }

    public DefaultLocationDescription(String name, String description, Class<? extends ResourceLocation> c) {
        this.name = name;
        this.description = description;
        this.c = c;
        this.key = createKey(name);
    }

    public static String createKey(String name) {
        String prekey = ALPHANUMERIC.matcher(name).replaceAll(".").toLowerCase(Locale.ENGLISH);
        return ALPHANUMERIC_DOT.matcher(prekey).replaceAll("");
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Class<? extends ResourceLocation> getType() {
        return c;
    }

    @Override
    public boolean hasDefault() {
        return defaultLocation != null;
    }

    @Override
    public ResourceLocation getDefault() {
        return defaultLocation;
    }
}
