package de.invesdwin.context.r.runtime.jri;

import javax.annotation.concurrent.NotThreadSafe;

import org.rosuda.JRI.Rengine;

import de.invesdwin.context.r.runtime.contract.IScriptTaskInputs;
import de.invesdwin.util.assertions.Assertions;

@NotThreadSafe
public class JriScriptTaskInputs implements IScriptTaskInputs {

    private final Rengine rengine;

    public JriScriptTaskInputs(final Rengine rengine) {
        this.rengine = rengine;
    }

    @Override
    public Rengine getEngine() {
        return rengine;
    }

    @Override
    public void putString(final String variable, final String value) {
        Assertions.checkTrue(rengine.assign(variable, value));
    }

    @Override
    public void putStringVector(final String variable, final String[] value) {
        Assertions.checkTrue(rengine.assign(variable, value));
    }

    @Override
    public void putDouble(final String variable, final double value) {
        Assertions.checkTrue(rengine.assign(variable, new double[] { value }));
    }

    @Override
    public void putDoubleVector(final String variable, final double[] value) {
        Assertions.checkTrue(rengine.assign(variable, value));
    }

    /**
     * http://permalink.gmane.org/gmane.comp.lang.r.rosuda.devel/87
     */
    @Override
    public void putDoubleMatrix(final String variable, final double[][] value) {
        final int rows = value.length;
        final int cols = value[0].length;
        final double[] flatMatrix = new double[rows * cols];
        int i = 0;
        for (int row = 0; row < rows; row++) {
            Assertions.checkEquals(value[row].length, cols);
            for (int col = 0; col < cols; col++) {
                flatMatrix[i] = value[row][col];
                i++;
            }
        }
        rengine.assign(variable, flatMatrix);
        putExpression(variable, "matrix(" + variable + ", " + rows + ", " + cols + ", TRUE)");
    }

    @Override
    public void putBoolean(final String variable, final boolean value) {
        Assertions.checkTrue(rengine.assign(variable, new boolean[] { value }));
    }

    @Override
    public void putBooleanVector(final String variable, final boolean[] value) {
        Assertions.checkTrue(rengine.assign(variable, value));
    }

    @Override
    public void putBooleanMatrix(final String variable, final boolean[][] value) {
        final int rows = value.length;
        final int cols = value[0].length;
        final int[] flatMatrix = new int[rows * cols];
        int i = 0;
        for (int row = 0; row < rows; row++) {
            Assertions.checkEquals(value[row].length, cols);
            for (int col = 0; col < cols; col++) {
                if (value[row][col]) {
                    flatMatrix[i] = 1;
                } else {
                    flatMatrix[i] = 0;
                }
                i++;
            }
        }
        rengine.assign(variable, flatMatrix);
        putExpression(variable, "matrix(" + variable + ", " + rows + ", " + cols + ", TRUE)");
    }

    @Override
    public void putExpression(final String variable, final String expression) {
        JriScriptTaskRunner.eval(rengine, variable + " <- " + expression);
    }

}
