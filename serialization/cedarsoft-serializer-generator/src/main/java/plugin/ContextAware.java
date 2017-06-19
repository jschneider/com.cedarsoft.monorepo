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
package plugin;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.search.GlobalSearchScope;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ContextAware {
  @Nonnull
  protected final DataContext dataContext;

  public ContextAware( @Nonnull DataContext dataContext ) {
    this.dataContext = dataContext;
  }

  @Nonnull
  public Project getProject() {
    Project project = CommonDataKeys.PROJECT.getData( dataContext );
    if ( project == null ) {
      throw new IllegalStateException( "no project found" );
    }

    return project;
  }

  @Nonnull
  public JavaPsiFacade getPsiFacade() {
    return JavaPsiFacade.getInstance( getProject() );
  }

  @Nonnull
  public GlobalSearchScope getGlobalSearchScope() {
    return GlobalSearchScope.allScope( getProject() );
  }

  @Nonnull
  public PsiElementFactory getPsiElementFactory() {
    return getPsiFacade().getElementFactory();
  }

  @Nonnull
  public DataContext getDataContext() {
    return dataContext;
  }

  @Nonnull
  public Application getApplication() {
    return ApplicationManager.getApplication();
  }
}
