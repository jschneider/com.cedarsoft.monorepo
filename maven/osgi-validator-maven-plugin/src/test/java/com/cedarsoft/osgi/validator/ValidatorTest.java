/**
 * Copyright (C) cedarsoft GmbH.
 * <p>
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
 * <p>
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 * <p>
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * <p>
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * <p>
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.osgi.validator;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Fail.fail;

import java.io.File;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;

/**
 */
public class ValidatorTest {
  @Test
  public void testBug2() throws Exception {
    Validator validator = new Validator("com.cedarsoft.commons.swing.swing-presenter", ImmutableSet.of("commons"));
    validator.isValid(fixPathSeparators("com/cedarsoft/swing/presenter/SwingPresenter.java"));
  }

  @NotNull
  private static String fixPathSeparators(@Nonnull String relativePath) {
    if (SystemUtils.IS_OS_WINDOWS) {
      return relativePath.replace('/', '\\');
    }

    return relativePath;
  }

  @Test
  public void testIssue1() throws Exception {
    Validator validator = new Validator("com.cedarsoft.commons.xml-commons", ImmutableSet.of("commons"));

    validator.isValid(fixPathSeparators("com/cedarsoft/xml"));
    validator.isValid(fixPathSeparators("com/cedarsoft/commons/xml"));

    validator.isValid(fixPathSeparators("com/cedarsoft/commons/xml/commons"));
    validator.isValid(fixPathSeparators("com/cedarsoft/xml/commons"));


    try {
      validator.isValid(fixPathSeparators("com/cedarsoft/xml2/commons"));
      fail("Where is the Exception");
    } catch (ValidationFailedException e) {
      assertThat(e).hasMessage(fixPathSeparators("Invalid path <com/cedarsoft/xml2/commons>. Failed at <com/cedarsoft/xml2>: Expected <xml>."));
    }
  }

  @Test
  public void test2() throws Exception {
    Validator validator = new Validator("com.cedarsoft.osgi-maven-plugin", ImmutableSet.of("maven", "plugin"));

    validator.isValid(fixPathSeparators("com/cedarsoft/osgi/maven/plugin"));
    validator.isValid(fixPathSeparators("com/cedarsoft/osgi"));
  }

  @Test
  public void test3() throws Exception {
    Validator validator = new Validator("com.cedarsoft.mygroup", ImmutableSet.<String>of());

    validator.isValid(fixPathSeparators("com/cedarsoft/mygroup"));
    try {
      validator.isValid(fixPathSeparators("com/cedarsoft/mygroup2"));
      fail("Where is the Exception");
    } catch (ValidationFailedException e) {
      assertThat(e).hasMessage(fixPathSeparators("Invalid path <com/cedarsoft/mygroup2>. Failed at <com/cedarsoft/mygroup2>: Expected <mygroup>."));
    }

    try {
      validator.isValid(fixPathSeparators("com/cedarsoft"));
      fail("Where is the Exception");
    } catch (ValidationFailedException e) {
      assertThat(e).hasMessage(fixPathSeparators("Invalid path <com/cedarsoft>: Too short for project id <com.cedarsoft.mygroup>"));
    }
    try {
      validator.isValid("com");
      fail("Where is the Exception");
    } catch (ValidationFailedException e) {
      assertThat(e).hasMessage("Invalid path <com>: Too short for project id <com.cedarsoft.mygroup>");
    }
    try {
      validator.isValid("");
      fail("Where is the Exception");
    } catch (ValidationFailedException e) {
      assertThat(e).hasMessage("Invalid path <>: Too short for project id <com.cedarsoft.mygroup>");
    }
  }

  @Test
  public void test4() throws Exception {
    Validator validator = new Validator("com.cedarsoft-ear.test-asdf-maven-plugin", ImmutableSet.<String>of("maven", "plugin"));

    validator.isValid(fixPathSeparators("com/cedarsoft/ear/test/asdf"));
    validator.isValid(fixPathSeparators("com/cedarsoft/ear/test/asdf/maven/plugin"));
  }

  @Test
  public void testSimpleSkip() throws Exception {
    Validator validator = new Validator("com.cedarsoft.commons.mygroup", ImmutableSet.of("commons"));

    validator.isValid(fixPathSeparators("com/cedarsoft/mygroup"));
    validator.isValid(fixPathSeparators("com/cedarsoft/commons/mygroup"));
    try {
      validator.isValid(fixPathSeparators("com/cedarsoft/mygroup2"));
      fail("Where is the Exception");
    } catch (ValidationFailedException e) {
      assertThat(e).hasMessage(fixPathSeparators("Invalid path <com/cedarsoft/mygroup2>. Failed at <com/cedarsoft/mygroup2>: Expected <mygroup>."));
    }
  }

  @Test
  public void testSplitterEmpty() throws Exception {
    Splitter splitter = Splitter.on(File.separator).omitEmptyStrings();
    assertThat(splitter.split("")).hasSize(0);
  }
}
