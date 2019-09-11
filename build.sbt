resolvers += Resolver.bintrayRepo("netlogo", "NetLogo-JVM")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.8" % Test,
  "org.nlogo" % "netlogo" % "6.1.0" % Test
)

scalaVersion := "2.12.8"

lazy val downloadFromZip = taskKey[Unit]("Download zipped extensions and extract them to ./extensions")

/// Here you have to specified the extensions used the netlogo model
downloadFromZip := {
  val baseURL = "https://raw.githubusercontent.com/NetLogo/NetLogo-Libraries/6.1/extensions/"
  val extensions = List(
    "table" -> "table-1.3.1.zip",
    "nw" -> "nw-3.7.7.zip",
    "rnd" -> "rnd-3.0.1.zip",
    "csv" -> "csv-1.1.1.zip"
  )
  for {
    (extension, file) <- extensions
    path = new File("extensions/" + extension)
    if java.nio.file.Files.notExists(path.toPath)
    url = new URL(baseURL + file)
  } {
    println("Downloading " + url)
    IO.unzipURL(url, path)
  }
}

compile in Test := (compile in Test).dependsOn(downloadFromZip).value

fork in Test := true
