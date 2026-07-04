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


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnabledIfSystemProperty(named = "run.playwright.tests", matches = "true")
class ThymeleafPlaywrightIT {

  @LocalServerPort
  int port;

  @Test
  void navigateThroughGraphThenPatientsThenActionsForFirstPatient() {
    String baseUrl = "http://localhost:" + port;

    try (Playwright pw = Playwright.create()) {
      Browser browser = pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
      Page page = browser.newPage();

      page.navigate(baseUrl + "/");
      page.waitForSelector("text=View patients");

      // Click "View patients"
      page.locator("text=View patients").first().click();
      page.waitForSelector("text=Patients");

      // Assert patients header and search label
      assertTrue(page.locator("text=Patients").count() > 0, "Patients header not found");
      assertTrue(page.locator("text=Search patients:").count() > 0, "Search label not found");

      // Click the first patient link in the table
      Locator firstPatientLink = page.locator("#patients-body a").first();
      assertTrue(firstPatientLink.count() > 0, "No patient links found");
      firstPatientLink.click();
      page.waitForSelector("text=When");

      // On patient page assert action table headings
      assertTrue(page.locator("text=When").count() > 0, "When header missing");
      assertTrue(page.locator("text=Activity").count() > 0, "Activity header missing");
      assertTrue(page.locator("text=Context").count() > 0, "Context header missing");
      assertTrue(page.locator("text=Module").count() > 0, "Module header missing");

      browser.close();
    }
  }
}

