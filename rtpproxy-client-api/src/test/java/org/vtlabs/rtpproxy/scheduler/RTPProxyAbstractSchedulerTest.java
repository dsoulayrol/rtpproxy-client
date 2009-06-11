/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vtlabs.rtpproxy.scheduler;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.vtlabs.rtpproxy.client.NoServerAvailableException;
import static org.junit.Assert.*;
import org.vtlabs.rtpproxy.client.RTPProxyServer;

/**
 *
 * @author mhack
 */
public class RTPProxyAbstractSchedulerTest extends RTPProxySchedulerTest {

    public RTPProxyAbstractSchedulerTest() {
    }

    /**
     * Test of addServer method, of class RTPProxyAbstractScheduler.
     */
    @Test
    public void testAddServer() {
        List<RTPProxyServer> list = new ArrayList<RTPProxyServer>();
        RTPProxyScheduler instance = new RTPProxyAbstractSchedulerMOCK(list);
        RTPProxyServer server1 = new RTPProxyServer();
        RTPProxyServer server2 = new RTPProxyServer();
        instance.addServer(server1);
        instance.addServer(server2);
        assertEquals("Server wasn't added", 2, list.size());
    }

    /**
     * Test of removeServer method, of class RTPProxyAbstractScheduler.
     */
    @Test
    public void testRemoveServer() {
        List<RTPProxyServer> listCopy = new ArrayList<RTPProxyServer>(servers);
        RTPProxyScheduler instance = new RTPProxyAbstractSchedulerMOCK(servers);

        for (RTPProxyServer s : listCopy) {
            instance.removeServer(s);
        }

        assertEquals("Server list should be empty", 0, servers.size());
    }

    /**
     * Test of getServerList method, of class RTPProxyAbstractScheduler.
     */
    @Test
    public void testGetServerList() {
        RTPProxyScheduler instance = new RTPProxyAbstractSchedulerMOCK(servers);
        assertEquals("Invalid server list", servers, instance.getServerList());
    }

    /**
     * Test of getNextServer method, of class RTPProxyAbstractScheduler.
     */
    @Test
    public void testGetNextServer() throws Exception {
       // getNextServer() must throw a NoServerAvailableException when called
       // with an empty server list.
       List<RTPProxyServer> emptyList = new ArrayList<RTPProxyServer>();
       RTPProxyScheduler instance = new RTPProxyAbstractSchedulerMOCK(emptyList);
       try {
           instance.getNextServer();
           fail("Expected exception wasn't threw");
           
       } catch (NoServerAvailableException e) {
           // nice!
       }
    }


    protected class RTPProxyAbstractSchedulerMOCK
            extends RTPProxyAbstractScheduler {

        public RTPProxyAbstractSchedulerMOCK(List<RTPProxyServer> servers) {
            super(servers);
        }
        
        @Override
        protected RTPProxyServer selectNextServer(List<RTPProxyServer> servers) {
            return null;
        }
    }
}