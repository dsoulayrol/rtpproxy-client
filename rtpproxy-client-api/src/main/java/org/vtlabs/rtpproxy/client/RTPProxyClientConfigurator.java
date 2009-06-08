/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.client;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author mhack
 */
public class RTPProxyClientConfigurator {

    public static final String CONFIG_PROPERTY = "org.vtlabs.rtpproxy.config";
    public static final String BIND_PORT_PROPERTY = "org.vtlabs.rtpproxy.bindport";
    public static final String SERVER_LIST_PROPERTY = "org.vtlabs.rtpproxy.servers";
    public static final String POOL_SIZE_PROPERTY = "org.vtlabs.rtpproxy.poolsize";

    private RTPProxyClientConfigurator() {}
    
    public static RTPProxyClientConfig load(File configFile)
            throws RTPProxyClientConfigException {
        try {
            FileReader reader = new FileReader(configFile);
            Properties configProperties = new Properties();
            configProperties.load(reader);

            return load(configProperties);

        } catch (IOException ioEx) {
            StringBuilder sb = new StringBuilder("Unable to read config file ");
            sb.append(configFile.getAbsolutePath());
            throw new RTPProxyClientConfigException(sb.toString(), ioEx);

        }
    }

    /**
     * Create RTPProxyClient configuration from a comma separated list of
     * RTPProxy server addresses in the format SERVER_IP:PORT. Assume default
     * values for the other properties.
     *
     * @return Client config.
     */
    public static RTPProxyClientConfig load(String serverList)
            throws RTPProxyClientConfigException {
        Properties configProps = new Properties();
        configProps.setProperty(SERVER_LIST_PROPERTY, serverList);
        return load(configProps);
    }

    /**
     * Create RTPProxyClient configuration from the given properties. Assume
     * default values for BIND_PORT and POOL_SIZE. SERVER_LIST is obrigatory.
     * @param Properties containing at leat SERVER_LIST property set.
     * @return RTPProxyClient configuration.
     * @throws org.vtlabs.rtpproxy.client.RTPProxyClientConfigException
     */
    public static RTPProxyClientConfig load(Properties propList)
            throws RTPProxyClientConfigException {
        RTPProxyClientConfig config = new RTPProxyClientConfig();
        // Bind port
        String strBindPort = propList.getProperty(BIND_PORT_PROPERTY);
        if (strBindPort != null) {
            int bindPort = Integer.parseInt(strBindPort);
            config.setBindPort(bindPort);
        }

        // Scheduled Thread Pool Size
        String strPoolSize = propList.getProperty(POOL_SIZE_PROPERTY);
        if (strPoolSize != null) {
            int poolSize = Integer.parseInt(strPoolSize);
            config.setPoolSize(poolSize);
        }

        // Server List
        String strServers = propList.getProperty(SERVER_LIST_PROPERTY);
        if (strServers == null) {
            StringBuilder sb = new StringBuilder("Server list is empty.");
            sb.append(" Set property ").append(SERVER_LIST_PROPERTY);
            sb.append(" as a comma separated list of RTPProxy servers");
            sb.append(" IP addresses");
            throw new RTPProxyClientConfigException(sb.toString());

        } else {
            String[] arrServers = StringUtils.split(strServers, ",");
            for (String strServerAddr : arrServers) {
                // Split HOSTNAME:PORT
                String[] arrAddr = StringUtils.split(strServerAddr, ":", 2);
                String host = arrAddr[0];
                int port = Integer.parseInt(arrAddr[1]);
                InetSocketAddress address = new InetSocketAddress(host, port);
                RTPProxyServer server = new RTPProxyServer();
                server.setAddress(address);
                config.addServer(server);
            }
        }

        return config;
    }
}
