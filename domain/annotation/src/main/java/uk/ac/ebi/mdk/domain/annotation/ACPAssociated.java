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

import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.lang.annotation.Unique;
import uk.ac.ebi.mdk.domain.entity.Metabolite;

import java.util.regex.Pattern;

/**
 * Marks metabolites that are associated to Acyl Carrier Protein using looking for the
 * name 'ACP'
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@Unique
@Context(Metabolite.class)
@Brief("ACP Associated")
@Description("Marks a metabolite as being attached to an Acyl Carrier Protein")
public class ACPAssociated extends AbstractAnnotation implements Flag {

    private static final Pattern ACP_NAME = Pattern.compile("ACP");

    private ACPAssociated() {
    }

    private static class ACPAssociatedHolder {
        private static ACPAssociated INSTANCE = new ACPAssociated();
    }

    public static ACPAssociated getInstance() {
        return ACPAssociatedHolder.INSTANCE;
    }

    @Override
    public boolean matches(AnnotatedEntity entity) {
        if (entity instanceof Metabolite) {
            if (ACP_NAME.matcher(entity.getName()).find()) {
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
    public ACPAssociated newInstance() {
        return ACPAssociatedHolder.INSTANCE;
    }
}
