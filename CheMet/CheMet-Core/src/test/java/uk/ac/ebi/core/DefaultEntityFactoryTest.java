/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.core;

import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;
import uk.ac.ebi.interfaces.Gene;
import uk.ac.ebi.interfaces.entities.EntityFactory;
import uk.ac.ebi.interfaces.entities.Metabolite;


/**
 *
 * @author johnmay
 */
public class DefaultEntityFactoryTest {

    public DefaultEntityFactoryTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testNewInstance() {

        EntityFactory factory = DefaultEntityFactory.getInstance();
        
        Gene gene = factory.newInstance(Gene.class);
        Assert.assertEquals(GeneImplementation.class, gene.getClass());
        
        Metabolite metabolite = factory.newInstance(Metabolite.class);
        Assert.assertEquals(uk.ac.ebi.core.Metabolite.class, metabolite.getClass());

    }
}
