/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.deprecated.services;

import uk.ac.ebi.mdk.domain.identifier.Identifier;

/**
 * @author pmoreno
 * @deprecated simplifed API with uk.ac.ebi.io.service.* interfaces. For this case see IUPACNameService
 */
@Deprecated
public interface ChemicalNameQueryService<I extends Identifier> extends NameQueryService<I> {

    public String getIUPACName(I identifier);

}
