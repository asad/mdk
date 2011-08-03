/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.util.inchi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV2000Writer;

/**
 * This class is probably not needed anymore considering that the embedded inchi
 * generator provides std inchi calculator (CDK 1.3.8) and Java 1.6
 *
 * @author pmoreno
 */
public class StdInChIGenerator {

    // TODO change this to a property file
    private static final String pathToShellScript = "/Users/pmoreno/Downloads/STDINCHI-1-API/STDINCHI/gcc_makefile/";
    private static final String shellInchi2Mol = "inchi2struct.sh";
    private static final String shellMol2Inchi = "struct2inchi.sh";

    public static IAtomContainer getCDKMolFromInChI(String inchi) throws IOException {
        File tmpDir = new File(pathToShellScript+File.separator+"dir"+Math.round(1000 * Math.random()));
        if(!tmpDir.mkdir()) {
            System.out.println("Cannot create temporary directory");
            return null;
        }
        String tmpFileName = tmpDir.getAbsolutePath()+File.separator+"fileCDKMolFromInChI";
        Runtime rt = Runtime.getRuntime();
        FileWriter w = new FileWriter(new File(tmpFileName));
        w.write(inchi+"\n");
        w.close();
        Process p = rt.exec(pathToShellScript + shellInchi2Mol + " " + tmpFileName);
        try {
            p.waitFor();
        } catch (InterruptedException ex) {
            System.out.println("Problems with script execution");
            Logger.getLogger(StdInChIGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        MDLV2000Reader r = new MDLV2000Reader(new FileReader(tmpFileName + ".mol"));
        try {
            IAtomContainer mol = (IMolecule) r.read(DefaultChemObjectBuilder.getInstance().newInstance(Molecule.class));
            r.close();
            deleteTmpDirAndFiles(tmpDir);
            return mol;
        } catch (Exception ex) {
            Logger.getLogger(StdInChIGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static InChIResult getInChIFromCDKMol(IAtomContainer mol) throws IOException {
        String[] inchiRes = getInChIAndKeyFromCDKMol(mol);
        InChIResult res = new InChIResult();
        res.setInchi(inchiRes[0]);
        res.setInchiKey(inchiRes[1]);
        res.setInchiKey(null);
        return res;
    }

    public static String[] getInChIAndKeyFromCDKMol(IAtomContainer mol) throws IOException {
        //TODO check that this is the best way of handling this. Shouldn't we have an exception here maybe?
        if(!checkMoleculeForInChI(mol)) {
            System.out.println("Molecule does not pass check for inchi");
            return null;
        }
        File tmpDir = new File(pathToShellScript+File.separator+"dir"+Math.round(1000 * Math.random()));
        if(!tmpDir.mkdir()) {
            System.out.println("Cannot create temporary directory");
            return null;
        }
        String tmpFileName = tmpDir.getAbsolutePath()+File.separator+"fileInChiFromCDKMol";
        MDLV2000Writer w = new MDLV2000Writer(new FileWriter(new File(tmpFileName)));
        talkAboutMol(mol);
        try {
            w.writeMolecule(mol);
        } catch (Exception ex) {
            Logger.getLogger(StdInChIGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        w.close();
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec(pathToShellScript + shellMol2Inchi + " " + tmpFileName);
        try {
            p.waitFor();
        } catch (InterruptedException ex) {
            Logger.getLogger(StdInChIGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedReader r = new BufferedReader(new FileReader(tmpFileName + "_Out.txt"));
        String line = r.readLine();
        String[] tokens = line.split("\t");
        if (tokens.length < 4) {
            return null;
        }
        String[] res = new String[2];
        res[0] = tokens[1];
        res[1] = tokens[3];
        deleteTmpDirAndFiles(tmpDir);
        return res;
    }

    private static boolean deleteTmpDirAndFiles(File tmpDir) {
        File[] files = tmpDir.listFiles();
        boolean ans = true;
        for (File file : files) {
            ans = ans && file.delete();
        }
        return ans && tmpDir.delete();
    }

    private static void talkAboutMol(IAtomContainer mol) {
        System.out.println("Mol loaded has " + mol.getAtomCount() + " atoms, " + mol.getBondCount() + " bonds.");
        int counter = 1;
        for (IAtom atom : mol.atoms()) {
            System.out.println("Atom " + counter + ": " + atom.getSymbol());
            counter++;
        }
    }

    private static boolean checkMoleculeForInChI(IAtomContainer mol) {
        // Has pseudoatoms?
        if (mol.getAtomCount() == 0) {
            System.out.println("Has no atoms!");
            return false;
        }
        boolean[] flags = mol.getFlags();
        for (IBond b : mol.bonds()) {
            if (b == null) {
                System.out.println("Null bonds for: " + mol.getID());
                return false;
            }
            if (b.getAtomCount() < 2) {
                System.out.println("bond with less than two atoms: " + mol.getID());
                return false;
            }
            if (b.getAtom(0) == null || b.getAtom(1) == null) {
                System.out.println("Bond with one or two atoms null: " + mol.getID());
                return false;
            }
        }
        for (IAtom a : mol.atoms()) {
            String className = a.getClass().getName();
            if (className.equalsIgnoreCase("org.openscience.cdk.PseudoAtom")) {
                return false;
            }
        }
        return true;
    }

}