/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.jndi.rmi;

import java.util.Hashtable;

import javax.naming.spi.ObjectFactory;

import org.apache.aries.util.AriesFrameworkUtil;
import org.apache.aries.util.log.Logger;
import org.apache.aries.util.nls.MessageUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.jndi.JNDIConstants;
import org.osgi.service.log.LogService;

public class Activator implements BundleActivator {
    private Logger logger;
    private ServiceRegistration reg;

    public void start(BundleContext context) {
        logger = new Logger(context);
        logger.open();

        try {
            Hashtable<Object, Object> props = new Hashtable<Object, Object>();
            props.put(JNDIConstants.JNDI_URLSCHEME, new String[] { "rmi" });
            reg = context.registerService(
                        ObjectFactory.class.getName(),
                        ClassLoader.getSystemClassLoader().loadClass("com.sun.jndi.url.rmi.rmiURLContextFactory").newInstance(),
                        props);

            logger.log(LogService.LOG_DEBUG, "Registered RMI url handler");
        }
        catch (Exception e)
        {
            MessageUtil msg = MessageUtil.createMessageUtil(Activator.class, "org.apache.aries.jndi.nls.jndiRmiMessages");
            logger.log(LogService.LOG_INFO, msg.getMessage("rmi.factory.creation.failed"), e);
        }
    }

    public void stop(BundleContext context) {
        AriesFrameworkUtil.safeUnregisterService(reg);

        if (logger != null)
            logger.close();
    }
}
