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
package com.cedarsoft.serialization.generator.intellij.model;

import javax.annotation.Nonnull;

/**
 * Describes a setter for a field
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public interface FieldSetter {
  /**
   * Whether it is constructed by access
   *
   * @return the constructor access
   */
  boolean isConstructorAccess();

  /**
   * Whether it is accessed by setter
   *
   * @return the setter
   */
  boolean isSetterAccess();

  /**
   * Implementation that uses a constructor
   *
   * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
   */
  class ConstructorFieldSetter implements FieldSetter {
    private final int parameterIndex;

    public ConstructorFieldSetter( int parameterIndex ) {
      this.parameterIndex = parameterIndex;
    }

    public int getParameterIndex() {
      return parameterIndex;
    }

    @Override
    public boolean isConstructorAccess() {
      return true;
    }

    @Override
    public boolean isSetterAccess() {
      return false;
    }
  }

  /**
   * Implementation that uses a setter
   *
   * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
   */
  class SetterFieldSetter implements FieldSetter {
    @Nonnull
    private final String setter;

    public SetterFieldSetter( @Nonnull String setter ) {
      this.setter = setter;
    }

    @Nonnull
    public String getSetter() {
      return setter;
    }

    @Override
    public boolean isConstructorAccess() {
      return false;
    }

    @Override
    public boolean isSetterAccess() {
      return true;
    }
  }
}
