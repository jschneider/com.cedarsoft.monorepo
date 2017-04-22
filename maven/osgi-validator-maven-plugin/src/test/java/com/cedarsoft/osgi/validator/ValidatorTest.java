package com.cedarsoft.osgi.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

import java.io.File;

import org.junit.*;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ValidatorTest {
  @Test
  public void testBug2() throws Exception {
    Validator validator = new Validator( "com.cedarsoft.commons.swing.swing-presenter", ImmutableSet.of( "commons" ) );
    validator.isValid( "com/cedarsoft/swing/presenter/SwingPresenter.java" );
  }

  @Test
  public void testIssue1() throws Exception {
    Validator validator = new Validator( "com.cedarsoft.commons.xml-commons", ImmutableSet.of( "commons" ) );

    validator.isValid( "com/cedarsoft/xml" );
    validator.isValid( "com/cedarsoft/commons/xml" );

    validator.isValid( "com/cedarsoft/commons/xml/commons" );
    validator.isValid( "com/cedarsoft/xml/commons" );


    try {
      validator.isValid( "com/cedarsoft/xml2/commons" );
      fail( "Where is the Exception" );
    } catch ( ValidationFailedException e ) {
      assertThat( e ).hasMessage( "Invalid path <com/cedarsoft/xml2/commons>. Failed at <com/cedarsoft/xml2>: Expected <xml>." );
    }
  }

  @Test
  public void test2() throws Exception {
    Validator validator = new Validator( "com.cedarsoft.osgi-maven-plugin", ImmutableSet.of( "maven", "plugin" ) );

    validator.isValid( "com/cedarsoft/osgi/maven/plugin" );
    validator.isValid( "com/cedarsoft/osgi" );
  }

  @Test
  public void test3() throws Exception {
    Validator validator = new Validator( "com.cedarsoft.mygroup", ImmutableSet.<String>of() );

    validator.isValid( "com/cedarsoft/mygroup" );
    try {
      validator.isValid( "com/cedarsoft/mygroup2" );
      fail( "Where is the Exception" );
    } catch ( ValidationFailedException e ) {
      assertThat( e ).hasMessage( "Invalid path <com/cedarsoft/mygroup2>. Failed at <com/cedarsoft/mygroup2>: Expected <mygroup>." );
    }

    try {
      validator.isValid( "com/cedarsoft" );
      fail( "Where is the Exception" );
    } catch ( ValidationFailedException e ) {
      assertThat( e ).hasMessage( "Invalid path <com/cedarsoft>: Too short for project id <com.cedarsoft.mygroup>" );
    }
    try {
      validator.isValid( "com" );
      fail( "Where is the Exception" );
    } catch ( ValidationFailedException e ) {
      assertThat( e ).hasMessage( "Invalid path <com>: Too short for project id <com.cedarsoft.mygroup>" );
    }
    try {
      validator.isValid( "" );
      fail( "Where is the Exception" );
    } catch ( ValidationFailedException e ) {
      assertThat( e ).hasMessage( "Invalid path <>: Too short for project id <com.cedarsoft.mygroup>" );
    }
  }

  @Test
  public void test4() throws Exception {
    Validator validator = new Validator( "com.cedarsoft-ear.test-asdf-maven-plugin", ImmutableSet.<String>of( "maven", "plugin" ) );

    validator.isValid( "com/cedarsoft/ear/test/asdf" );
    validator.isValid( "com/cedarsoft/ear/test/asdf/maven/plugin" );
  }

  @Test
  public void testSimpleSkip() throws Exception {
    Validator validator = new Validator( "com.cedarsoft.commons.mygroup", ImmutableSet.<String>of( "commons" ) );

    validator.isValid( "com/cedarsoft/mygroup" );
    validator.isValid( "com/cedarsoft/commons/mygroup" );
    try {
      validator.isValid( "com/cedarsoft/mygroup2" );
      fail( "Where is the Exception" );
    } catch ( ValidationFailedException e ) {
      assertThat( e ).hasMessage( "Invalid path <com/cedarsoft/mygroup2>. Failed at <com/cedarsoft/mygroup2>: Expected <mygroup>." );
    }
  }

  @Test
  public void testSplitterEmpty() throws Exception {
    Splitter splitter = Splitter.on( File.separator ).omitEmptyStrings();
    assertThat( splitter.split( "" ) ).hasSize( 0 );
  }
}
