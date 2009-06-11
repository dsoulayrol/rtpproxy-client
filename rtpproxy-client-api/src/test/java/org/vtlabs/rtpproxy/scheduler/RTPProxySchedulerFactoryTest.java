package org.vtlabs.rtpproxy.scheduler;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.vtlabs.rtpproxy.client.RTPProxyServer;
import static org.junit.Assert.*;

/**
 *
 * @author mhack
 */
public class RTPProxySchedulerFactoryTest {
    private List<RTPProxyServer> servers;

    @Before
    public void setUp() {
        servers = new ArrayList<RTPProxyServer>();
    }

    @Test
    public void testCreateStaticScheduler() throws Exception {
        RTPProxyScheduler scheduler = RTPProxySchedulerFactory.createScheduler("static", servers);
        assertTrue(scheduler instanceof RTPProxyScheduler);
    }

    @Test
    public void testCreateRRScheduler() throws Exception {
        RTPProxyScheduler scheduler = RTPProxySchedulerFactory.createScheduler("rr", servers);
        assertTrue(scheduler instanceof RTPProxyRoundRobinScheduler);
    }
}
