name := "TarExtractor"

version := "0.1"

scalaVersion := "2.13.4"

libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
libraryDependencies += "org.apache.commons" % "commons-compress" % "1.20"
// https://mvnrepository.com/artifact/commons-io/commons-io
libraryDependencies += "commons-io" % "commons-io" % "2.6"
//https://mvnrepository.com/artifact/org.tukaani/xz/1.5
libraryDependencies += "org.tukaani" % "xz" % "1.5"

// Add dependency on ScalaFX library
libraryDependencies += "org.scalafx" %% "scalafx" % "15.0.1-R21"

// Determine OS version of JavaFX binaries
lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux")   => "linux"
  case n if n.startsWith("Mac")     => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

// Add dependency on JavaFX libraries, OS dependent
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map( m =>
  "org.openjfx" % s"javafx-$m" % "15.0.1" classifier osName
)