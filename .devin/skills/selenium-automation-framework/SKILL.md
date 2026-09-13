# Selenium Automation Framework Skill

Guidance for working with the OpenCart Selenium automation framework in this repository.

## Project overview

A Java 11 / Maven / TestNG Selenium WebDriver framework for the OpenCart demo e-commerce application (`https://naveenautomationlabs.com/opencart`).

Key characteristics:

- **Pattern:** Page Object Model (POM)
- **Parallel execution:** ThreadLocal `WebDriver` — always access via `DriverFactory.getDriver()`
- **Browsers:** Chrome, Firefox, Edge, Safari
- **Reporting:** Allure + ChainTest
- **Test data:** Inline TestNG DataProviders and Apache POI Excel sheets
- **Environments:** `config.properties` (prod default), `qa.properties`, `uat.properties`, `stage.properties`

## Build and run commands

```bash
# Full regression (parallel across Chrome/Firefox/Edge via testng_regression.xml)
mvn clean test

# Run a specific test class
mvn clean test -Dtest=LoginPageTest

# Run a specific browser suite
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/runner/chrome_regression.xml
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/runner/firefox_regression.xml

# Target a specific environment
mvn clean test -Denv=qa
mvn clean test -Denv=uat
mvn clean test -Denv=stage

# Build without tests
mvn clean install -DskipTests

# Generate the Allure HTML report (added via allure-maven plugin)
mvn allure:report

# Serve Allure interactively (requires allure CLI)
allure serve allure-results
```

Reports are produced at:

- ChainTest: `target/chaintest/Index.html`
- Allure results: `allure-results/`
- Allure HTML report: `allure-report/index.html`

## Architecture conventions

### Layer structure

```
src/main/java/
  factory/       DriverFactory, OptionsManager          — driver lifecycle
  pages/         LoginPage, HomePage, CommonPage, ...   — POM classes
  utils/         ElementUtil, ExcelUtil, JavaScriptExecutorUtil, BrowserUtil, StringUtil, TestDataUtil
  listeners/     AnnotationTransformer, Retry, TestAllureListener
  constants/     AppConstants, AppError                 — magic strings/values/errors
  errors/        FrameworkException                     — unchecked config/setup errors

src/test/java/
  base/          BaseTest                               — setup/teardown, shared page fields
  test/          *Test classes
```

- `pages.CommonPage` holds locators/actions shared across pages (logo, footer links).
- `pages.LoginDemoPage` is a standalone scratch class with a `main()` method used for manual experimentation; it is **not** part of the automated test flow and has no Javadoc-worthy behavior beyond a `System.out.println`.
- `utils.TestDataUtil` centralizes reusable `@DataProvider`s shared across test classes (e.g. `footerLinksData`), avoiding per-test duplication.
- `errors.FrameworkException` is thrown by `DriverFactory` for invalid browser/environment names — a `RuntimeException` subclass, so callers aren't forced to catch it.

### Page Object contract

1. Constructor takes `WebDriver` and instantiates `ElementUtil`.
2. Locators are `private final By` fields.
3. Navigation actions return the destination page object (e.g., `LoginPage.doLogin()` returns `HomePage`).
4. Use `ElementUtil` methods instead of raw `driver.findElement`.

### BaseTest lifecycle

- `@BeforeTest` initializes `DriverFactory`, loads environment properties, and creates shared page objects.
- `@AfterMethod` attaches a screenshot on failure via `ChainTestListener`.
- `@AfterTest` quits the driver.
- The `browser` parameter is `@Optional`, so tests can be launched from XML suites **or** with `-Dtest=...`.

### Important implementation notes

