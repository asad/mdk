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

package uk.ac.ebi.mdk.service.loader;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import uk.ac.ebi.mdk.service.index.LuceneIndex;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides index inspection for unit tests. Note the index should not be too larger as this
 * class reads the entire index into a map which can then be queried
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class LuceneIndexInspector {

    private static final Logger LOGGER = Logger.getLogger(LuceneIndexInspector.class);

    private IndexReader reader;
    private Set<String> fields;
    private Document[]  documents;
    private Multimap<String, String> values = HashMultimap.create();

    public LuceneIndexInspector(LuceneIndex index) throws IOException {
        reader    = IndexReader.open(index.getDirectory(), true);
        fields    = new HashSet<String>(reader.getFieldNames(IndexReader.FieldOption.ALL));
        documents = new Document[reader.numDocs()];
    }

    public LuceneIndexInspector load() throws IOException {

        // read the documents and place in a map (stored and not binary)
        for(int i = 0; i < reader.numDocs(); i++){
            documents[i] = reader.document(i);

            for(Fieldable field : documents[i].getFields()){
                if(field.isStored() && !field.isBinary()){
                    for(String value : documents[i].getValues(field.name())){
                        values.put(field.name(), value);
                    }
                }
            }
        }

        return this;

    }

    public Document getDocument(int i){
        return documents[i];
    }

    public boolean hasField(String name){
        return fields.contains(name);
    }

    public boolean hasValue(String field, String value){
        return values.containsKey(field) && values.get(field).contains(value);
    }

    public void close() throws IOException {
        reader.close();
    }


}
