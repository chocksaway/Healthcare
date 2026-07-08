package com.chocksaway.healthcare.web;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnabledIfSystemProperty(named = "run.playwright.tests", matches = "true")
class ThymeleafPlaywrightIT {

  private static final Logger log = LoggerFactory.getLogger(ThymeleafPlaywrightIT.class);

  @LocalServerPort
  int port;

  @Test
  void navigateThroughGraphThenPatientsThenActionsForFirstPatient() {
    String baseUrl = "http://localhost:" + port;

    log.info("Playwright test starting against {}", baseUrl);

    try (Playwright pw = Playwright.create()) {
      Browser browser = pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
      Page page = browser.newPage();

      // navigate to the index page
      log.info("Navigating to index page");
      page.navigate(baseUrl + "/");

      // Wait for the dashboard canvas to ensure the page and inline script have rendered
      log.debug("Waiting for canvas#breakdown to be present");
      page.waitForSelector("canvas#breakdown");

      // Grab the first inline <script> (exclude external scripts with a src attribute)
      String inlineScript = page.locator("script:not([src])").first().textContent();
      assertNotNull(inlineScript, "Expected an inline script on the index page");
      log.debug("Inline script length: {}", inlineScript.length());

      // Check the invited, registered, and discharged figures in the JavaScript
      boolean hasInvited = inlineScript.contains("const invited = 100;");
      boolean hasRegistered = inlineScript.contains("const registered = 96;");
      boolean hasDischarged = inlineScript.contains("const discharged = 17;");

      log.info("Found constants - invited: {}, registered: {}, discharged: {}", hasInvited, hasRegistered, hasDischarged);

      assertTrue(hasInvited, "Expected invited = 100 in inline script");
      assertTrue(hasRegistered, "Expected registered = 96 in inline script");
      assertTrue(hasDischarged, "Expected discharged = 17 in inline script");

      // Also wait for the View patients link to ensure page interactivity
      log.debug("Waiting for 'View patients' link");
      page.waitForSelector("text=View patients");

      // Click the view patients link
      log.info("Clicking 'View patients' link");


      page.locator("text=View patients").first().click();
      page.waitForSelector("text=Patients");


      // Assert patients header and search label
      assertTrue(page.locator("text=Patients").count() > 0, "Patients header found");
      assertTrue(page.locator("text=Search patients:").count() > 0, "Search label found");

      // Check the first patient link in the table
      Locator firstPatientLink = page.locator("#patients-body a").first();
      assertTrue(firstPatientLink.count() > 0, "Patient link found");
      assertEquals("Marcus O'Reilly", firstPatientLink.textContent(), "Marcus O'Reilly is the first patient");

      // Click the first patient link in the table
      firstPatientLink.click();

      page.waitForSelector("text=When");
      int actionRows = page.locator("table:has-text(\"When\") tbody tr").count();
      log.info("Action rows: {}", actionRows);

      // On patient page assert action table headings
      assertTrue(page.locator("text=When").count() > 0, "When header missing");
      assertTrue(page.locator("text=Activity").count() > 0, "Activity header missing");
      assertTrue(page.locator("text=Context").count() > 0, "Context header missing");
      assertTrue(page.locator("text=Module").count() > 0, "Module header missing");

      // subtract one for the When, Activity, Context, and Module headers.
      assertEquals(37, --actionRows, "37 Action rows found");
      log.info("Action rows: {}", actionRows); // optional assertion assertTrue(actionRows >= 0, "Found action rows");



      browser.close();
    }
  }
}

