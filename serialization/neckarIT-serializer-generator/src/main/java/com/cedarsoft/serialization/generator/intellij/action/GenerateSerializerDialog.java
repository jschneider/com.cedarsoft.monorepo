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
package it.neckar.open.serialization.generator.intellij.action;

import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.ide.util.DirectoryChooser;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.refactoring.move.moveClassesOrPackages.DestinationFolderComboBox;
import com.intellij.refactoring.ui.PackageNameReferenceEditorCombo;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.EnumComboBoxModel;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.util.IncorrectOperationException;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.jps.model.java.JavaResourceRootType;
import org.jetbrains.jps.model.java.JavaSourceRootType;
import org.jetbrains.jps.model.module.JpsModuleSourceRootType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.List;
import java.util.StringTokenizer;

/**
 */
public class GenerateSerializerDialog extends DialogWrapper {
  @Nonnull
  private final CollectionListModel<PsiField> fieldCollectionListModel;
  @Nonnull
  private final EnumComboBoxModel<Dialect> dialectEnumComboBoxModel = new EnumComboBoxModel<Dialect>(Dialect.class);

  @Nonnull
  private final PsiClass psiClass;
  private final ReferenceEditorComboWithBrowseButton referenceEditor;

  @Nonnull
  private final DestinationFolderComboBox serializerDestinationBox = new DestinationFolderComboBox() {
    @Override
    public String getTargetPackage() {
      return "";
    }
  };

  @Nonnull
  private final DestinationFolderComboBox testsDestinationBox = new DestinationFolderComboBox() {
    @Override
    public String getTargetPackage() {
      return "";
    }
  };

  @Nonnull
  private final ResourceFolderComboBox testResourcesDestinationBox = new ResourceFolderComboBox() {
    @Override
    public String getTargetPackage() {
      return "";
    }
  };

  public GenerateSerializerDialog(@Nonnull PsiClass psiClass) {
    super(psiClass.getProject());
    this.psiClass = psiClass;
    setTitle("Generate Serializer");

    fieldCollectionListModel = new CollectionListModel<>(psiClass.getAllFields());
    Project project = psiClass.getProject();
    referenceEditor = new PackageNameReferenceEditorCombo(guessPackage(), project, "generate.serializer.recent.packages", "Select Serializer target package");

    Module module = ModuleUtilCore.findModuleForPsiElement(psiClass);
    if (module == null) {
      throw new IllegalStateException("No module found for <" + psiClass + ">");
    }
    ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(module);

    PsiDirectory defaultDirectory = psiClass.getContainingFile().getContainingDirectory();
    PsiDirectory testResourceRoot = getSourceRootDir(project, moduleRootManager, defaultDirectory, JavaResourceRootType.TEST_RESOURCE);
    PsiDirectory testsRoot = getSourceRootDir(project, moduleRootManager, defaultDirectory, JavaSourceRootType.TEST_SOURCE);

    serializerDestinationBox.setData(project, defaultDirectory, referenceEditor.getChildComponent());
    testsDestinationBox.setData(project, testsRoot, referenceEditor.getChildComponent());
    testResourcesDestinationBox.setData(project, testResourceRoot, referenceEditor.getChildComponent());

    init();
  }

  @Nonnull
  private static PsiDirectory getSourceRootDir(@Nonnull Project project, ModuleRootManager moduleRootManager, @Nonnull PsiDirectory defaultDirectory, @Nonnull JpsModuleSourceRootType<?> sourceRootType) {
    List<VirtualFile> testResourceRoots = moduleRootManager.getSourceRoots(sourceRootType);
    if (testResourceRoots.isEmpty()) {
      return defaultDirectory;
    }
    @Nullable PsiDirectory directory = PsiManager.getInstance(project).findDirectory(testResourceRoots.get(0));
    if (directory == null) {
      return defaultDirectory;
    }
    return directory;
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    JPanel holder = new JPanel(new MigLayout("wrap 2, fill", "[][fill,grow]", "[][fill,grow][][][][]"));

    //ask for the dialect
    {
      ComboBox dialectList = new ComboBox(dialectEnumComboBoxModel);
      holder.add(new JLabel("Dialect:"));
      holder.add(dialectList);
    }

    {
      //noinspection TypeMayBeWeakened
      JBList fieldList = new JBList(fieldCollectionListModel);
      fieldList.setCellRenderer(new DefaultPsiElementCellRenderer());

      ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fieldList);
      decorator.disableAddAction();

      JPanel panel = decorator.createPanel();
      holder.add(LabeledComponent.create(panel, "Select fields to serialize"), "span 2, grow");
    }

