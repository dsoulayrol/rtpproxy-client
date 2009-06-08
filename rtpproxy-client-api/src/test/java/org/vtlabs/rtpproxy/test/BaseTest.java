/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vtlabs.rtpproxy.test;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mhack
 */
@Ignore
public class BaseTest {
    protected static Logger log;

    @BeforeClass
    public static void classInit() {
        BasicConfigurator.configure();
        log = LoggerFactory.getLogger(BaseTest.class);
    }
}
