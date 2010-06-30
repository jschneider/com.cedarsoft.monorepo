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
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */

package com.cedarsoft.io;

/*
 * Copyright  2001-2005 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */


import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.String;
import java.util.Locale;

public class Os {
  @NotNull
  @NonNls
  private static final String OS_NAME = System.getProperty( "os.name" ).toLowerCase( Locale.US );
  @NotNull
  @NonNls
  private static final String OS_ARCH = System.getProperty( "os.arch" ).toLowerCase( Locale.US );
  @NotNull
  @NonNls
  private static final String OS_VERSION = System.getProperty( "os.version" ).toLowerCase( Locale.US );
  @NotNull
  @NonNls
  private static final String PATH_SEP = System.getProperty( "path.separator" );

  private Os() {
  }

  /**
   * <p>getOsName</p>
   *
   * @return a {@link String} object.
   */
  @NotNull
  public static String getOsName() {
    return OS_NAME;
  }

  /**
   * <p>getOsArch</p>
   *
   * @return a {@link String} object.
   */
  @NotNull
  public static String getOsArch() {
    return OS_ARCH;
  }

  /**
   * <p>getOsVersion</p>
   *
   * @return a {@link String} object.
   */
  @NotNull
  public static String getOsVersion() {
    return OS_VERSION;
  }

  /**
   * <p>getPathSep</p>
   *
   * @return a {@link String} object.
   */
  @NotNull
  public static String getPathSep() {
    return PATH_SEP;
  }

  /**
   * Determines if the OS on which Ant is executing matches the
   * given OS family, name, architecture and version
   *
   * @param family  The OS family
   * @param name    The OS name
   * @param arch    The OS architecture
   * @param version The OS version
   * @return true if the OS matches
   *
   * @since 1.7
   */
  public static boolean isOs( @NotNull @NonNls String family, @NotNull @NonNls String name, @NotNull @NonNls String arch, @NotNull @NonNls String version ) {
    if ( !isFamily( family ) ) {
      return false;
    }
    if ( !isOs( name ) ) {
      return false;
    }
    if ( !isArch( arch ) ) {
      return false;
    }
    if ( !isOsVersion( version ) ) {
      return false;
    }

    return true;
  }

  private static boolean isOsVersion( @NotNull @NonNls String version ) {
    return version.equals( OS_VERSION );
  }

  private static boolean isArch( @NotNull @NonNls String arch ) {
    return arch.equals( OS_ARCH );
  }

  private static boolean isOs( @NotNull @NonNls String name ) {
    return name.equals( OS_NAME );
  }

  /**
   * <p>isFamily</p>
   *
   * @param family a {@link String} object.
   * @return a boolean.
   */
  public static boolean isFamily( @NotNull @NonNls String family ) {
    if ( family.equals( "windows" ) ) {
      return OS_NAME.contains( "windows" );
    } else if ( family.equals( "os/2" ) ) {
      return OS_NAME.contains( "os/2" );
    } else if ( family.equals( "netware" ) ) {
      return OS_NAME.contains( "netware" );
    } else if ( family.equals( "dos" ) ) {
      return PATH_SEP.equals( ";" ) && !isFamily( "netware" );
    } else if ( family.equals( "mac" ) ) {
      return OS_NAME.contains( "mac" );
    } else if ( family.equals( "tandem" ) ) {
      return OS_NAME.contains( "nonstop_kernel" );
    } else if ( family.equals( "unix" ) ) {
      return PATH_SEP.equals( ":" ) && !isFamily( "openvms" ) && ( !isFamily( "mac" ) || OS_NAME.endsWith( "x" ) );
    } else if ( family.equals( "win9x" ) ) {
      return isFamily( "windows" )
        && ( OS_NAME.contains( "95" )
        || OS_NAME.contains( "98" )
        || OS_NAME.contains( "me" )
        || OS_NAME.contains( "ce" ) );
    } else if ( family.equals( "z/os" ) ) {
      return OS_NAME.contains( "z/os" )
        || OS_NAME.contains( "os/390" );
    } else if ( family.equals( "os/400" ) ) {
      return OS_NAME.contains( "os/400" );
    } else if ( family.equals( "openvms" ) ) {
      return OS_NAME.contains( "openvms" );
    }

    throw new IllegalStateException( "Don\'t know how to detect os family \"" + family + "\"" );
  }
}
