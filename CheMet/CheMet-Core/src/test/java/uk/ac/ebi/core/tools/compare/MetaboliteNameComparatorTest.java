/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.core.tools.compare;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.annotation.Synonym;
import uk.ac.ebi.core.DefaultEntityFactory;
import uk.ac.ebi.interfaces.entities.EntityFactory;
import uk.ac.ebi.interfaces.entities.Metabolite;


/**
 *
 * @author johnmay
 */
public class MetaboliteNameComparatorTest {

    private MetaboliteComparator comparator;

    private EntityFactory factory;

    private Metabolite m1;

    private Metabolite m2;


    public MetaboliteNameComparatorTest() {

        comparator = new MetaboliteNameComparator();
        factory = DefaultEntityFactory.getInstance();
    }


    @Before
    public void createNewMeatbolites() {
        m1 = factory.newInstance(Metabolite.class);
        m2 = factory.newInstance(Metabolite.class);
    }


    @Test
    public void testNameEquality() {

        m1.setName("ATP");
        m2.setName("Adenosine TP");

        Assert.assertFalse(comparator.areEqual(m1, m2));

        m2.setName("atp");

        Assert.assertTrue(comparator.areEqual(m1, m2));

    }


    @Test
    public void testSynonymEquality() {

        m1.setName("ATP");
        m2.setName("Adenosine TP");

        Assert.assertFalse(comparator.areEqual(m1, m2));

        m2.addAnnotation(new Synonym("atp"));

        Assert.assertTrue(comparator.areEqual(m1, m2));

    }
}
