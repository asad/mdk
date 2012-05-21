/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.webservices;

import java.util.Map;
import java.util.ArrayList;
import com.google.common.collect.Multimap;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.chemet.ws.exceptions.WebServiceException;
import uk.ac.ebi.metabolomes.webservices.eutils.PubChemNamesResult;
import uk.ac.ebi.metabolomes.webservices.eutils.MeSHResult;
import static org.junit.Assert.*;

/**
 *
 * @author pmoreno
 */
public class EUtilsWebServiceConnectionTest {

    private EUtilsWebServiceConnection instance;

    public EUtilsWebServiceConnectionTest() {
        instance = new EUtilsWebServiceConnection();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getPubChemSubstanceFromPubChemCompound method, of class EUtilsWebServiceConnection.
     */
    @Test
    public void testGetPubChemSubstanceFromPubChemCompound() throws Exception {
        System.out.println("getPubChemSubstanceFromPubChemCompound");
        List<String> pubchemCompoundIds = new ArrayList<String>();
        pubchemCompoundIds.add("44264212");
        pubchemCompoundIds.add("44134622");
        pubchemCompoundIds.add("24201024");
        pubchemCompoundIds.add("16218850");
        pubchemCompoundIds.add("16133838");
        pubchemCompoundIds.add("16133648");
        pubchemCompoundIds.add("16132374");
        pubchemCompoundIds.add("16132321");
        pubchemCompoundIds.add("16132312");
        pubchemCompoundIds.add("16132280");
        pubchemCompoundIds.add("16131310");
        pubchemCompoundIds.add("16129677");
        pubchemCompoundIds.add("16129627");
        pubchemCompoundIds.add("16051918");
        pubchemCompoundIds.add("11979494");

        Multimap<String, String> result = instance.getPubChemSubstanceFromPubChemCompound(pubchemCompoundIds);

        assertNotNull(result);

        System.out.println("Compound\tSubstance");
        for (String compoundID : result.keySet()) {
            for (String substance : result.get(compoundID)) {
                System.out.println(compoundID + "\t" + substance);
                assertTrue(compoundID.length() > 0);
                assertTrue(substance.length() > 0);
            }
        }
    }

    @Test
    public void testGetExternalIdentifiersForPubChemCompound() throws Exception {
        System.out.println("testGetExternalIdentifiersForPubChemCompounds");
        List<String> pubchemComps = new ArrayList<String>();
        pubchemComps.add("53477538");

        Multimap<String, String> cpd2subs = instance.getPubChemSubstanceFromPubChemCompound(pubchemComps);
        for (String cpd : cpd2subs.keySet()) {
            Multimap<String, CrossReference> result = instance.getExternalIdentifiersForPubChemSubstances(cpd2subs.get(cpd));

            assertNotNull(result);

            System.out.println("Compound\tSubstance\tExtDB\tExtID");
            for (String subsID : result.keySet()) {
                System.out.println("For substance:" + subsID);
                for (CrossReference crossReference : result.get(subsID)) {
                    System.out.println(cpd + "\t" + subsID + "\t" + crossReference.getShortDescription() + "\t" + crossReference.getIdentifier().getAccession());
                }
            }
        }


    }

    @Test
    public void testGetExternalIdentifiersForPubChemSubstances() throws Exception {
        System.out.println("testGetExternalIdentifiersForPubChemSubstances");
        List<String> pubchemSubstances = new ArrayList<String>();
        pubchemSubstances.add("30179699");
        pubchemSubstances.add("103178971");

        Multimap<String, CrossReference> result = instance.getExternalIdentifiersForPubChemSubstances(pubchemSubstances);

        assertNotNull(result);

        System.out.println("Substance\tExtDB\tExtID");
        for (String subsID : result.keySet()) {
            for (CrossReference crossReference : result.get(subsID)) {
                System.out.println(subsID + "\t" + crossReference.getShortDescription() + "\t" + crossReference.getIdentifier().getAccession());
            }
        }


    }

    @Test
    public void testCitricAcid() throws Exception {
        System.out.println("testCitricAcid");
        List<String> pubchemCompCitricAcid = new ArrayList<String>(1);
        pubchemCompCitricAcid.add("311");

        Multimap<String, String> pccomp2subs = instance.getPubChemSubstanceFromPubChemCompound(pubchemCompCitricAcid);
        assertTrue(pccomp2subs.values().size() > 0);
        System.out.println("Substances retrieved: " + pccomp2subs.values().size());

        List<String> subs = new ArrayList<String>();
        subs.addAll(pccomp2subs.get("311"));
        Multimap<String, CrossReference> refs = instance.getExternalIdentifiersForPubChemSubstances(subs);

        for (String substance : subs) {
            for (CrossReference crossReference : refs.get(substance)) {
                System.out.println(substance + "\t" + crossReference.getShortDescription() + "\t" + crossReference.getIdentifier().getAccession());
            }
        }
    }

    @Test
    public void testPreferredNameRetrievalForPubChemCIDs() throws Exception {
        System.out.println("testPreferredNameRetrievalForPubChemCIDs");
        List<String> pubchemCompoundIds = new ArrayList<String>();
        pubchemCompoundIds.add("949");
        pubchemCompoundIds.add("44264212");
        pubchemCompoundIds.add("44134622");
        pubchemCompoundIds.add("24201024");
        pubchemCompoundIds.add("16218850");
        pubchemCompoundIds.add("16133838");
        pubchemCompoundIds.add("16133648");
        pubchemCompoundIds.add("16132374");
        pubchemCompoundIds.add("16132321");
        pubchemCompoundIds.add("16132312");
        pubchemCompoundIds.add("16132280");
        pubchemCompoundIds.add("16131310");
        pubchemCompoundIds.add("16129677");
        pubchemCompoundIds.add("16129627");
        pubchemCompoundIds.add("16051918");
        pubchemCompoundIds.add("11979494");

        pubchemCompoundIds.add("447286"); // To test AC1L case

        Map<String, String> pccomp2Names = instance.getPreferredNameForPubChemCompounds(pubchemCompoundIds);
        assertTrue(pccomp2Names.values().size() > 0);

        for (String pccompId : pccomp2Names.keySet()) {
            System.out.println(pccompId + "\t" + pccomp2Names.get(pccompId));
        }

        PubChemNamesResult res = instance.getNamesForPubChemCompounds(pubchemCompoundIds);

        for (String pccompId : res.getCompoundIds()) {
            for (String syn : res.getSynonyms(pccompId)) {
                System.out.println(pccompId + "\tS:" + syn);
            }
        }
    }
    
    @Test
    public void testGetMeSHTerms() {
        System.out.println("testGetMeSHTerms");
        List<String> meshTermIDs = new ArrayList<String>();
        meshTermIDs.add("68001685");
        meshTermIDs.add("68018925");
        
        MeSHResult res = instance.getMeSHObjects(meshTermIDs);
        
        for (String meshId : meshTermIDs) {
            System.out.println(meshId+"\t"+res.getMeSHTermName(meshId));
        }
    }

    @Test
    public void testGetPubChemCompoundParentFromPubChemCompound() throws WebServiceException {
        System.out.println("testPreferredNameRetrievalForPubChemCIDs");
        List<String> pubchemCompoundIds = new ArrayList<String>();
        pubchemCompoundIds.add("57449");
        pubchemCompoundIds.add("5962");
        pubchemCompoundIds.add("25202645");
        pubchemCompoundIds.add("25201779");
        pubchemCompoundIds.add("121885");
        pubchemCompoundIds.add("3593277");
        pubchemCompoundIds.add("58");
        pubchemCompoundIds.add("7059571");
        pubchemCompoundIds.add("6076");
        pubchemCompoundIds.add("449043");
        pubchemCompoundIds.add("25245190");
        pubchemCompoundIds.add("439291");
        pubchemCompoundIds.add("25244768");
        pubchemCompoundIds.add("5459867");
        pubchemCompoundIds.add("119405");
        pubchemCompoundIds.add("445049");
        pubchemCompoundIds.add("25245222");
        pubchemCompoundIds.add("439424");
        pubchemCompoundIds.add("25245232");
        pubchemCompoundIds.add("57449");
        pubchemCompoundIds.add("5962");
        pubchemCompoundIds.add("6560146");
        
        
        Multimap<String,String> pccomp2parentComp = instance.getPubChemCompoundParentFromPubChemCompound(pubchemCompoundIds);
        
        for (String pccomp : pubchemCompoundIds) {
            for (String parent : pccomp2parentComp.get(pccomp)) {
                System.out.println(pccomp+" --> "+parent);
            }
        }

    }
}
