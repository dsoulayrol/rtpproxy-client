/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.client;

import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author mhack
 */
public class RTPProxyClientConfigurator {

    private static final String CONFIG_PROPERTY = "org.vtlabs.rtpproxy.config";
    private static final String BIND_PORT_PROPERTY = "org.vtlabs.rtpproxy.bindport";
    private static final String SERVER_LIST_PROPERTY = "org.vtlabs.rtpproxy.servers";
    private static final String POOL_SIZE_PROPERTY = "org.vtlabs.rtpproxy.poolsize";

    /**
     * Default bind port used by UDP service if it isn't specified.
     */
    public static final int DEFAULT_BIND_PORT = 9876;

    /**
     * Default scheduled thread pool executor pool size, used by the command
     * timeout service.
     */
    public static final int DEFAULT_SCHEDULED_POOL_SIZE = 3;

    private int bindPort;
    private int poolSize;
    private List<RTPProxyServer> serverList;
    
    public RTPProxyClientConfigurator() throws ConfigErrorException {
        String configFileName = System.getProperty(CONFIG_PROPERTY);

        if (configFileName == null) {
            throw new ConfigErrorException("Configuration file name not found. " 
                    + "Set " + CONFIG_PROPERTY + " property with the "
                    + "configuration file path");
        }

        try {
            FileReader reader = new FileReader(configFileName);
            Properties configProperties = new Properties();
            configProperties.load(reader);

            loadConfig(configProperties);

        } catch (IOException ioEx) {
            throw new ConfigErrorException("Unable to read config file " 
                    + configFileName, ioEx);

        } catch (Exception e) {
            throw new ConfigErrorException("Error reading config file "
                    + configFileName, e);
        }
    }

    private void loadConfig(Properties prop) throws ConfigErrorException {
        // Bind port
        String strBindPort = prop.getProperty(BIND_PORT_PROPERTY);
        if (strBindPort == null) {
            bindPort = DEFAULT_BIND_PORT;
        } else {
            bindPort = Integer.parseInt(strBindPort);
        }

        // Scheduled Thread Pool Size
        String strPoolSize = prop.getProperty(POOL_SIZE_PROPERTY);
        if (strPoolSize == null) {
            poolSize = DEFAULT_SCHEDULED_POOL_SIZE;
        } else {
            poolSize = Integer.parseInt(strPoolSize);
        }

        // Server List
        String strServers = prop.getProperty(SERVER_LIST_PROPERTY);
        if (strServers == null) {
            throw new ConfigErrorException("Server list is empty. Set property "
                    + SERVER_LIST_PROPERTY + " as a comma sparated list of "
                    + "RTPProxy servers");
        } else {
            String[] arrServers = StringUtils.split(strServers, ",");
            serverList = new ArrayList<RTPProxyServer>();
            for (String strServerAddr : arrServers) {
                // Split HOSTNAME:PORT
                String[] arrServerAddr = StringUtils.split(strServerAddr,":",2);
                String host = arrServerAddr[0];
                int port = Integer.parseInt(arrServerAddr[1]);
                InetSocketAddress address = new InetSocketAddress(host, port);
                RTPProxyServer server = new RTPProxyServer();
                server.setAddress(address);
                serverList.add(server);
            }
        }
    }

    public int getBindPort() {
        return bindPort;
    }

    public int getScheduledThreadPoolSize() {
        return poolSize;
    }

    public List<RTPProxyServer> getServerList() {
        return serverList;
    }
}
