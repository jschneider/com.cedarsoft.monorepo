package com.cedarsoft.exec;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Class that is able to start a new java process
 */
public class JavaCommandBuilder {
  @NonNls
  @NotNull
  private String javaBin = "java";
  @NotNull
  private final List<String> classPathElements = new ArrayList<String>();
  @NotNull
  @NonNls
  private final String mainClass;
  @NotNull
  private final List<String> vmProperties = new ArrayList<String>();
  @NotNull
  private final List<String> arguments = new ArrayList<String>();

  public JavaCommandBuilder( @NotNull @NonNls String mainClass ) {
    this.mainClass = mainClass;
  }

  @NotNull
  public List<String> getClassPathElements() {
    return Collections.unmodifiableList( classPathElements );
  }

  public void setClassPathElements( @NotNull @NonNls String... classPathElements ) {
    this.classPathElements.clear();
    for ( String element : classPathElements ) {
      if ( element == null || element.length() == 0 ) {
        continue;
      }
      this.classPathElements.add( element );
    }
  }

  @NotNull
  @NonNls
  public String getJavaBin() {
    return javaBin;
  }

  /**
   * Returns the classpath
   *
   * @return the classpath
   */
  @Nullable
  @NonNls
  public String getClassPath() {
    if ( classPathElements.isEmpty() ) {
      return null;
    }
    StringBuilder stringBuilder = new StringBuilder();
    for ( Iterator<String> it = classPathElements.iterator(); it.hasNext(); ) {
      String classPathElement = it.next();
      if ( classPathElement == null || classPathElement.length() == 0 ) {
        continue;
      }
      stringBuilder.append( classPathElement );
      if ( it.hasNext() ) {
        stringBuilder.append( File.pathSeparator );
      }
    }

    String classPath = stringBuilder.toString();
    if ( classPath.contains( " " ) ) {
      if ( !System.getProperty( "os.name" ).toLowerCase().contains( "windows" ) ) {
        System.err.println( "WARNING: Be carefull! Added \" to the classpath. May be incompatible!" );
      }
      return '\"' + classPath + '\"';
    } else {
      return classPath;
    }
  }

  @NotNull
  @NonNls
  public String getMainClass() {
    return mainClass;
  }

  public void addClassPathElement( @NotNull @NonNls String classPathElement ) {
    classPathElements.add( classPathElement );
  }

  @NotNull
  public List<String> getVmProperties() {
    return Collections.unmodifiableList( vmProperties );
  }

  public void setVmProperties( @NonNls @NotNull String... vmProperties ) {
    this.vmProperties.clear();
    for ( String vmProperty : vmProperties ) {
      if ( vmProperty == null || vmProperty.length() == 0 ) {
        continue;
      }
      this.vmProperties.add( vmProperty );
    }
  }

  public void addVmProperty( @NotNull @NonNls String vmProeprty ) {
    vmProperties.add( vmProeprty );
  }

  public void addArgument( @NotNull @NonNls String argument ) {
    this.arguments.add( argument );
  }

  public void setArguments( @NotNull @NonNls String... arguments ) {
    this.arguments.clear();
    for ( String argument : arguments ) {
      if ( argument == null || argument.length() == 0 ) {
        continue;
      }
      this.arguments.add( argument );
    }
  }

  @NotNull
  @NonNls
  public List<String> getArguments() {
    return Collections.unmodifiableList( arguments );
  }

  @NotNull
  @NonNls
  public List<String> getCommandLineElements() {
    List<String> elements = new ArrayList<String>();
    elements.add( getJavaBin() );

    for ( String property : getVmProperties() ) {
      elements.add( "-D" + property );
    }

    String classPath = getClassPath();
    if ( classPath != null ) {
      elements.add( "-cp" );
      elements.add( classPath );
    }
    
    elements.add( getMainClass() );

    for ( String argument : getArguments() ) {
      elements.add( argument );
    }

    return elements;
  }

  /**
   * Returns the command line as string. This method should only be used
   * for debugging purposes.
   * For creating a process use {@link #getCommandLineElements()} instead.
   *
   * @return the command line as string
   *
   * @see #getCommandLineElements()
   */
  @NotNull
  @NonNls
  public String getCommandLine() {
    StringBuilder stringBuilder = new StringBuilder();
    for ( String element : getCommandLineElements() ) {
      stringBuilder.append( element );
      stringBuilder.append( ' ' );
    }

    return stringBuilder.toString().trim();
  }
}
