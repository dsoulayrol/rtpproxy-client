/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vtlabs.rtpproxy.test;


/**
 *
 * @author marcoshack
 */
public class BaseTest {
    private static final String LOG_CONFIG_FILE = "conf/log4j.properties";

    static {
        System.setProperty("log4j.configuration", "file:" + LOG_CONFIG_FILE);
    }
}
