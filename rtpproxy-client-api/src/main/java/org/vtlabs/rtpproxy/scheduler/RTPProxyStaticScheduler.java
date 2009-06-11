package org.vtlabs.rtpproxy.scheduler;

import java.util.List;
import org.vtlabs.rtpproxy.client.RTPProxyServer;

/**
 * The {@link RTPProxyStaticScheduler} always return the first server of the
 * server list gave to its constructor.
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class RTPProxyStaticScheduler extends RTPProxyAbstractScheduler {

    public RTPProxyStaticScheduler(List<RTPProxyServer> servers) {
        super(servers);
    }

    @Override
    protected RTPProxyServer selectNextServer(List<RTPProxyServer> servers) {
        // Allways return the first server of the list.
        return servers.get(0);
    }
}
