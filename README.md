# QA Automation Framework - API + UI

A test automation framework that covers the **same application end to end**: its REST API and its web UI. Built with Java, Selenium, REST Assured, TestNG and Allure, running automatically on GitHub Actions with a live published test report.

**Application under test:** [Notes App](https://practice.expandtesting.com/notes/app/) (Expand Testing) - a web app with both a UI and a public REST API, so every feature is tested through both layers.

[![Tests](https://github.com/hanin-magdy/QA-Automation-Framework/actions/workflows/ci.yml/badge.svg)](https://github.com/hanin-magdy/QA-Automation-Framework/actions/workflows/ci.yml)

**Live test report (Allure):** https://hanin-magdy.github.io/QA-Automation-Framework/

## Tech stack

| Layer | Tools |
|-------|-------|
| UI testing | Selenium WebDriver 4, Page Object Model |
| API testing | REST Assured |
| Test runner | TestNG |
| Reporting | Allure (with screenshots on failure), published to GitHub Pages |
| CI/CD | GitHub Actions - tests run on every push |
| Build | Maven, Java 17 |

## Project structure

```
├── .github/workflows/ci.yml        # CI pipeline: test + publish Allure report
├── pom.xml
└── src/test
    ├── java/com/hanin/qa
    │   ├── api/                    # REST Assured client for the Notes API
    │   ├── base/                   # WebDriver setup/teardown
    │   ├── config/                 # config.properties loader with CLI overrides
    │   ├── listeners/              # TestNG listener: screenshot on failure -> Allure
    │   ├── pages/                  # Page Objects (Register, Login, Notes)
    │   ├── tests/
    │   │   ├── api/                # API tests: auth + notes CRUD
    │   │   └── ui/                 # UI tests: registration/login + note creation
    │   └── utils/                  # unique test-data generation
    └── resources/
        ├── config.properties       # base URLs, headless mode, timeouts
        ├── testng.xml              # suite definition (API + UI)
        └── allure.properties
```

## What is covered

**API tests** (`tests/api`)
- User registration returns 201 with the created user
- Login returns a valid auth token
- Login with a wrong password is rejected (401)
- Notes CRUD: create, list, mark completed, delete

**UI tests** (`tests/ui`)
- Register a new user through the UI, then log in
- Create a note through the UI and verify it appears in the list

Every test creates its own data and cleans up after itself (accounts and notes created during a run are deleted again).

## Run locally

Requirements: Java 17+, Maven, Chrome.

```bash
mvn test                 # runs the full suite (headless Chrome)
mvn test -Dheadless=false
mvn allure:serve         # opens the Allure report in the browser
```

Base URLs can be overridden without touching the code:

```bash
mvn test -Dbase.url.api=https://example.com/notes/api -Dbase.url.ui=https://example.com/notes/app
```

## CI/CD

Every push to `main` triggers the pipeline (`.github/workflows/ci.yml`):
1. Set up JDK 17 and Chrome on Ubuntu
2. Run the full API + UI suite
3. Generate the Allure report
4. Publish it to GitHub Pages - the link above always shows the latest run
