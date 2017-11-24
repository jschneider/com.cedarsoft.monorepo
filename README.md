com.cedarsoft.monorepo
==================
One big repository containing most OpenSource projects created by [cedarsoft GmbH][cedarsoft]


### Projects contained in this repository
#### 
* [cedarsoft Annotatinos](annotations/README.md)
* [cedarsoft Unit](unit/README.md)
* [cedarsoft Serialization](serialization/README.md)
* [cedarsoft commons](commons/README.md)


Documentation and projects available at [cedarsoft.org]

[cedarsoft]: http://www.cedarsoft.com
[cedarsoft.org]: http://www.cedarsoft.org


[![Build Status](https://travis-ci.org/jschneider/com.cedarsoft.monorepo.svg?branch=develop)](https://travis-ci.org/jschneider/com.cedarsoft.monorepo)


# Release Notes

## 8.4.0 (2017-11-24)

### Bugfixes
* Fix package name for guava extension methods
* X509Support handles expired certificates correctly

### Features
* Upgrade dependencies
* Use dokka to generate documentation (instead of javadoc maven plugin)
* Add Kluent dependency
* Add Kotlin Guice extension methods
* Convert more tests to JUnit 5
* New annotations: @Sorted
* JavaFX
  * add exception handler with JavaFX base didalog
  * add balloon basics
  * add Kotlin extension methods

## 8.3.1 (2017-07-15)

### Bugfixes
* JsonUtils: Fix exception type: Use `org.junit.ComparisonFailure` to allow IDE to show diff
* LazyField uses Kotlin lazy()

## 8.3.0 (2017-07-10)
### Features
* Upgrade to JUnit 5
  * Convert rules to extensions
  * add Temporary Folder extension
* Kotlin
  * Add Kotlin Maven plugin
  * Convert a lot of classes to Kotlin
  * Some Guava extension methods
* Java Compiler: add parameter names

### Tasks
* Update Travic CI configuration to use latest JDK 8
* Update license headers
* Update version numbers for third party dependencies

## 8.2.1 (2017-06-10)
### Features
* Dependencies
  * new dependency: three-ten-extra
  * use newer Apache Commons artifact IDs
* add FxAsync: JavaFX implementation of the SwingAsync class
* add time module
  * DateUtils with duration formatting stuff
* Serialization
  * add duration serializer

## 8.2.0 (2017-05-27)

### Features
* Dependency Injection
  * introduce CachingProvider  
* ApplicationHomeAccess#createTemporaryHomeAccess() for unit testing
* Swing
  * JGoodies: add Validating Component
  * Dialogs: Improve borders
  * Fonts: add strike through font
* Serialization
  * Common Jackson serializers: Add serializer for java.time types  
* Dependencies
  * add Jackson databinding dependencies
* Version: add valueOf() method with parameter suffix

### Bugs
* Using charset for string/file access in several places
* CodeStyle
  * use @Immutable from ErrorProne compiler
  * avoiding a lot of compiler warnings
  * replace deprecated methods/classes
* Delete a lot of unused / old code and modules


## 8.1.1 (2017-05-04)
* Use project version for plugins
* Swing
  * SwingHelper: Return only existing frame if no frame is focused
  * Add busy icon to AbstractDialog
  * Exception Handler: Add support for TypeHandlers
* Update dependencies to latest version
  * add dep for XZ compression 

## 8.1.0 (2017-05-04)
* JGoodies related classes: 
    * IconFeedbackPanel
    * ComponentFactory
* delete Os* classes
* OptionDialog
  * improve radio dialog
  * add combo box version
* Serialization
  * Jackson
    * add UUID Serializer
  * Stax
    * Remove JSON support (use Jackson instead)
    
* New sub projects
  * Photos
  * OSGI validator maven plugin
  * Instrumentation maven plugin
  * Business
* Use error prone compiler
  * Use @Immutable class from error prone compiler
  
 
## 8.0.1 (2017-03-24)
* Using logback for logging
* Version number in dependency management can be overridden in a property: monorepo.dep.management.version
* add FileUtils
* add ExceptionHandling classes
* rename Application to ApplicationInformation
* introduce @Application
* add MemoryLeakWorkarounds
* add Swing related utility classes
* add CJideSplitPane
* add OptionDialog
* add SimpleSwingWorker
* add build number plugin

## 8.0.0 (2017-03-03)
* First release for mono repo containing several sub modules:
  * annotations
  * unit
  * commons
  * serialization
* travis integration
* Fix ThreadRule - avoids IllegalArgumentException in some corner cases
