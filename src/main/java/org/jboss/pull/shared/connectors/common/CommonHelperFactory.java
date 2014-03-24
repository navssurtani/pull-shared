/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.pull.shared.connectors.common;

/**
 * A Factory class for the {@link org.jboss.pull.shared.connectors.common.CommonHelper}
 *
 * @author navssurtani
 */
public class CommonHelperFactory {

    private static CommonHelper helper = null;

    /**
     * Returns the {@link org.jboss.pull.shared.connectors.common.CommonHelper} stored within this class.
     *
     * @return the helper.
     */
    public static CommonHelper getHelperInstance(String configurationFileProperty,
                                                 String configurationFileDefault) throws Exception {
        helper = new CommonHelper(configurationFileProperty, configurationFileDefault);
        return helper;
    }
}
