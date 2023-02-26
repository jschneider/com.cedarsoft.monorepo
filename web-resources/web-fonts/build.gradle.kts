import de.undercouch.gradle.tasks.download.Download

/**
 * Downloads all fonts by calling
 *
 * gradle downloadFonts
 *
 * to src/main/resources
 */

plugins {
  download
}

val downloadGoogleFonts: Download = task<Download>("downloadGoogleFonts") {
  description = "Downloads Google fonts"
  group = "resources"

  val url2FileNames: Map<String, String> = mapOf(
    "https://fonts.google.com/download?family=Open%20Sans" to "OpenSans.ttf",
    "https://fonts.google.com/download?family=Roboto" to "Roboto.ttf",
    "https://fonts.google.com/download?family=Roboto%20Condensed" to "RobotoCondensed.ttf",
    "https://fonts.google.com/download?family=Roboto%20Flex" to "RobotoFlex.ttf",
    "https://fonts.google.com/download?family=Nunito" to "Nunito.ttf",
    "https://fonts.google.com/download?family=Oswald" to "Oswald.ttf",

    "https://github.com/google/material-design-icons/raw/master/variablefont/MaterialSymbolsOutlined%5BFILL%2CGRAD%2Copsz%2Cwght%5D.ttf" to "MaterialIcons.ttf",
  )

  src(url2FileNames.keys)
  eachFile {
    name = url2FileNames[this.sourceURL.toString()]
  }

  dest("src/main/resources/webfonts")
  overwrite(true)
  tempAndMove(true)
}


val downloadZipFiles: Download by tasks.creating(Download::class) {
  description = "Downloads zipped fonts"
  group = "resources"

  //Update the download link from here:
  //
  // https://fontawesome.com/download
  //

  src("https://use.fontawesome.com/releases/v6.3.0/fontawesome-free-6.3.0-web.zip")
  dest("build/download/zipped/fontawesome-free.zip")

  overwrite(true)
  tempAndMove(true)
}

val unzipDownloadedFiles: TaskProvider<Copy> = tasks.register<Copy>("unzipDownloadedFiles") {
  dependsOn(downloadZipFiles)

  from(zipTree(downloadZipFiles.dest)) {
    include("**/*.ttf")
    include("**/fontawesome.min.css")
    include("**/regular.min.css")
    include("**/solid.min.css")
    include("**/brands.min.css")
  }

  into("src/main/resources")
  includeEmptyDirs = false

  eachFile {
    //Remove the leading directory
    path = this.path.substringAfter("/")
  }
}


task("downloadFonts") {
  dependsOn(downloadGoogleFonts, unzipDownloadedFiles)
}

