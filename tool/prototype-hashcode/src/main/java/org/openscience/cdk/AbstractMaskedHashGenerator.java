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

package org.openscience.cdk;

import org.openscience.cdk.hash_mdk.graph.AdjacencyList;
import org.openscience.cdk.hash_mdk.graph.Graph;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.number.Counter;
import org.openscience.cdk.number.DoubleCounter;
import org.openscience.cdk.number.MapCounter;
import org.openscience.cdk.number.NumberRotater;
import org.openscience.cdk.number.PseudoRandomNumber;
import org.openscience.cdk.parity.component.StereoComponent;
import org.openscience.cdk.parity.component.StereoComponentAggregator;
import org.openscience.cdk.parity.locator.EmptyStereoProvider;
import org.openscience.cdk.parity.locator.StereoComponentProvider;
import uk.ac.ebi.mdk.prototype.hash.HashGenerator;
import uk.ac.ebi.mdk.prototype.hash.seed.MaskedSeed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author John May
 */
public abstract class AbstractMaskedHashGenerator<T extends Number & Comparable<T>>
        implements HashGenerator<T> {

    protected final Collection<MaskedSeed> methods;
    private final int depth;
    protected final NumberRotater<T> rotater;
    private final StereoComponentProvider<T> stereoProvider;
    private final AtomMask function;

    public AbstractMaskedHashGenerator(Collection<MaskedSeed> methods, StereoComponentProvider<T> stereoProvider, PseudoRandomNumber<T> generator, int depth, AtomMask function) {
        this.methods = methods;
        this.depth = depth;
        this.rotater = new NumberRotater<T>(generator);
        this.stereoProvider = stereoProvider;
        this.function = function;
    }

    public AbstractMaskedHashGenerator(Collection<MaskedSeed> methods, PseudoRandomNumber<T> generator, int depth, AtomMask mask) {
        this(methods, new EmptyStereoProvider<T>(), generator, depth, mask);
    }

    @Override
    public T generate(IAtomContainer container) {
        return generate(new AdjacencyList(container));
    }

    private String toString(Integer[] values) {
        StringBuilder sb = new StringBuilder(values.length * 10);
        sb.append("{");
        for (int i = 0; i < values.length; i++) {
            sb.append("0x").append(Integer.toHexString(values[i]));
            if (i + 1 < values.length)
                sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }

    public T generate(Graph graph) {
        BitSet mask = generateMask(graph);
        return generate(graph,
                        initialise(graph, mask),
                        new StereoComponentAggregator<T>(stereoProvider
                                                                 .getComponents(graph)),
                        mask,
                        false);
    }

    public T generate(Graph graph, T[] previous, StereoComponent<T> stereo, BitSet mask, boolean modified) {

        int n = graph.n();

        T[] current = Arrays.copyOf(previous, n);

        // initialise counters
        // - global counter keeps track of all values
        // - we add it as a parent to the child counts (1 per atom) - these will
        //   also register all the counts with the parent counter
        List<CounterImpl> counters = new ArrayList<CounterImpl>(n);
        Counter<T> global = new MapCounter<T>(depth * 5 * n);
        for (int i = 0; i < n; i++) {
            counters.add(new CounterImpl(depth * 5, global));
        }

        while (stereo.configure(previous, current)) {
            System.arraycopy(current, 0, previous, 0, n);
        }

        for (int d = 0; d < depth; d++) {

            // seeds for this depth
            for (int i = mask.nextSetBit(0); i >= 0; i = mask
                    .nextSetBit(i + 1)) {
                current[i] = includeNeighbours(previous, graph, i, counters
                        .get(i), mask);
            }

            while (stereo.configure(previous, current)) {
                System.arraycopy(current, 0, previous, 0, n);
            }

            System.arraycopy(current, 0, previous, 0, n);

        }

        // combined the final values (checking for duplicates)
        T hash = initialValue();
        Map<T, Integer> equivalence = new HashMap<T, Integer>((n + 4) % 3);
        for (int i = mask.nextSetBit(0); i >= 0; i = mask.nextSetBit(i + 1)) {

            if (equivalence.get(current[i]) == null)
                equivalence.put(current[i], i);

            hash = xor(hash,
                       rotater.rotate(current[i], global
                               .register(current[i])));

        }

        return equivalence.size() != n && !modified
                ? perturb(equivalence, graph, stereo, mask, hash, current)
                : hash;

    }

    private BitSet generateMask(Graph g) {
        BitSet bs = new BitSet(g.n());
        for (int i = 0; i < g.n(); i++) {
            bs.set(i, function.apply(g.getVertexValue(i)));
        }
        return bs;
    }


    public abstract T initialValue();

    public abstract T xor(T left, T right);

    public abstract int lowOrderBits(T left);

    private T[] modify(T[] values, int index) {
        T[] copy = Arrays.copyOf(values, values.length);
        copy[index] = rotater.rotate(copy[index], 1);
        return copy;
    }


    private T perturb(Map<T, Integer> equivalence, Graph graph, StereoComponent<T> stereo, BitSet mask, T hash, T[] values) {
        int n = graph.n();
        BitSet perturbed = new BitSet(n);
        Counter<T> counter = new MapCounter<T>(n);
        for (int i = mask.nextSetBit(0); i >= 0; i = mask.nextSetBit(i + 1)) {
            int index = equivalence.get(values[i]);

            // don't do terminal atoms (mask doesn't need to be checked)
            if (index != i && graph.neighbors(i).length > 1) {


                // check if we've modified primary index
                if (!perturbed.get(index)) {
                    stereo.reset();
                    T value = generate(graph, modify(values, index), stereo, mask, true);
                    hash = xor(hash, rotater.rotate(value, counter.register(value)));
                }

                // include modification for i
                stereo.reset();
                T value = generate(graph, modify(values, i), stereo, mask, true);
                hash = xor(hash, rotater.rotate(value, counter
                        .register(value)));


                perturbed.set(i);
                perturbed.set(index);
            }

        }

        return hash;
    }


    private T includeNeighbours(T[] current, Graph g, int i, Counter<T> counter, BitSet mask) {

        // keep this un-boxed
        T value = rotater.rotate(current[i], lowOrderBits(current[i]));

        for (int j : g.neighbors(i)) {
            if (mask.get(j))
                value = xor(value, rotater.rotate(current[j], counter
                        .register(current[j])));
        }

        return value;

    }

    public abstract T[] initialise(Graph g, BitSet mask);

    // need this so we can have an array
    private class CounterImpl extends DoubleCounter<T> {
        private CounterImpl(int size, Counter<T> parent) {
            super(new MapCounter<T>(size), parent);
        }
    }

}
