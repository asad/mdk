/**
 * KEGGCompoundNameService.java
 *
 * 2011.10.27
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.io.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import uk.ac.ebi.interfaces.services.NameQueryService;
import uk.ac.ebi.io.remote.KEGGCompoundNames;
import uk.ac.ebi.resource.chemical.KEGGCompoundIdentifier;

/**
 *          KEGGCompoundNameService - 2011.10.27 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class KEGGCompoundNameService
        extends KEGGCompoundQueryService
        implements NameQueryService<KEGGCompoundIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(KEGGCompoundNameService.class);
    private IndexSearcher searcher;
    private Term nameTerm = new Term("name");

    private KEGGCompoundNameService() {
        super(new KEGGCompoundNames());
        try {
            searcher = new IndexSearcher(getDirectory(), true);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    public static KEGGCompoundNameService getInstance() {
        return KEGGCompoundNameServiceHolder.INSTANCE;
    }

    private static class KEGGCompoundNameServiceHolder {

        private static final KEGGCompoundNameService INSTANCE = new KEGGCompoundNameService();
    }

    public Collection<KEGGCompoundIdentifier> fuzzySearchForName(String name) {
        Query q = new FuzzyQuery(nameTerm.createTerm(name));
        return search(q);
    }

    public Collection<KEGGCompoundIdentifier> searchForName(String name) {
        Query q = new TermQuery(nameTerm.createTerm(name));
        return search(q);
    }

    public Collection<String> getNames(KEGGCompoundIdentifier identifier) {
        try {
            Query q = NumericRangeQuery.newIntRange("id", identifier.getValue(), identifier.getValue(), true, true);


            TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            if (hits.length > 1) {
                LOGGER.info("more then one hit found for an id! this shouldn't happen");
            }
            Collection<String> names = new HashSet<String>(hits.length);
            for (ScoreDoc scoreDoc : hits) {
                return Arrays.asList(getValues(scoreDoc, "name"));
            }
            return names;
        } catch (IOException ex) {
            LOGGER.info(ex.getMessage());
        }
        return null;
    }

    private Collection<KEGGCompoundIdentifier> search(Query query) {
        Collection<KEGGCompoundIdentifier> ids = new HashSet<KEGGCompoundIdentifier>();
        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                ids.add(new KEGGCompoundIdentifier(String.format("C%05d", Integer.parseInt(getValue(scoreDoc, "id")))));
            }
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query);
        }

        return ids;
    }
}