
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

/**
 * FingerprintEncoder.java
 *
 * 2011.09.22
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
package uk.ac.ebi.mdk.tool.resolve;

import org.apache.log4j.Logger;


/**
 *          FingerprintEncoder – 2011.09.22 <br>
 *          A basic fingerprint
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class FingerprintEncoder extends AbstractEncoder {

    private static final Logger LOGGER = Logger.getLogger(FingerprintEncoder.class);


    public String encode(String string) {

        String clean = string.trim();
        clean = string.toLowerCase();
        String preClean = clean;
        clean = removeChargeBrace(clean);
        clean = addSpaceWhereDashesAre(clean);
        clean = removeControlCharacters(clean);
        clean = removeHTMLTags(clean);
        clean = removeGenericBeginning(clean);
        clean = reorderFragments(clean);

        return asciify(clean);

    }


}

