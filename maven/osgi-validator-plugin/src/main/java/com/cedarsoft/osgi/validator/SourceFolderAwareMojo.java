package com.cedarsoft.osgi.validator;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class SourceFolderAwareMojo extends AbstractMojo {
  /**
   * The source directories containing the sources to be compiled.
   *
   */
  @Parameter (defaultValue = "${project.compileSourceRoots}", readonly = true, required = true)
  protected List<String> sourceRoots;
  /**
   * The source directories containing the test sources to be compiled.
   *
   */
  @Parameter (defaultValue = "${project.testCompileSourceRoots}", readonly = true, required = true)
  protected List<String> testSourceRoots;

  /**
   * The list of resources.
   *
   */
  @Parameter (defaultValue = "${project.resources}", readonly = true, required = true)
  private List<Resource> resources;

  /**
   * The list of test resources
   *
   */
  @Parameter (defaultValue = "${project.testResources}", readonly = true, required = true)
  private List<Resource> testResources;

  @Parameter (defaultValue = "${project.build.outputDirectory}", readonly = true, required = true)
  protected File classesDir;

  @Parameter (defaultValue = "${project}", readonly = true, required = true)
  protected MavenProject mavenProject;



  protected MavenProject getProject() {
    return mavenProject;
  }

  @Nonnull
  public List<String> getSourceRoots() {
    return Collections.unmodifiableList(sourceRoots);
  }
}
