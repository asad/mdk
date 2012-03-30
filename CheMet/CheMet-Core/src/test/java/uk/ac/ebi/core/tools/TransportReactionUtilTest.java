/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.core.tools;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.chemet.resource.basic.BasicChemicalIdentifier;
import uk.ac.ebi.chemet.resource.basic.BasicReactionIdentifier;
import uk.ac.ebi.core.MetabolicReactionImplementation;
import uk.ac.ebi.core.MetaboliteImplementation;
import uk.ac.ebi.core.reaction.MetabolicParticipantImplementation;
import uk.ac.ebi.core.reaction.compartment.Organelle;
import uk.ac.ebi.interfaces.entities.Metabolite;
import uk.ac.ebi.interfaces.reaction.Direction;

import static org.junit.Assert.assertEquals;


/**
 *
 * @author johnmay
 */
public class TransportReactionUtilTest {

    public TransportReactionUtilTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testGetClassification_SYMPORT() {

        System.out.print("testGetClassification_SYMPORT ");

        Metabolite atp = new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "atp", "ATP");
        Metabolite alanine = new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "dala", "D-Alanine");

        MetabolicReactionImplementation rxn = new MetabolicReactionImplementation(BasicReactionIdentifier.nextIdentifier(), "st", "symportTest");

        rxn.addReactant(new MetabolicParticipantImplementation(atp, Organelle.CYTOPLASM));
        rxn.addReactant(new MetabolicParticipantImplementation(alanine, Organelle.CYTOPLASM));

        rxn.addProduct(new MetabolicParticipantImplementation(atp, Organelle.EXTRACELLULA));
        rxn.addProduct(new MetabolicParticipantImplementation(alanine, Organelle.EXTRACELLULA));

        rxn.setDirection(Direction.BACKWARD.FORWARD);

        System.out.println(rxn + " : " + TransportReactionUtil.getClassification(rxn));


        assertEquals(TransportReactionUtil.Classification.SYMPORTER, TransportReactionUtil.getClassification(rxn));

    }


    @Test
    public void testGetClassification_ANTIPORT() {
        System.out.print("testGetClassification_ANTIPORT ");


        MetaboliteImplementation atp = new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "atp", "ATP");
        MetaboliteImplementation alanine = new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "dala", "D-Alanine");

        MetabolicReactionImplementation rxn = new MetabolicReactionImplementation(BasicReactionIdentifier.nextIdentifier(), "st", "symportTest");

        rxn.addReactant(new MetabolicParticipantImplementation(atp, Organelle.CYTOPLASM));
        rxn.addReactant(new MetabolicParticipantImplementation(alanine, Organelle.EXTRACELLULA));

        rxn.addProduct(new MetabolicParticipantImplementation(atp, Organelle.EXTRACELLULA));
        rxn.addProduct(new MetabolicParticipantImplementation(alanine, Organelle.CYTOPLASM));

        rxn.setDirection(Direction.FORWARD);

        System.out.println(rxn + " : " + TransportReactionUtil.getClassification(rxn));

        assertEquals(TransportReactionUtil.Classification.ANTIPORTER, TransportReactionUtil.getClassification(rxn));

    }


    @Test
    public void testGetClassification_UNIPORT() {
        System.out.print("testGetClassification_UNIPORT ");


        MetaboliteImplementation atp = new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "atp", "ATP");
        MetaboliteImplementation alanine = new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "dala", "D-Alanine");

        MetabolicReactionImplementation rxn = new MetabolicReactionImplementation(BasicReactionIdentifier.nextIdentifier(), "st", "symportTest");

        rxn.addReactant(new MetabolicParticipantImplementation(atp, Organelle.CYTOPLASM));
        rxn.addReactant(new MetabolicParticipantImplementation(alanine, Organelle.EXTRACELLULA));

        rxn.addProduct(new MetabolicParticipantImplementation(atp, Organelle.CYTOPLASM));
        rxn.addProduct(new MetabolicParticipantImplementation(alanine, Organelle.CYTOPLASM));

        rxn.setDirection(Direction.BACKWARD);

        System.out.println(rxn + " : " + TransportReactionUtil.getClassification(rxn));
        assertEquals(TransportReactionUtil.Classification.UNIPORTER, TransportReactionUtil.getClassification(rxn));

    }
}
