/*
 * Copyright (c) 2012. John May <jwmay@users.sf.net>
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

package org.openscience.cdk.hash;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.number.MapCounter;

import java.util.Arrays;

/**
 * A basic molecule hash combines the individual atom hashes into a single
 * value.
 *
 * @author John May
 */
public class BasicMoleculeHash extends AbstractHashGenerator
        implements MoleculeHashGenerator {

    /* prime number to start our combined hash from */
    private static final long INITIAL_HASH_VALUE = 49157L;

    /* a generator for our individual atom hashes */
    private AtomHashGenerator atomGenerator;

    public BasicMoleculeHash(AtomHashGenerator atomGenerator) {
        this.atomGenerator = atomGenerator;
    }

    @Override
    public Long generate(IAtomContainer container) {

        Long[] hashes = atomGenerator.generate(container);
        long hash = INITIAL_HASH_VALUE;

        Counter counter = new Counter(hashes.length);
        for (Long h : hashes) {
            hash ^= rotate(h, counter.register(h));
        }

        return hash;

    }
}