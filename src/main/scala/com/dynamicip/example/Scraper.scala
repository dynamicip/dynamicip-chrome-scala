package com.dynamicip.example

import java.io.File
import java.nio.file.Path
import java.util

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}

import scala.util.control.NonFatal

object Scraper {

  def main(args: Array[String]): Unit = {
    println("Running web scraper...")

    val driver = createDriver()
    try {
      // Load a javascript-enabled page.
      driver.navigate().to("https://examples.dynamicip.com/single-page-apps/basic")

      // Wait for javascript. This particular solution is specific to jQuery.
      waitForJQuery(driver)

      // Extract the DOM.
      val renderedHTML = driver.executeScript("return document.documentElement.outerHTML")

      // Display the result.
      println("Page response:")
      println(renderedHTML)
    }
    finally {
      driver.quit()
    }
  }

  private def createDriver(): ChromeDriver = {
    val options = new ChromeOptions()

    // Configure Chrome to use DynamicIP as a proxy.
    options.addArguments("--proxy-server=https://dynamicip.com:443")

    // Perform proxy authentication via a custom plugin (see 'resources/chrome_extension' dir).
    options.addArguments(s"--load-extension=${jarDirectory.resolve("chrome_extension").toAbsolutePath}")

    // ChromeDriver's default behaviour is to allow invalid certificates.
    // To improve security, we explicitly unset this flag here.
    options.setExperimentalOption("excludeSwitches", util.Arrays.asList("ignore-certificate-errors"))

    new ChromeDriver(options)
  }

  private def waitForJQuery(driver: JavascriptExecutor): Unit = {
    try {
      val jsTimeoutSecs = 30
      var index = 0
      while (index < jsTimeoutSecs) {
        index = index + 1
        val isJQueryComplete = driver.executeScript("return jQuery.active === 0").asInstanceOf[Boolean]
        if (isJQueryComplete) {
          index = jsTimeoutSecs // exit loop
        }
        else {
          Thread.sleep(1000)
        }
      }
    }
    catch { case NonFatal(ex) =>
      throw new Exception("Failed to load page. Problem may be temporary - please try again.", ex);
    }
  }

  private def jarDirectory: Path = {
    new File(getClass.getProtectionDomain.getCodeSource.getLocation.toURI.getPath).toPath.getParent
  }
}