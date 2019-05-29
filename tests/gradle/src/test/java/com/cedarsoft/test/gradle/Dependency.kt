package com.cedarsoft.test.gradle

/**
 * A maven dependency
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
data class Dependency(val groupId: String, val artifactId: String, val version: String?, val scope: Scope) {
  fun format(): String {
    return "$groupId:$artifactId:$version"
  }
}

enum class Scope {
  COMPILE,
  PROVIDED,
  TEST;

  companion object {
    fun parse(scopeAsString: String?): Scope {
      if (scopeAsString == null) {
        return COMPILE
      }

      if (scopeAsString == "compile") {
        return COMPILE
      }
      if (scopeAsString == "test") {
        return TEST
      }
      if (scopeAsString == "provided") {
        return PROVIDED
      }

      throw IllegalArgumentException("Invalid string <$scopeAsString>")
    }
  }

}