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

package uk.ac.ebi.mdk.io.text.kegg;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * Enumeration of all possible KEGG Compound fields. These fields can be used in
 * combination with the {@link KEGGCompoundParser} to load specific parts of an
 * entry and ignore others.
 *
 * @author John May
 * @see KEGGCompoundParser
 * @see KeggFlatfile#compound(java.io.File)
 * @see <a href="http://www.kegg.jp/kegg/rest/dbentry.html">DB Entry Format</a>
 */
public enum KEGGCompoundField implements KEGGField {
    ENTRY,
    NAME,
    REACTION,
    FORMULA,
    MASS,
    REMARK,
    PATHWAY,
    ENZYME,
    DBLINKS,
    ATOM,
    BOND,
    COMMENT,
    BRACKET,
    ORIGINAL,
    REPEAT,
    SEQUENCE,
    ORGANISM,
    GENE,
    REFERENCE,

    // > v57
    BRITE,
    EXACT_MASS,
    MOL_WEIGHT,

    // glycan
    COMPOSITION, NODE, EDGE, CLASS, ORTHOLOGY;

    private final Set<String> names;

    private KEGGCompoundField(String... names) {
        ImmutableSet.Builder<String> ns = new ImmutableSet.Builder<String>();
        for (String name : names) {
            ns.add(name);
        }
        ns.add(name());
        this.names = ns.build();
    }

    /** @inheritDoc */
    @Override public Set<String> names() {
        return names;
    }

}
