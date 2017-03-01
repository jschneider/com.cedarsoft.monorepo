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
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
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