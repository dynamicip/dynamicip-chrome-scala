name := "dynamicip-chrome-scala"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "org.seleniumhq.selenium" % "selenium-java"          % "3.6.0",
  "org.seleniumhq.selenium" % "selenium-chrome-driver" % "3.6.0"
)