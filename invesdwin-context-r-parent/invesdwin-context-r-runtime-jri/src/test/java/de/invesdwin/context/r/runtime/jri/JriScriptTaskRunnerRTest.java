package de.invesdwin.context.r.runtime.jri;

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;

import org.junit.Test;

import de.invesdwin.context.r.runtime.contract.InputsAndResultsTests;
import de.invesdwin.context.test.ATest;

@NotThreadSafe
public class JriScriptTaskRunnerRTest extends ATest {

    @Inject
    private JriScriptTaskRunnerR runner;

    @Test
    public void test() {
        new InputsAndResultsTests(runner).test();
    }

    @Test
    public void testParallel() {
        new InputsAndResultsTests(runner).testParallel();
    }

}
