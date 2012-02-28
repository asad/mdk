package uk.ac.ebi.service.query.data;

import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.service.query.QueryService;

/**
 * MolecularChargeService - 28.02.2012 <br/>
 * <p/>
 * Defines a service that can look-up charge on an given identifier. There
 * is currently no support for searching charge as the results sets would
 * likely be very large. If a specific charge is required one approach
 * could be to search with a different criteria (i.e. molecular formula)
 * and then filter those results for the desired charge
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface MolecularChargeService<I extends Identifier>
        extends QueryService<I> {

    /**
     * Look-up the charge value for a given identifier. If no charge
     * is found then {@see Double.NAN} is returned.
     *
     * @param identifier query identifier
     *
     * @return the charge of the identifier, Double.NAN if no charge is found.
     */
    public Double getCharge(I identifier);

}
