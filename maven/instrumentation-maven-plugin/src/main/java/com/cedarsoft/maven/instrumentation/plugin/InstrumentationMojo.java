/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
