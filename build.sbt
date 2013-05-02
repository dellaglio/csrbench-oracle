name := "srbench-oracle"

organization := "eu.planetdata"

version := "0.0.1"

scalaVersion := "2.10.1"

crossPaths := false

libraryDependencies ++= Seq(
  "org.openrdf.sesame" % "sesame-runtime" % "2.7.0",
  "commons-configuration" % "commons-configuration" % "1.9",
  "ch.qos.logback" % "logback-classic" % "1.0.11",
  "org.codehaus.jackson" % "jackson-jaxrs" % "1.9.12",
  "junit" % "junit" % "4.7" % "test")

resolvers ++= Seq(
  DefaultMavenRepository,
  "Local ivy Repository" at "file://"+Path.userHome.absolutePath+"/.ivy2/local"
 )

scalacOptions += "-deprecation"

EclipseKeys.skipParents in ThisBuild := false

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource


