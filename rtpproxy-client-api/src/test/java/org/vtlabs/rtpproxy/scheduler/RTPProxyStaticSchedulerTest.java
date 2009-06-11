package org.vtlabs.rtpproxy.scheduler;

import org.junit.Test;
import static org.junit.Assert.*;
import org.vtlabs.rtpproxy.client.RTPProxyServer;

/**
 *
 * @author mhack
 */
public class RTPProxyStaticSchedulerTest extends RTPProxySchedulerTest {

    /**
     * Test of selectNextServer method, of class RTPProxyStaticScheduler.
     */
    @Test
    public void testSelectNextServer() throws Exception {
        RTPProxyScheduler instance = new RTPProxyStaticScheduler(servers);

        int selectCounter = 5;
        for (int i = 0; i < selectCounter; i++) {
            RTPProxyServer result = instance.getNextServer();
            ((RTPProxyServerMOCK)result).usageCounter++;
        }

        RTPProxyServerMOCK firstServer = (RTPProxyServerMOCK) servers.get(0);
        assertEquals("First server wasn't used everytime selectNextServer() was"
                + "called", selectCounter, firstServer.usageCounter);
    }
}