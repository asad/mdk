
/**
 * MolecularFormula.java
 *
 * 2011.09.14
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
package uk.ac.ebi.annotation.chemical;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import uk.ac.ebi.annotation.AbstractAnnotation;
import uk.ac.ebi.annotation.util.AnnotationLoader;
import uk.ac.ebi.core.Description;


/**
 *          MolecularFormula – 2011.09.14 <br>
 *          Annotation of molecular formula
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MolecularFormula
  extends AbstractAnnotation {

    private static final Logger LOGGER = Logger.getLogger(MolecularFormula.class);
    private IMolecularFormula formula;
    private String stringFormula;
    private static Description description = AnnotationLoader.getInstance().get(
      MolecularFormula.class);


    /**
     *
     * Default constructor need for externalization
     *
     */
    public MolecularFormula() {
    }


    /**
     *
     * Constructs a formula annotation with a provided {@see MolecularFormula} from CDK library
     *
     * @param formula
     *
     */
    public MolecularFormula(IMolecularFormula formula) {
        this.formula = formula;
        this.stringFormula = MolecularFormulaManipulator.getString(formula);
    }


    /**
     *
     * Construct a MolecularFormula annotation from a string
     *
     * @param formula
     *
     */
    public MolecularFormula(String formula) {
        IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
        this.stringFormula = formula;
        this.formula = MolecularFormulaManipulator.getMolecularFormula(formula, builder);
    }


    /**
     *
     * Accessor to the underlying formula object
     *
     * @return An instance of IMolecularFormula
     *
     */
    public IMolecularFormula getFormula() {
        return formula;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return stringFormula;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getShortDescription() {
        return description.shortDescription;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getLongDescription() {
        return description.longDescription;
    }


    /**
     * @inheritDoc
     */
    @Override
    public Byte getIndex() {
        return description.index;
    }


    /**
     * @inheritDoc
     */
    @Override
    public MolecularFormula getInstance() {
        return new MolecularFormula();
    }


    /**
     * @inheritDoc
     */
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
        this.stringFormula = in.readUTF();
        this.formula = MolecularFormulaManipulator.getMolecularFormula(stringFormula, builder);

    }


    /**
     * @inheritDoc
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(stringFormula);
    }


}
