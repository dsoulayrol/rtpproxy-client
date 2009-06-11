/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vtlabs.rtpproxy.scheduler;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.vtlabs.rtpproxy.client.RTPProxyServer;

/**
 *
 * @author mhack
 */
@Ignore
public class RTPProxySchedulerTest {
    List<RTPProxyServer> servers;

    @Before
    public void setUp() {
        servers = new ArrayList<RTPProxyServer>();
        servers.add(new RTPProxyServerMOCK(new InetSocketAddress("127.0.0.1", 9000)));
        servers.add(new RTPProxyServerMOCK(new InetSocketAddress("127.0.0.1", 9001)));
        servers.add(new RTPProxyServerMOCK(new InetSocketAddress("127.0.0.1", 9002)));
        servers.add(new RTPProxyServerMOCK(new InetSocketAddress("127.0.0.1", 9003)));
        servers.add(new RTPProxyServerMOCK(new InetSocketAddress("127.0.0.1", 9004)));
    }

    protected class RTPProxyServerMOCK extends RTPProxyServer {
        public int usageCounter;

        public RTPProxyServerMOCK(InetSocketAddress serverAddress) {
            super(serverAddress);
            usageCounter = 0;
        }
    }
}
