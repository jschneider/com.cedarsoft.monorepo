package com.cedarsoft.maven.instrumentation.plugin;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Mojo( name = "instrument", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME )
public class InstrumentationMojo extends AbstractInstrumentationMojo {
  @Parameter(readonly = true, required = true, property = "project.build.outputDirectory")
  protected File outputDirectory;

  @Override
  @Nonnull
  protected File getOutputDirectory() {
    return outputDirectory;
  }

  /**
   * Project classpath.
   */
  @Parameter(readonly = true, required = true, property = "project.compileClasspathElements")
  protected List<String> classpathElements;

  @Nonnull
  @Override
  protected Iterable<? extends String> getClasspathElements() {
    return Collections.unmodifiableList( classpathElements );
  }

  @Nonnull
  @Override
  protected String getGoal() {
    return "instrument";
  }
}
