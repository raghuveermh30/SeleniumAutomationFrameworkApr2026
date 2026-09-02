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
  pages/         LoginPage, HomePage, ...               — POM classes
  utils/         ElementUtil, ExcelUtil, JSExecutorUtil, BrowserUtil, StringUtil
  listeners/     AnnotationTransformer, Retry, TestAllureListener
  constants/     AppConstants, AppError                 — magic strings/values/errors

src/test/java/
  base/          BaseTest                               — setup/teardown, shared page fields
  test/          *Test classes
```

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

## Verification checklist

Before opening a PR:

1. `mvn clean compile -DskipTests` passes.
2. `mvn test -Dsurefire.suiteXmlFiles=src/test/resources/runner/chrome_regression.xml` passes.
3. `mvn allure:report` generates `allure-report/index.html`.
4. No generated files are staged.
