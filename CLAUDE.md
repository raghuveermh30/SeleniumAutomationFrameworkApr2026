# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Run the full regression suite (multi-browser parallel, as configured in pom.xml)
mvn clean test

# Run with a specific environment
mvn clean test -Denv=qa
mvn clean test -Denv=uat
mvn clean test -Denv=stage
# Default (no -Denv flag) uses config.properties (prod credentials)

# Run a specific test class
mvn clean test -Dtest=LoginPageTest
mvn clean test -Dtest=HomePageTest

# Run with a specific TestNG XML suite (override the default in pom.xml)
mvn clean test -DsuiteXmlFile=src/test/resources/runner/chrome_regression.xml
mvn clean test -DsuiteXmlFile=src/test/resources/runner/firefox_regression.xml

# Build without running tests
mvn clean install -DskipTests

# Generate and open Allure report (after a test run)
allure serve allure-results
```

The default suite (`testng_regression.xml`) runs all tests in parallel across Chrome, Firefox, and Edge with `thread-count="3"`. The browser for each `<test>` block is injected via the `<parameter name="browser">` XML attribute, which `BaseTest.setup()` receives and passes to `DriverFactory`.

## Architecture

### Layer structure

```
src/main/java/
  factory/       DriverFactory, OptionsManager          — driver lifecycle
  pages/         LoginPage, HomePage, ...               — Page Object Model
  utils/         ElementUtil, ExcelUtil, JSExecutorUtil  — Selenium helpers
  listeners/     AnnotationTransformer, Retry, TestAllureListener
  constants/     AppConstants, AppError                 — all magic strings/values

src/test/java/
  base/          BaseTest                               — setup/teardown, shared page fields
  test/          *Test classes                          — test methods only

src/test/resources/
  config/        config.properties (prod), qa/uat/stage.properties
  runner/        testng_regression.xml, chrome_regression.xml, firefox_regression.xml
  testdata/      openCartTestdata.xlsx, users.xlsx
```

### Key design decisions

**ThreadLocal WebDriver** — `DriverFactory.driverThreadLocal` stores one `WebDriver` per thread. Always access it via `DriverFactory.getDriver()`, never hold a local reference across test boundaries. `@BeforeTest` initializes the driver; `@AfterTest` quits it.

**BaseTest** — every test class extends `BaseTest`. It exposes `protected` fields for all page objects (`loginPage`, `homePage`, `searchResultsPage`, etc.) and `properties`. Tests initialize page objects they need in `@BeforeClass`; they do not call `new PageObject(driver)` themselves.

**Page Object contract** — every page class takes `WebDriver` in its constructor, holds an `ElementUtil` instance, and declares all locators as `private final By` fields. Navigation actions return the destination page object (e.g., `LoginPage.doLogin()` returns `HomePage`). Use `elementUtil.doSendKeys()` rather than `element.sendKeys()` directly — `doSendKeys` clears the field first.

**ElementUtil** — the canonical wrapper for all Selenium interactions. Prefer its explicit-wait methods (`waitForElementVisible`, `waitForElementsToBePresent`) over raw `driver.findElement`. `isElementDisplayed(By)` checks for **exactly one** matching element; use `getElements(By).size() > 0` when multiple are expected.

**Test data** — inline `@DataProvider` for small datasets; Excel via `ExcelUtil.getTestData(sheetName)` for larger sets. Sheet names are constants in `AppConstants`. The `doRegister()` method in `RegistrationPage` auto-appends a timestamp suffix to the email to ensure uniqueness.

**Reporting** — two reporting systems run simultaneously: Allure (via `TestAllureListener` + `@Step` annotations on page methods) and ChainTest (via `ChainTestListener`; call `ChainTestListener.log(msg)` inside test methods for step-level logs). Screenshots are attached on failure in `BaseTest.attachScreenshotOnFailure()`.

**Retry** — `AnnotationTransformer` applies `Retry` (IRetryAnalyzer) to all tests automatically. Configure the retry count inside `Retry.java`.

**Element highlighting** — the `highlight` property in config enables `JavaScriptExecutorUtil.flash()` on every element interaction. Set `highlight=true` in a properties file to enable visual debugging during local runs.

### Adding a new page

1. Create `src/main/java/pages/NewPage.java` — constructor takes `WebDriver`, instantiates `ElementUtil`, declares `private final By` locators.
2. Add a `protected NewPage newPage;` field to `BaseTest`.
3. Add `newPage = new NewPage(driver);` in `BaseTest.setup()` if it's a page the app always starts on, or return it from the preceding page's navigation method.

### Adding a new test class

1. Extend `BaseTest`.
2. Add Allure `@Epic`, `@Story`, `@Feature` class-level annotations.
3. Use `@BeforeClass` to perform login or navigation (page objects from `BaseTest` are available).
4. Register the class in the appropriate runner XML under a `<test>` block with a `<parameter name="browser">` element.

### Test priority conventions

- **Negative / pre-navigation tests**: use negative priorities (e.g., `-2`, `-1`) so they run before tests that navigate away from the initial page.
- **Destructive / logout tests**: use `priority = Integer.MAX_VALUE` so they run last within a test class.
- **Default priority (0)**: stateless read-only assertions that work from the starting page.

### Constants conventions

- All error assertion messages → `AppError`
- All timeouts, URL fragments, page titles, sheet names, file paths → `AppConstants`
- Never hardcode a timeout or URL string in a test or page class.

### Environment configuration

Each `.properties` file under `src/test/resources/config/` maps to an environment. The `url`, `browser`, `username`, `password`, `headless`, `incognito`, and `highlight` keys are read by `DriverFactory`. Pass `-Denv=<name>` to Maven to switch environments; omitting it selects `config.properties` (prod).
