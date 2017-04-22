package com.cedarsoft.maven.instrumentation.plugin;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Mojo( name = "instrument-tests", defaultPhase = LifecyclePhase.PROCESS_TEST_CLASSES, requiresDependencyResolution = ResolutionScope.TEST )
public class InstrumentationTestsMojo extends AbstractInstrumentationMojo {
  @Parameter(readonly = true, required = true, property = "project.build.testOutputDirectory")
  private File outputDirectory;

  @Nonnull
  @Override
  protected String getGoal() {
    return "instrument-tests";
  }

  @Override
  @Nonnull
  protected File getOutputDirectory() {
    return outputDirectory;
  }

  /**
   * Project classpath.
   */
  @Parameter(readonly = true, required = true, property = "project.testClasspathElements")
  private List<String> classpathElements;

  @Nonnull
  @Override
  protected Iterable<? extends String> getClasspathElements() {
    return Collections.unmodifiableList(classpathElements);
  }
}
