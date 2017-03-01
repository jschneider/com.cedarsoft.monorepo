/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft.configuration;

import com.cedarsoft.configuration.xml.XmlConfigurationManager;
import org.apache.commons.configuration.Configuration;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.beans.factory.FactoryBean;

import java.lang.Class;

/**
 * <p>
 * Date: Jul 1, 2007<br>
 * Time: 6:39:21 PM<br>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
@Deprecated
public class ConfigurationFactory implements FactoryBean {
  @Nonnull
  private final XmlConfigurationManager configurationManager;
  @Nullable
  private final Class<?> moduleType;

  /**
   * <p>Constructor for ConfigurationFactory.</p>
   *
   * @param configurationManager a XmlConfigurationManager object.
   */
  public ConfigurationFactory( @Nonnull XmlConfigurationManager configurationManager ) {
    this.configurationManager = configurationManager;
    moduleType = null;
  }

  /**
   * <p>Constructor for ConfigurationFactory.</p>
   *
   * @param configurationManager a XmlConfigurationManager object.
   * @param moduleType           a Class object.
   */
  public ConfigurationFactory( @Nonnull XmlConfigurationManager configurationManager, @Nullable Class<?> moduleType ) {
    this.configurationManager = configurationManager;
    this.moduleType = moduleType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public Object getObject() throws Exception {
    if ( moduleType != null ) {
      return configurationManager.getModuleConfiguration( moduleType );
    } else {
      return configurationManager.getConfiguration();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public Class<Configuration> getObjectType() {
    return Configuration.class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSingleton() {
    return true;
  }
}
