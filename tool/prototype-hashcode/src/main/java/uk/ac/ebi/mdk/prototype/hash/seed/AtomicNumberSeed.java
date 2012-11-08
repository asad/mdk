/*
 * Copyright (c) 2012. John May <jwmay@sf.net>
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
package uk.ac.ebi.mdk.prototype.hash.seed;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * NonNullAtomicNumberSeed - 2011.11.09 <br>
 * Atomic number seed used the atomic number to provide a seed
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class AtomicNumberSeed implements AtomSeed {

    public AtomicNumberSeed() {
    }

    public int seed(IAtomContainer molecule, IAtom atom) {
        return atom.getAtomicNumber() != null ? atom.getAtomicNumber().hashCode() :
                257;
    }

    @Override
    public String toString() {
        return "Atomic Number (nullable)";
    }
}