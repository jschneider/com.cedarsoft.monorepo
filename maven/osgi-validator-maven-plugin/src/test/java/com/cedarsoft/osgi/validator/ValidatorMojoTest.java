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
package com.cedarsoft.osgi.validator;

import static org.assertj.core.api.Assertions.*;

import java.io.File;

import javax.annotation.Nonnull;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.LoggerManager;
import org.junit.*;


/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Ignore
public class ValidatorMojoTest extends AbstractMojoTestCase {
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    LoggerManager loggerManager = getContainer().lookup(LoggerManager.class);
    loggerManager.setThreshold(Logger.LEVEL_DEBUG);
  }

  //public void testMultiSkip() throws Exception {
  //  Set<String> allowedPrefixes = ValidatorMojo.createAllowedPrefixes( "com.cedarsoft.commons", "xml-common", ImmutableSet.of( "commons", "common" ) );
  //  assertThat( allowedPrefixes ).contains( "com/cedarsoft/commons/xml/common" );
  //  assertThat( allowedPrefixes ).contains( "com/cedarsoft/xml" );
  //  assertThat( allowedPrefixes ).hasSize( 2 );
  //}
  //
  //public void testIssue1() throws Exception {
  //  Set<String> allowedPrefixes = ValidatorMojo.createAllowedPrefixes( "com.cedarsoft.commons", "xml-commons", ImmutableSet.of( "commons" ) );
  //  assertThat( allowedPrefixes ).contains( "com/cedarsoft/commons/xml/commons" );
  //  assertThat( allowedPrefixes ).contains( "com/cedarsoft/xml" );
  //  assertThat( allowedPrefixes ).hasSize( 2 );
  //}
  //
  //public void testPartsOsgi() throws Exception {
  //  Set<String> allowedPrefixes = ValidatorMojo.createAllowedPrefixes( "com.cedarsoft", "osgi-maven-plugin", Collections.<String>emptySet() );
  //  assertThat( allowedPrefixes ).hasSize( 2 );
  //  assertThat( allowedPrefixes ).contains( "com/cedarsoft/osgi/maven/plugin" );
  //  assertThat( allowedPrefixes ).contains( "com/cedarsoft/osgi" );
  //}
  //
  //public void testParts() throws Exception {
  //  Set<String> allowedPrefixes = ValidatorMojo.createAllowedPrefixes( "com.cedarsoft", "myGroup", Collections.<String>emptySet() );
  //  assertThat( allowedPrefixes ).hasSize( 1 );
  //  assertThat( allowedPrefixes ).contains( "com/cedarsoft/myGroup" );
  //}
  //
  //public void testParts2() throws Exception {
  //  Set<String> allowedPrefixes = ValidatorMojo.createAllowedPrefixes( "com.cedarsoft", "myGroup", ImmutableSet.of( "cedarsoft" ) );
  //  assertThat( allowedPrefixes ).hasSize( 2 );
  //  assertThat( allowedPrefixes )
  //    .contains( "com/cedarsoft/myGroup" )
  //    .contains( "com/myGroup" )
  //  ;
  //}
  //
  //public void testSkip2() throws Exception {
  //  Set<String> allowedPrefixes = ValidatorMojo.createAllowedPrefixes( "com.cedarsoft", "myart", ImmutableSet.of( "myart" ) );
  //  assertThat( allowedPrefixes ).hasSize( 2 );
  //  assertThat( allowedPrefixes )
  //    .contains( "com/cedarsoft/myart" )
  //    .contains( "com/cedarsoft" )
  //  ;
  //}
  //
  //public void testMaven() throws Exception {
  //  Set<String> allowedPrefixes = ValidatorMojo.createAllowedPrefixes( "com.cedarsoft-ear", "test-asdf-maven-plugin", ImmutableSet.of( "myart" ) );
  //  assertThat( allowedPrefixes ).hasSize( 2 );
  //  assertThat( allowedPrefixes )
  //    .contains( "com/cedarsoft/ear/test/asdf" )
  //    .contains( "com/cedarsoft/ear/test/asdf/maven/plugin" )
  //  ;
  //}

  public void testBasic() throws Exception {
    ValidatorMojo mojo = createMojo("basic.xml");
    assertThat(mojo).isNotNull();
    mojo.execute();
  }

  public void testInvalid() throws Exception {
    ValidatorMojo mojo = createMojo("invalid.xml");

    assertThat(mojo).isNotNull();

    try {
      mojo.execute();
      fail("Where is the Exception");
    } catch (MojoExecutionException ignore) {
    }
  }

  public void testMavenPlugin() throws Exception {
    ValidatorMojo mojo = createMojo("basic.xml");
    mojo.mavenProject.setGroupId("com.cedarsoft");
    mojo.mavenProject.setArtifactId("osgi-validator-maven-plugin");
    mojo.execute();
  }

  @Nonnull
  private ValidatorMojo createMojo(@Nonnull String name) throws Exception {
    File testPom = new File(getBasedir(), "src/test/resources/com/cedarsoft/osgi/validator/test/" + name);
    assertTrue(testPom.exists());
    ValidatorMojo mojo = (ValidatorMojo) lookupMojo("validate", testPom);

    assertNotNull(mojo);
    MavenProjectStub projectStub = new MavenProjectStub();
    projectStub.setGroupId("com.cedarsoft.osgi-validator");
    projectStub.setArtifactId("test");

    mojo.mavenProject = projectStub;

    //    cleanUp( mojo );
    return mojo;
  }

  //public void testPackagePath() throws Exception {
  //  assertThat(ValidatorMojo.createAllowedPrefix("com.cedarsoft", "test")).isEqualTo("com/cedarsoft/test");
  //  assertThat(ValidatorMojo.createAllowedPrefix("com.cedarsoft", "test-asdf")).isEqualTo("com/cedarsoft/test/asdf");
  //  assertThat(ValidatorMojo.createAllowedPrefix("com.cedarsoft-ear", "test-asdf")).isEqualTo("com/cedarsoft/ear/test/asdf");
  //
  //  assertThat(ValidatorMojo.createAllowedPrefix("com.cedarsoft-ear", "test-asdf-maven-plugin")).isEqualTo("com/cedarsoft/ear/test/asdf");
  //}
  //
  //public void testPossibleIds() throws Exception {
  //  assertThat(ValidatorMojo.createPossibleIds("com.cedarsoft.commons.history")).contains("com.cedarsoft.history");
  //  assertThat(ValidatorMojo.createPossibleIds("com.cedarsoft.commons.history")).contains("com.cedarsoft.commons.history");
  //}
  //
  //public void testMorePossibleIds() throws Exception {
  //  assertThat(ValidatorMojo.createPossibleIds("com.cedarsoft.domain.history", ImmutableSet.of( "domain" ))).hasSize( 2 )
  //    .contains("com.cedarsoft.history")
  //    .contains("com.cedarsoft.domain.history")
  //  ;
  //  assertThat(ValidatorMojo.createPossibleIds("com.cedarsoft.domain-asdf.history", ImmutableSet.of("domain"))).contains("com.cedarsoft.asdf.history");
  //}
}
