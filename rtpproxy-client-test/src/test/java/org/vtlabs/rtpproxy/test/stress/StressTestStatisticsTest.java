package org.vtlabs.rtpproxy.test.stress;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marcoshack
 */
public class StressTestStatisticsTest {
    private StressTestStatistics stats;

    @Before
    public void setUp() {
        stats = new StressTestStatistics();
    }

    @Test
    public void testCreateAccessors() {
        stats.incCreateRequest();
        stats.incCreateSucess();
        stats.incCreateFail();
        stats.incCreateTimeout();
        assertEquals(1, stats.getCreateRequestCounter());
        assertEquals(1, stats.getCreateSucessCounter());
        assertEquals(1, stats.getCreateFailCounter());
        assertEquals(1, stats.getCreateTimeoutCounter());
    }

    @Test
    public void testUpdateAccessors() {
        stats.incUpdateRequest();
        stats.incUpdateSucess();
        stats.incUpdateFail();
        stats.incUpdateTimeout();
        assertEquals(1, stats.getUpdateRequestCounter());
        assertEquals(1, stats.getUpdateSucessCounter());
        assertEquals(1, stats.getUpdateFailCounter());
        assertEquals(1, stats.getUpdateTimeoutCounter());
    }

    @Test
    public void testDestroyAccessors() {
        stats.incDestroyRequest();
        stats.incDestroySucess();
        stats.incDestroyFail();
        stats.incDestroyTimeout();
        assertEquals(1, stats.getDestroyRequestCounter());
        assertEquals(1, stats.getDestroySucessCounter());
        assertEquals(1, stats.getDestroyFailCounter());
        assertEquals(1, stats.getDestroyTimeoutCounter());
    }

    @Test
    public void testRate() {
        for (int i = 0; i < 10; i++) {
            stats.incCreateRequest();

            if (i % 2 == 0) {
                stats.incCreateSucess();
            } else {
                stats.incCreateFail();
                stats.incCreateTimeout();
            }
        }

        assertEquals(0.5, stats.getCreateSucessRate(), 0.001);
        assertEquals(0.5, stats.getCreateFailRate(), 0.001);
        assertEquals(0.5, stats.getCreateTimeoutRate(), 0.001);
    }
}
