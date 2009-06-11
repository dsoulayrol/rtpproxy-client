/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vtlabs.rtpproxy.scheduler;

import org.junit.Test;
import static org.junit.Assert.*;
import org.vtlabs.rtpproxy.client.RTPProxyServer;

/**
 *
 * @author mhack
 */
public class RTPProxyRoundRobinSchedulerTest extends RTPProxySchedulerTest {

    /**
     * Test of selectNextServer method, of class RTPProxyRoundRobinScheduler.
     */
    @Test
    public void testSelectNextServer() throws Exception{
        RTPProxyScheduler instance = new RTPProxyRoundRobinScheduler(servers);
        
        int perServerExpectedUse = 3;

        int selectCounter = servers.size() * perServerExpectedUse;
        for (int i = 0; i < selectCounter; i++) {
            RTPProxyServer result = instance.getNextServer();
            ((RTPProxyServerMOCK)result).usageCounter++;
        }

        for (RTPProxyServer s : servers) {
            assertEquals("Server " + s.getAddress() + " wasn't used twice",
                    perServerExpectedUse, ((RTPProxyServerMOCK)s).usageCounter);
        }
    }
}