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

package uk.ac.ebi.mdk.service.query.name;

import org.apache.lucene.index.Term;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.QueryService;

import java.util.Collection;

/**
 * NameService - 21.02.2012 <br/>
 * <p/>
 * A general purpose name service. This service will normally be used
 * to aggregate {@see PreferredNameService}, {@see SynonymService} and
 * {@see IUPACService} into a common search and fetch method. It can
 * also be used when a database does not make a distinction between
 * an IUPAC Name, Preferred Name or Synonym name
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface NameService<I extends Identifier>
        extends QueryService<I> {

    /**
     * Term to be used if you can not make a distinction between
     * naming schema
     */
    public static Term NAME = new Term("Name");

    /**
     * Search for the provide name. If the service provides a preferred name,
     * IUPAC name and a synonyms all three should be search with this method
     *
     * @param name name to search for
     * @param approximate perform a approximate search
     * @return collection of service specific identifiers matching the provided name
     */
    public Collection<I> searchName(String name, boolean approximate);

    /**
     * Access all names for a given identifier. As with {@see searchName}, if the
     * service provides multiple naming sub-divisions this method will return all
     * name's for a given identifier
     *
     * @param identifier identifier to get the names for
     * @return collection of names for the given identifier in this service
     */
    public Collection<String> getNames(I identifier);

    
}
