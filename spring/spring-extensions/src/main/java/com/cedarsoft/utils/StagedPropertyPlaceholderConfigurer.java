/**
 * Copyright (C) 2010 cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce.txt
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

package com.cedarsoft.utils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Map;
import java.util.Properties;

/**
 * Staged property placeholder configurer.
 * The system property {@link #STAGE_KEY} is used as prefix for the given properties file.
 * If the property is not set, the default value "dev" is used.
 */
public class StagedPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
  @NotNull
  @NonNls
  public static final String STAGE_KEY = "stage";

  @NonNls
  private String actualStage;

  @NotNull
  @NonNls
  public String getActualStage() {
    if ( actualStage == null ) {
      actualStage = System.getProperty( STAGE_KEY );
      if ( actualStage == null || actualStage.length() == 0 || actualStage.equals( "null" ) ) {
        actualStage = "dev"; //Setting the default value (for unit testing etc).
      }
    }
    return actualStage;
  }

  public void setActualStage( @NotNull @NonNls String actualStage ) {
    this.actualStage = actualStage;
  }

  @Override
  protected void processProperties( @NotNull ConfigurableListableBeanFactory beanFactoryToProcess, @NotNull Properties props ) throws BeansException {
    super.processProperties( beanFactoryToProcess, filter( props ) );
  }

  @NotNull
  private Properties filter( @NotNull Properties props ) {
    String stage = getActualStage();

    Properties result = new Properties();
    for ( Map.Entry<Object, Object> entry : props.entrySet() ) {
      String key = String.valueOf( entry.getKey() );
      if ( key.startsWith( stage ) ) {
        String newKey = key.substring( stage.length() + 1 );
        result.setProperty( newKey, ( String ) entry.getValue() );
      }
    }
    return result;
  }
}
