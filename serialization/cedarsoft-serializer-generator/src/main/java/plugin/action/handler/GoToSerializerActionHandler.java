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
package plugin.action.handler;

import plugin.psi.PsiHelper;
import plugin.psi.PsiHelperImpl;
import plugin.verifier.PopupDisplayer;
import plugin.verifier.PopupListFactory;
import plugin.verifier.SerializerFinder;
import plugin.verifier.SerializerVerifier;
import plugin.verifier.SerializerVerifierImpl;
import com.intellij.ClassFinder;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.swing.JList;
import java.io.File;
import java.io.IOException;

/**
 */
public class GoToSerializerActionHandler extends EditorActionHandler {
  @Nonnull
  private final PsiHelper psiHelper;
  @Nonnull
  private final SerializerVerifier serializerVerifier;
  @Nonnull
  private final SerializerFinder serializerFinder;
  @Nonnull
  private final PopupDisplayer popupDisplayer;
  @Nonnull
  private final PopupListFactory popupListFactory;

  public GoToSerializerActionHandler() throws IOException {
    this( new PsiHelperImpl(), new SerializerVerifierImpl(), new SerializerFinder(new ClassFinder( new File( "." ), "com", true ) ), new PopupDisplayer() {
            @Override
            public void displayPopupChooser( Editor editor, JList list, Runnable runnable ) {
            }
          }, new PopupListFactory() {
            @Override
            public JList getPopupList() {
              throw new UnsupportedOperationException();
            }
          }
    );
  }

  @SuppressWarnings( "PMD.ExcessiveParameterList" )
  public GoToSerializerActionHandler( @NotNull PsiHelper psiHelper, @NotNull SerializerVerifier serializerVerifier, @NotNull SerializerFinder serializerFinder, @NotNull PopupDisplayer popupDisplayer, @NotNull PopupListFactory popupListFactory ) {
    this.psiHelper = psiHelper;
    this.serializerVerifier = serializerVerifier;
    this.serializerFinder = serializerFinder;
    this.popupDisplayer = popupDisplayer;
    this.popupListFactory = popupListFactory;
  }

  @Override
  public void execute( Editor editor, DataContext dataContext ) {
    Project project = ( Project ) dataContext.getData( DataKeys.PROJECT.getName() );
    PsiClass psiClassFromEditor = psiHelper.getPsiClassFromEditor( editor, project );
    if ( psiClassFromEditor != null ) {
      navigateOrDisplay( editor, psiClassFromEditor, dataContext );
    }
  }

  private void navigateOrDisplay( Editor editor, PsiClass psiClassFromEditor, DataContext dataContext ) {
    boolean isBuilder = serializerVerifier.isSerializer( psiClassFromEditor );
    PsiClass classToGo = findClassToGo( psiClassFromEditor, isBuilder );
    if ( classToGo != null ) {
      psiHelper.navigateToClass( classToGo );
    } else if ( !isBuilder ) {
      displayPopup( editor, psiClassFromEditor, dataContext );
    }
  }

  private void displayPopup( final Editor editor, final PsiClass psiClassFromEditor, final DataContext dataContext ) {
    throw new UnsupportedOperationException();
    //JList popupList = popupListFactory.getPopupList();
    //Project project = ( Project ) dataContext.getData( DataKeys.PROJECT.getName() );
    //displayChoosersRunnable.setEditor( editor );
    //displayChoosersRunnable.setProject( project );
    //displayChoosersRunnable.setPsiClassFromEditor( psiClassFromEditor );
    //popupDisplayer.displayPopupChooser( editor, popupList, displayChoosersRunnable );
  }

  @Nullable
  private PsiClass findClassToGo( @Nonnull PsiClass psiClassFromEditor, boolean isSerializer ) {
    if ( isSerializer ) {
      return serializerFinder.findClassForBuilder( psiClassFromEditor );
    }
    return serializerFinder.findBuilderForClass( psiClassFromEditor );
  }
}
