package uk.ac.ebi.chemet.service.index.other;

import uk.ac.ebi.chemet.service.index.StandardNIOIndex;

/**
 * TaxonomyIndex - 23.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class TaxonomyIndex extends StandardNIOIndex {

    public TaxonomyIndex() {
        super("UniProt Taxonomy", "uniprot/taxonomy");
    }


}
