package org.vtlabs.rtpproxy.scheduler;

import java.util.List;
import org.vtlabs.rtpproxy.config.RTPProxyClientConfigException;
import org.vtlabs.rtpproxy.client.RTPProxyServer;

/**
 *
 * Very simple factory class to create {@link RTPProxyScheduler} instance based
 * on a scheduler name.
 *
 * @author Marcos Hack <marcosh@voicetechnology>
 */
public class RTPProxySchedulerFactory {
    public static final String STATIC_SCHEDULER = "static";
    public static final String ROUND_ROBIN_SCHEDULER = "rr";

    public static RTPProxyScheduler createScheduler(String schedulerName, 
            List<RTPProxyServer> servers) throws RTPProxyClientConfigException {

        if (schedulerName.equals(STATIC_SCHEDULER)) {
            return new RTPProxyStaticScheduler(servers);

        } else if (schedulerName.equals(ROUND_ROBIN_SCHEDULER)) {
            return new RTPProxyRoundRobinScheduler(servers);
            
        } else {
            throw new RTPProxyClientConfigException("Invalid scheduler name: '"
                    + schedulerName + "\'");
        }
    }
}
