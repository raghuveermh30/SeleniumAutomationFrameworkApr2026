# SeleniumAutomationFrameworkApr2026

A robust, production-ready Selenium test automation framework for the OpenCart e-commerce application. Built with the Page Object Model pattern, parallel cross-browser execution, dual reporting (Allure + ChainTest), and Excel-driven test data.

## Repository

**GitHub:** https://github.com/raghuveermh30/SeleniumAutomationFrameworkApr2026

---

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 11 | Language |
| Selenium WebDriver | 4.41.0 | Browser automation |
| TestNG | 7.7.0 | Test runner & parallel execution |
| Maven | 3.x | Build & dependency management |
| Allure | 2.24.0 | Rich HTML reporting |
| ChainTest | 1.0.11 | Step-level HTML reporting |
| Apache POI | 3.9 | Excel test data |
| Log4j 2 | 2.20.0 | Logging |
| AspectJ | 1.9.20.1 | Allure agent weaving |

---

## Project Structure

```
src/
├── main/java/
│   ├── constants/       AppConstants, AppError       — all magic strings & timeouts
│   ├── errors/          FrameworkException            — custom exception
│   ├── factory/         DriverFactory, OptionsManager — driver lifecycle (ThreadLocal)
│   ├── listeners/       AnnotationTransformer, Retry, TestAllureListener
│   ├── pages/           LoginPage, HomePage, SearchResultsPage,
│   │                    ProductInfoPage, RegistrationPage, LogoutPage, ...
│   └── utils/           ElementUtil, ExcelUtil, JavaScriptExecutorUtil
│
└── test/
    ├── java/
    │   ├── base/        BaseTest                      — setup/teardown, shared page fields
    │   └── test/        LoginPageTest, HomePageTest, LogoutPageTest,
    │                    ProductInfoPageTest, RegistrationPageTest
    └── resources/
        ├── config/      config.properties (prod), qa/uat/stage.properties
        ├── runner/      testng_regression.xml, chrome_regression.xml, firefox_regression.xml
        └── testdata/    openCartTestdata.xlsx, users.xlsx
```

---

## Prerequisites

- Java 11 (JDK)
- Maven 3.6+
- Chrome / Firefox / Edge browsers installed
- Allure CLI (for reports): `brew install allure`

---

## Running Tests

```bash
# Full regression suite (parallel: Chrome + Firefox + Edge)
mvn clean test

# Target a specific environment
mvn clean test -Denv=qa
mvn clean test -Denv=uat
mvn clean test -Denv=stage
# (no -Denv flag uses config.properties — prod credentials)

# Run a single test class
mvn clean test -Dtest=LoginPageTest
mvn clean test -Dtest=HomePageTest
mvn clean test -Dtest=ProductInfoPageTest
mvn clean test -Dtest=RegistrationPageTest
mvn clean test -Dtest=LogoutPageTest

# Run with a specific browser suite
mvn clean test -DsuiteXmlFile=src/test/resources/runner/chrome_regression.xml
mvn clean test -DsuiteXmlFile=src/test/resources/runner/firefox_regression.xml

# Build without running tests
mvn clean install -DskipTests
```

---

## Generating Reports

```bash
# Allure — interactive report (opens in browser)
allure serve allure-results

# ChainTest report is generated automatically at target/chaintest/
```

---

## Architecture

### ThreadLocal WebDriver
`DriverFactory.driverThreadLocal` stores one `WebDriver` per thread, enabling safe parallel execution. Always access via `DriverFactory.getDriver()`.

### Page Object Model
Every page class:
- Takes `WebDriver` in its constructor
- Holds an `ElementUtil` instance
- Declares all locators as `private final By` fields
- Returns the destination page object from navigation methods (e.g. `LoginPage.doLogin()` → `HomePage`)

### ElementUtil
The canonical Selenium wrapper. Use `waitForElementVisible`, `waitForElementsToBePresent`, `doSendKeys` (clears before typing), and `isElementDisplayed` instead of raw driver calls.

### Retry Mechanism
`AnnotationTransformer` applies `Retry` (IRetryAnalyzer) to every test automatically. Configure the retry count in `Retry.java`.

### Environment Configuration
Switch environments with `-Denv=<name>`. Available configs: `config.properties` (default/prod), `qa.properties`, `uat.properties`, `stage.properties`.

---

## Adding a New Page

1. Create `src/main/java/pages/NewPage.java` — constructor takes `WebDriver`, instantiates `ElementUtil`, declares `private final By` locators.
2. Add `protected NewPage newPage;` to `BaseTest`.
3. Initialize it in `BaseTest.setup()` or return it from the preceding page's navigation method.

## Adding a New Test Class

1. Extend `BaseTest`.
2. Add Allure annotations: `@Epic`, `@Story`, `@Feature`.
3. Use `@BeforeClass` for login/navigation setup.
4. Register the class in the appropriate runner XML under a `<test>` block with `<parameter name="browser">`.

---

## Test Priority Conventions

| Scenario | Priority |
|---|---|
| Negative / pre-navigation tests | `-2`, `-1` |
| Default stateless assertions | `0` |
| Destructive / logout tests | `Integer.MAX_VALUE` |

---

## Claude Code Hooks

This repo includes `.claude/settings.json` which automatically runs `mvn compile` before and after every file edit when working with Claude Code — catching Java compilation errors immediately.
