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
package plugin.psi;

import com.intellij.ide.util.PackageUtil;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassOwner;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.refactoring.util.RefactoringMessageUtil;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import static com.intellij.ide.util.EditSourceUtil.getDescriptor;

/**
 */
public class PsiHelperImpl implements PsiHelper {
  @Nullable
  @Override
  public PsiFile getPsiFileFromEditor( Editor editor, Project project ) {
    return getPsiFile( editor, project );
  }

  @Nullable
  @Override
  public PsiClass getPsiClassFromEditor( Editor editor, Project project ) {
    PsiClass psiClass = null;
    PsiFile psiFile = getPsiFile( editor, project );
    if ( psiFile instanceof PsiClassOwner ) {
      PsiClass[] classes = ( ( PsiClassOwner ) psiFile ).getClasses();
      if ( classes.length == 1 ) {
        psiClass = classes[0];
      }
    }
    return psiClass;
  }

  @Nullable
  private static PsiFile getPsiFile( @Nonnull Editor editor, @Nonnull Project project ) {
    return PsiUtilBase.getPsiFileInEditor( editor, project );
  }

  @Override
  public PsiShortNamesCache getPsiShortNamesCache( Project project ) {
    return PsiShortNamesCache.getInstance( project );
  }

  @Nullable
  @Override
  public PsiDirectory getDirectoryFromModuleAndPackageName( Module module, String packageName ) {
    PsiDirectory baseDir = PackageUtil.findPossiblePackageDirectoryInModule( module, packageName );
    return PackageUtil.findOrCreateDirectoryForPackage( module, packageName, baseDir, true );
  }

  @Override
  public void navigateToClass( @Nonnull PsiClass psiClass ) {
    Navigatable navigatable = getDescriptor( psiClass );
    if ( navigatable != null ) {
      navigatable.navigate( true );
    }
  }

  @Override
  @Nullable
  public String checkIfClassCanBeCreated( PsiDirectory targetDirectory, String className ) {
    return RefactoringMessageUtil.checkCanCreateClass( targetDirectory, className );
  }

  @Override
  public JavaDirectoryService getJavaDirectoryService() {
    return JavaDirectoryService.getInstance();
  }

  @Nullable
  @Override
  public PsiPackage getPackage( PsiDirectory psiDirectory ) {
    return getJavaDirectoryService().getPackage( psiDirectory );
  }

  @Override
  public JavaPsiFacade getJavaPsiFacade( Project project ) {
    return JavaPsiFacade.getInstance( project );
  }

  @Override
  public CommandProcessor getCommandProcessor() {
    return CommandProcessor.getInstance();
  }

  @Override
  public Application getApplication() {
    return ApplicationManager.getApplication();
  }

  @Nullable
  @Override
  public Module findModuleForPsiClass( @Nonnull PsiClass psiClass, @Nonnull Project project ) {
    @javax.annotation.Nullable VirtualFile virtualFile = psiClass.getContainingFile().getVirtualFile();
    if ( virtualFile == null ) {
      throw new IllegalStateException( "No virtual file found for " + psiClass );
    }

    return ModuleUtilCore.findModuleForFile( virtualFile, project );
  }
}
