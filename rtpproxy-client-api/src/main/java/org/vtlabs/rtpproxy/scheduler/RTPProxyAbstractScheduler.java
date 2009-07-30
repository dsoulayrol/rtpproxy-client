package org.vtlabs.rtpproxy.scheduler;

import java.util.List;
import org.vtlabs.rtpproxy.exception.NoServerAvailableException;
import org.vtlabs.rtpproxy.client.RTPProxyServer;

/**
 * Common code for {@link RTPProxyScheduler} implementations.
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public abstract class RTPProxyAbstractScheduler implements RTPProxyScheduler {
    protected List<RTPProxyServer> servers;

    public RTPProxyAbstractScheduler(List<RTPProxyServer> servers) {
        this.servers = servers;
    }

    public void addServer(RTPProxyServer server) {
        synchronized(this) {
            servers.add(server);
        }
    }

    public void removeServer(RTPProxyServer server) {
        synchronized(this) {
            servers.remove(server);
        }
    }

    public List<RTPProxyServer> getServerList() {
        return servers;
    }

    public RTPProxyServer getNextServer() throws NoServerAvailableException {
        if (!servers.isEmpty()) {
            // always return the first server of the list.
            return selectNextServer(servers);
        } else {
            throw new NoServerAvailableException("Server list is empty");
        }
    }

    /**
     * Implementation specific scheduling. This method is NEVER called when the
     * server list is empty.
     *
     * @param servers
     * @return next server to be used.
     */
    protected abstract RTPProxyServer selectNextServer(List<RTPProxyServer> servers);
}
