/*
 * Copyright (C) 2012  John May and Pablo Moreno
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package uk.ac.ebi.mdk.tool.match;

import uk.ac.ebi.mdk.domain.entity.Entity;


/**
 * Interface defines a method to comparing two entities and determining
 * whether those two entities are matches.
 *
 * @author johnmay
 */
public interface EntityMatcher<E extends Entity, M> {

    /**
     * Determines whether the query entities are matches in the score of the
     * comparator.
     *
     * @param query   the query entity
     * @param subject the subject entity
     *
     * @return whether the entities are matches in the score of this test
     */
    public Boolean matches(E query, E subject);

    /**
     * Calculates the required metric for this method metric.
     *
     * @param entity
     *
     * @return
     */
    public M calculatedMetric(E entity);

    /**
     * Compares metrics
     *
     * @param queryMetric
     * @param subjectMetric
     *
     * @return
     */
    public Boolean matches(M queryMetric, M subjectMetric);

}
