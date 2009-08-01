package com.cedarsoft.utils.io;

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

  @NotNull
  public static String getOsName() {
    return OS_NAME;
  }

  @NotNull
  public static String getOsArch() {
    return OS_ARCH;
  }

  @NotNull
  public static String getOsVersion() {
    return OS_VERSION;
  }

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

  public static boolean isFamily( @NotNull @NonNls String family ) {
    if ( family.equals( "windows" ) ) {
      return OS_NAME.indexOf( "windows" ) > -1;
    } else if ( family.equals( "os/2" ) ) {
      return OS_NAME.indexOf( "os/2" ) > -1;
    } else if ( family.equals( "netware" ) ) {
      return OS_NAME.indexOf( "netware" ) > -1;
    } else if ( family.equals( "dos" ) ) {
      return PATH_SEP.equals( ";" ) && !isFamily( "netware" );
    } else if ( family.equals( "mac" ) ) {
      return OS_NAME.indexOf( "mac" ) > -1;
    } else if ( family.equals( "tandem" ) ) {
      return OS_NAME.indexOf( "nonstop_kernel" ) > -1;
    } else if ( family.equals( "unix" ) ) {
      return PATH_SEP.equals( ":" ) && !isFamily( "openvms" ) && ( !isFamily( "mac" ) || OS_NAME.endsWith( "x" ) );
    } else if ( family.equals( "win9x" ) ) {
      return isFamily( "windows" )
        && ( OS_NAME.indexOf( "95" ) >= 0
        || OS_NAME.indexOf( "98" ) >= 0
        || OS_NAME.indexOf( "me" ) >= 0
        || OS_NAME.indexOf( "ce" ) >= 0 );
    } else if ( family.equals( "z/os" ) ) {
      return OS_NAME.indexOf( "z/os" ) > -1
        || OS_NAME.indexOf( "os/390" ) > -1;
    } else if ( family.equals( "os/400" ) ) {
      return OS_NAME.indexOf( "os/400" ) > -1;
    } else if ( family.equals( "openvms" ) ) {
      return OS_NAME.indexOf( "openvms" ) > -1;
    }

    throw new IllegalStateException( "Don\'t know how to detect os family \"" + family + "\"" );
  }
}
