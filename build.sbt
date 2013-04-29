name := "srbench-oracle"

organization := "eu.planetdata"

version := "0.0.1"

scalaVersion := "2.10.1"

crossPaths := false

//externalPom()

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.2",
  "org.joda" % "joda-convert" % "1.3.1",
  "org.openrdf.sesame" % "sesame-runtime" % "2.7.0",
  "commons-configuration" % "commons-configuration" % "1.9",
  "ch.qos.logback" % "logback-classic" % "1.0.11",
  "org.codehaus.jackson" % "jackson-jaxrs" % "1.9.12",
  "junit" % "junit" % "4.7" % "test",
  "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test",
  "org.scalacheck" % "scalacheck_2.10" % "1.10.0" % "test"
)

resolvers ++= Seq(
  DefaultMavenRepository,
  "Local ivy Repository" at "file://"+Path.userHome.absolutePath+"/.ivy2/local"
//  "aldebaran-releases" at "http://aldebaran.dia.fi.upm.es/artifactory/sstreams-releases-local"
 )

scalacOptions += "-deprecation"

EclipseKeys.skipParents in ThisBuild := false

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

//unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(Seq(_))

//unmanagedSourceDirectories in Test <<= (scalaSource in Test)(Seq(_))