- **ThreadLocal WebDriver:** `DriverFactory.getDriver()` returns the per-thread driver. Do not hold local `WebDriver` references across test boundaries.
- **`@BeforeTest` vs `@BeforeClass`:** Page fields like `homePage`, `searchResultsPage`, etc. are declared in `BaseTest` but initialized by navigation methods in tests or in `@BeforeClass` methods. Because `@BeforeTest` runs once per `<test>` tag, this design works correctly only when each `<test>` block in the XML contains **one test class**. Running multiple test classes via `-Dtest=Class1,Class2,Class3` causes null-field issues because `@BeforeTest` initializes only one instance.
- **ElementUtil `isElementDisplayed(By)`** checks for exactly one matching element; use `getElements(By).size() > 0` when multiple elements are expected.
- **Excel data:** `ExcelUtil.getTestData(String sheetName)` reads from `users.xlsx`. For product data, use `ExcelUtil.getTestData(String filePath, String sheetName)` with `AppConstants.TEST_DATA_WORKBOOK_PATH`.
- **Unique emails:** Use `StringUtil.getUniqueEmail(baseEmail)` instead of manual timestamp concatenation.
- **Browser options:** `OptionsManager` applies `headless` and `incognito` flags from properties.
- **Retry count:** `listeners.Retry` retries a failed test up to `maxTry = 3` times before it's marked permanently failed; `AnnotationTransformer` wires `Retry` onto every `@Test` automatically, so individual test methods don't need `retryAnalyzer` set explicitly.
- **Default suite coverage:** `testng_regression.xml` (the default suite) only wires up `LoginPageTest`, `HomePageTest`, and `ProductInfoPageTest` across browsers. `RegistrationPageTest` and `LogoutPageTest` exist but are **not** included in the default regression run — run them explicitly with `-Dtest=RegistrationPageTest` / `-Dtest=LogoutPageTest`, or add them to a runner XML, if you need them exercised.

## Adding new code

### Add a new page

1. Create `src/main/java/pages/NewPage.java`.
2. Add `protected NewPage newPage;` to `BaseTest`.
3. Initialize it in `BaseTest.setup()` or return it from the preceding page's navigation method.
4. Add class and method Javadoc.

### Add a new test class

1. Extend `BaseTest`.
2. Add Allure `@Epic`, `@Story`, `@Feature` class-level annotations.
3. Use `@BeforeClass` for login/navigation setup.
4. Register the class in the appropriate runner XML under a `<test>` block with a `<parameter name="browser">` element.

### Test priority conventions

- Negative / pre-navigation tests: `-2`, `-1`
- Default stateless assertions: `0`
- Destructive / logout tests: `Integer.MAX_VALUE`

## Constants and Javadoc

- Place error assertion messages in `AppError`.
- Place timeouts, URL fragments, page titles, sheet names, and file paths in `AppConstants`.
- Add Javadoc to every public class, constructor, and method.
- Avoid hardcoding timeouts or URL strings in test/page classes.

## Common pitfalls

- **Compile errors from unused imports:** This project previously had invalid `software.amazon.awssdk.services.s3.endpoints.internal.Value` imports that broke compilation. Remove any stray unused imports before committing.
- **SLF4J binding mismatch:** The project now uses `slf4j-api` 2.x with `log4j-slf4j2-impl`. Do not revert to `log4j-slf4j-impl`.
- **Allure results directory:** Allure results are written to the project root `allure-results/`. The Allure Maven plugin is configured to read from `${project.basedir}/allure-results` and generate into `${project.basedir}/allure-report`.
- **Generated files:** `allure-results/`, `allure-report/`, `.allure/`, `logs/`, `target/`, and `.idea/workspace.xml` should not be committed. They are listed in `.gitignore`.
- **Cross-browser runs:** Firefox runs may fail if `geckodriver` is missing or corrupted locally. Verify driver availability before debugging test failures on Firefox/Edge.
- **Safari driver is NOT ThreadLocal-safe:** In `DriverFactory.initDriver()`, the `chrome`/`firefox`/`edge` branches call `driverThreadLocal.set(...)`, but the `safari` branch assigns to the plain instance field `driver` instead. `DriverFactory.getDriver()` reads only `driverThreadLocal`, so Safari runs will NPE on `getDriver()`. Fix this (set `driverThreadLocal` in the safari branch too) before relying on Safari, especially in parallel suites.

## Verification checklist

Before opening a PR:

1. `mvn clean compile -DskipTests` passes.
2. `mvn test -Dsurefire.suiteXmlFiles=src/test/resources/runner/chrome_regression.xml` passes.
3. `mvn allure:report` generates `allure-report/index.html`.
4. No generated files are staged.
