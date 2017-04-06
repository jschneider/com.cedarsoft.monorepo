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