    holder.add(new JLabel("Target package:"));
    holder.add(referenceEditor);
    holder.add(new JLabel("Serializer:"));
    holder.add(serializerDestinationBox);
    holder.add(new JLabel("Tests:"));
    holder.add(testsDestinationBox);
    holder.add(new JLabel("Test-Resources:"));
    holder.add(testResourcesDestinationBox);

    return holder;
  }

  @Nonnull
  private String guessPackage() {
    PsiJavaFile psiJavaFile = (PsiJavaFile) psiClass.getContainingFile();
    return psiJavaFile.getPackageName() + ".io";
    //JavaPsiFacade.getInstance( psiJavaFile.getProject() ).findPackage( psiJavaFile.getPackageName() );
  }

  public String getTargetPackageName() {
    return referenceEditor.getText();
  }

  @Nonnull
  public PsiDirectory getSerializerDestination() {
    PsiDirectory srcRootDir = ((DirectoryChooser.ItemWrapper) serializerDestinationBox.getComboBox().getSelectedItem()).getDirectory();
    return createPackage(srcRootDir, getTargetPackageName());
  }

  @Nonnull
  public PsiDirectory getTestsDestination() {
    PsiDirectory srcRootDir = ((DirectoryChooser.ItemWrapper) testsDestinationBox.getComboBox().getSelectedItem()).getDirectory();
    return createPackage(srcRootDir, getTargetPackageName());
  }

  @Nonnull
  public PsiDirectory getTestResourcesDestination() {
    PsiDirectory srcRootDir = ((DirectoryChooser.ItemWrapper) testResourcesDestinationBox.getComboBox().getSelectedItem()).getDirectory();
    return createPackage(srcRootDir, getTargetPackageName());
  }

  @Nonnull
  public List<? extends PsiField> getSelectedFields() {
    return fieldCollectionListModel.getItems();
  }

  @Nonnull
  public Dialect getSelectedDialect() {
    @Nullable Dialect selectedItem = dialectEnumComboBoxModel.getSelectedItem();
    if (selectedItem == null) {
      throw new IllegalStateException("No dialect selected");
    }
    return selectedItem;
  }

  @Nonnull
  public PsiClass getPsiClass() {
    return psiClass;
  }


  @Nonnull
  public static PsiDirectory createDirectory(@Nonnull PsiDirectory parent, @Nonnull String name)
    throws IncorrectOperationException {
    PsiDirectory result = null;

    for (PsiDirectory dir : parent.getSubdirectories()) {
      if (dir.getName().equalsIgnoreCase(name)) {
        result = dir;
        break;
      }
    }

    if (null == result) {
      result = parent.createSubdirectory(name);
    }

    return result;
  } // createDirectory()

  @Nonnull
  public static PsiDirectory createPackage(@Nonnull PsiDirectory sourceDir, @Nonnull String qualifiedPackage)
    throws IncorrectOperationException {
    PsiDirectory parent = sourceDir;
    StringTokenizer token = new StringTokenizer(qualifiedPackage, ".");
    while (token.hasMoreTokens()) {
      String dirName = token.nextToken();
      parent = createDirectory(parent, dirName);
    }
    return parent;
  } // createPackage()

  public enum Dialect {
    JACKSON,
    STAX_MATE
  }
}
