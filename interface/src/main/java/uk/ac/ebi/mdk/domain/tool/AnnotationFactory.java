package uk.ac.ebi.mdk.domain.tool;

import uk.ac.ebi.chemet.annotation.Flag;
import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.Annotation;

import java.util.List;
import java.util.Set;

/**
 * @author John May
 */
public interface AnnotationFactory {

    public List<Annotation> ofContext(AnnotatedEntity entity);


    /**
     * Access a list of annotations that are specific to the
     * provided entity class. Note: the annotations contained
     * with in the list should only be used to construct
     * new entities and not to store information. The instances_old
     * instead of the classes are returned to avoid extra object
     * creation.
     *
     * @param entityClass class of the entity you which to get context
     *                    annotations for
     *
     * @return List of annotations that can be added to that class which
     *         are intended for new instantiation only
     */
    public List<Annotation> ofContext(Class<? extends AnnotatedEntity> entityClass);


    /**
     * Construct an empty annotation of the given class type. Note there is an
     * overhead off using this method over {@ofIndex(Byte)} as the Byte index is
     * first looked up in the AnnotationLoader. The average speed reduction is
     * 1800 % slower (note this is still only about 1/3 second for 100 000
     * objects).
     *
     * @param c
     *
     * @return
     */
    public <A extends Annotation> A ofClass(Class<? extends A> c);



    /**
     * Access a set of annotation flags that could match this entity. This provides suggestion
     * of matching flag's for this entity using the {@see AbstractFlag.matches(AnnotatedEntity)}
     *
     * @param entity the entity to collect matching flags for
     *
     * @return annotations which could be added to the entity (may need user prompt)
     *
     * @see uk.ac.ebi.chemet.annotation.Flag#matches(uk.ac.ebi.interfaces.AnnotatedEntity)
     */
    public Set<Flag> getMatchingFlags(AnnotatedEntity entity);


}
