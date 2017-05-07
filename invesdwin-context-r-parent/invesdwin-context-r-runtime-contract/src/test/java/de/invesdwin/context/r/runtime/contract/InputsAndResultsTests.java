package de.invesdwin.context.r.runtime.contract;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;

import de.invesdwin.util.concurrent.Executors;
import de.invesdwin.util.concurrent.Futures;
import de.invesdwin.util.concurrent.WrappedExecutorService;

@NotThreadSafe
public class InputsAndResultsTests {

    private final IScriptTaskRunner runner;

    public InputsAndResultsTests(final IScriptTaskRunner runner) {
        this.runner = runner;
    }

    public void test() {
        new InputsAndResultsTestDouble(runner).testDouble();
        new InputsAndResultsTestString(runner).testString();
        new InputsAndResultsTestBoolean(runner).testBoolean();
    }

    public void testParallel() {
        final List<Runnable> tasks = new ArrayList<Runnable>();
        for (int i = 0; i < 100; i++) {
            tasks.add(new Runnable() {
                @Override
                public void run() {
                    test();
                }
            });
        }
        final WrappedExecutorService executor = Executors.newFixedThreadPool(
                InputsAndResultsTests.class.getSimpleName() + "_testParallel",
                Runtime.getRuntime().availableProcessors());
        try {
            Futures.submitAndWait(executor, tasks);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

}