package org.vtlabs.rtpproxy.scheduler;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.vtlabs.rtpproxy.client.RTPProxyServer;

/**
 * The {@link RTPProxyRoundRobinScheduler} tries to equally balance the requests
 * to all severs in the list.
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class RTPProxyRoundRobinScheduler extends RTPProxyAbstractScheduler {
    private AtomicInteger lastIndex;

    public RTPProxyRoundRobinScheduler(List<RTPProxyServer> servers) {
        super(servers);
        lastIndex = new AtomicInteger(0);
    }

    @Override
    protected RTPProxyServer selectNextServer(List<RTPProxyServer> servers) {
        if (lastIndex.intValue() >= servers.size()) {
            lastIndex.set(0);
        }
        return servers.get(lastIndex.getAndIncrement());
    }
}
