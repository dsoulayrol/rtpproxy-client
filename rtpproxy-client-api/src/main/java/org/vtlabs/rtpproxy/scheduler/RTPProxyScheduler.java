package org.vtlabs.rtpproxy.scheduler;

import java.util.List;
import org.vtlabs.rtpproxy.client.NoServerAvailableException;
import org.vtlabs.rtpproxy.client.RTPProxyServer;

/**
 * A {@link RTPProxyScheduler} is responsible to select the next server to be
 * used. A concrete instance must be created using the
 * {@link RTPProxySchedulerFactory} factory class.
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public interface RTPProxyScheduler {
    public RTPProxyServer getNextServer() throws NoServerAvailableException;
    public void addServer(RTPProxyServer server);
    public void removeServer(RTPProxyServer server);
    public List<RTPProxyServer> getServerList();
}
