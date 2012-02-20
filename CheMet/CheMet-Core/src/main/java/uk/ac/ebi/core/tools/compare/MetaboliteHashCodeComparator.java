package uk.ac.ebi.core.tools.compare;

import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.core.tools.hash.MolecularHashFactory;
import uk.ac.ebi.core.tools.hash.seeds.*;
import uk.ac.ebi.interfaces.annotation.ChemicalStructure;
import uk.ac.ebi.interfaces.entities.Metabolite;


/**
 *          MetaboliteHashCodeComparator 2012.02.16 <br/>
 *          Class realises MetaboliteComparator using the hash codes of the
 *          structures to compare metabolites
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MetaboliteHashCodeComparator
        implements MetaboliteComparator {

    private static final Logger LOGGER = Logger.getLogger(MetaboliteHashCodeComparator.class);

    private final MolecularHashFactory factory = MolecularHashFactory.getInstance();

    private final Set<AtomSeed> seeds;


    public MetaboliteHashCodeComparator() {
        seeds = SeedFactory.getInstance().getSeeds(AtomicNumberSeed.class, ConnectedAtomSeed.class, BondOrderSumSeed.class);
    }


    public MetaboliteHashCodeComparator(Set<AtomSeed> seeds) {
        this.seeds = seeds;
    }


    /**
     * Compares the {@see ChemicalStructure} annotations of the metabolites
     * using the {@see MolecularHashFactory} to generate molecular hash codes.
     * If any of the chemical structure hashes are equal they metabolites
     * are considered equal
     * @inheritDoc
     */
    public boolean areEqual(Metabolite query, Metabolite subject) {

        Set<Integer> queryHashes = new HashSet<Integer>();
        Set<Integer> subjectHashes = new HashSet<Integer>();

        for (ChemicalStructure structure : query.getStructures()) {
            IAtomContainer atomcontainer = structure.getStructure();
            queryHashes.add(factory.getHash(atomcontainer, seeds).hash);
        }

        for (ChemicalStructure structure : subject.getStructures()) {
            IAtomContainer atomcontainer = structure.getStructure();
            subjectHashes.add(factory.getHash(atomcontainer, seeds).hash);
        }

        for (Integer queryHash : queryHashes) {
            for (Integer subjectHash : subjectHashes) {
                if (queryHash.equals(subjectHash)) {
                    return true;
                }
            }
        }

        return false;

    }
}