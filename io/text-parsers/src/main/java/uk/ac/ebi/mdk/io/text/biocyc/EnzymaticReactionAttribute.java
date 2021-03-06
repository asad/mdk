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

package uk.ac.ebi.mdk.io.text.biocyc;

import uk.ac.ebi.mdk.io.text.attribute.Attribute;

/**
 * Attributes for BioCyc enzrxns.dat file
 *
 * @author John May
 * @link http://bioinformatics.ai.sri.com/ptools/flatfile-format.html#enzrxns.dat
 */
public enum EnzymaticReactionAttribute implements Attribute {
    UNIQUE_ID,
    TYPES,
    COMMON_NAME,
    ALTERNATIVE_COFACTORS,
    ALTERNATIVE_SUBSTRATES,
    CITATIONS,
    COFACTOR_BINDING_COMMENT,
    COFACTORS,
    COFACTORS_OR_PROSTHETIC_GROUPS,
    COMMENT,
    ENZYME,
    KM,
    PH_OPT,
    PROSTHETIC_GROUPS,
    REACTION,
    REACTION_DIRECTION,
    REGULATED_BY,
    REQUIRED_PROTEIN_COMPLEX,
    SYNONYMS,
    TEMPERATURE_OPT;

    private String name;
    private String pattern;

    EnzymaticReactionAttribute() {
        this.name = name().replaceAll("_", "-");
        this.pattern = name;
    }

    EnzymaticReactionAttribute(String name) {
        this(name, name);
    }

    EnzymaticReactionAttribute(String name, String pattern) {
        this.name = name;
        this.pattern = pattern;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPattern() {
        return pattern;
    }
}