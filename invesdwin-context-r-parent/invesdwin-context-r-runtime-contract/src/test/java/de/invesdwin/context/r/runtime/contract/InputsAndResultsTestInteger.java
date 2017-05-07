package de.invesdwin.context.r.runtime.contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.io.ClassPathResource;

import de.invesdwin.util.assertions.Assertions;

@NotThreadSafe
public class InputsAndResultsTestInteger {

    private final IScriptTaskRunner runner;

    public InputsAndResultsTestInteger(final IScriptTaskRunner runner) {
        this.runner = runner;
    }

    public void testInteger() {
        //putInteger
        final int putInteger = 123;

        //putIntegerVector
        final int[] putIntegerVector = new int[3];
        for (int i = 0; i < putIntegerVector.length; i++) {
            putIntegerVector[i] = Integer.parseInt((i + 1) + "" + (i + 1));
        }

        //putIntegerVectorAsList
        final List<Integer> putIntegerVectorAsList = Arrays.asList(ArrayUtils.toObject(putIntegerVector));

        //putIntegerMatrix
        final int[][] putIntegerMatrix = new int[4][];
        for (int row = 0; row < putIntegerMatrix.length; row++) {
            final int[] vector = new int[3];
            for (int col = 0; col < vector.length; col++) {
                vector[col] = Integer.parseInt((row + 1) + "" + (col + 1));
            }
            putIntegerMatrix[row] = vector;
        }

        //putIntegerMatrixAsList
        final List<List<Integer>> putIntegerMatrixAsList = new ArrayList<List<Integer>>(putIntegerMatrix.length);
        for (final int[] vector : putIntegerMatrix) {
            putIntegerMatrixAsList.add(Arrays.asList(ArrayUtils.toObject(vector)));
        }

        final AScriptTask task = new AScriptTask(
                new ClassPathResource("InputsAndResultsTestInteger.R", InputsAndResultsTestInteger.class)) {
            @Override
            public void populateInputs(final IScriptTaskInputs inputs) {
                inputs.putInteger("putInteger", putInteger);

                inputs.putIntegerVector("putIntegerVector", putIntegerVector);

                inputs.putIntegerVectorAsList("putIntegerVectorAsList", putIntegerVectorAsList);

                inputs.putIntegerMatrix("putIntegerMatrix", putIntegerMatrix);

                inputs.putIntegerMatrixAsList("putIntegerMatrixAsList", putIntegerMatrixAsList);
            }
        };
        try (IScriptTaskResults results = runner.run(task)) {
            //getInteger
            final Integer getInteger = results.getInteger("getInteger");
            Assertions.assertThat(putInteger).isEqualTo(getInteger);

            //getIntegerVector
            final int[] getIntegerVector = results.getIntegerVector("getIntegerVector");
            Assertions.assertThat(putIntegerVector).isEqualTo(getIntegerVector);

            //getIntegerVectorAsList
            final List<Integer> getIntegerVectorAsList = results.getIntegerVectorAsList("getIntegerVectorAsList");
            Assertions.assertThat(putIntegerVectorAsList).isEqualTo(getIntegerVectorAsList);

            //getIntegerMatrix
            final int[][] getIntegerMatrix = results.getIntegerMatrix("getIntegerMatrix");
            Assertions.assertThat(putIntegerMatrix).isEqualTo(getIntegerMatrix);

            //getIntegerMatrixAsList
            final List<List<Integer>> getIntegerMatrixAsList = results.getIntegerMatrixAsList("getIntegerMatrixAsList");
            Assertions.assertThat(putIntegerMatrixAsList).isEqualTo(getIntegerMatrixAsList);
        }
    }

}
