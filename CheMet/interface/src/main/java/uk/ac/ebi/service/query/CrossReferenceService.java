package uk.ac.ebi.service.query;

import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import uk.ac.ebi.interfaces.identifiers.Identifier;

import java.util.Collection;

/**
 * CrossReferenceService - 29.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface CrossReferenceService<I extends Identifier>
        extends QueryService<I> {

    public static final Term DATABASE_IDENTIFIER_INDEX = new Term("DatabaseName");
    public static final Term DATABASE_ACCESSION = new Term("DatabaseAccession");

    /**
     * Access all cross-references for a given identifier
     * @param identifier
     * @return
     */
    public Collection<? extends Identifier> getCrossReferences(I identifier);

    /**
     * Access cross-references of a specific type
     * @param identifier
     * @param filter
     * @param <T>
     * @return
     */
    public <T extends Identifier> Collection<T> getCrossReferences(I identifier, Class<T> filter);

    /**
     * Search the cross-references
     * @param crossReference
     * @return
     */
    public Collection<I> searchCrossReferences(Identifier crossReference);

}
