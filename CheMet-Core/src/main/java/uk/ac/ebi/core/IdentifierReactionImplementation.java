package uk.ac.ebi.core;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.entities.reaction.AbstractReaction;
import uk.ac.ebi.mdk.domain.entity.reaction.IdentifierReaction;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.entity.reaction.Participant;

/**
 * IdentifierReactionImplementation - 05.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class IdentifierReactionImplementation<I extends Identifier>
        extends AbstractReaction<Participant<I, Double>>
        implements IdentifierReaction<I> {

    private static final Logger LOGGER = Logger.getLogger(IdentifierReactionImplementation.class);

    @Override
    public IdentifierReaction newInstance() {
        return new IdentifierReactionImplementation();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
