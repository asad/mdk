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

package uk.ac.ebi.mdk.domain.identifier;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.deprecated.MIR;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@MIR(14)
public class KEGGReactionIdentifier extends AbstractIdentifier {

    private static final Logger LOGGER = Logger.getLogger(KEGGReactionIdentifier.class);

    public KEGGReactionIdentifier() {
        super();
    }

    public KEGGReactionIdentifier(String accession) {
        super(accession);
    }

    @Override
    public KEGGReactionIdentifier newInstance() {
        return new KEGGReactionIdentifier();
    }
}
