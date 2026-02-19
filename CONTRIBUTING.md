# Contributing to Verdandi

Thank you for taking the time to contribute! This document covers everything you need to get started.

---

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Development Workflow](#development-workflow)
- [Running Tests](#running-tests)
- [Code Style](#code-style)
- [Submitting a Pull Request](#submitting-a-pull-request)
- [Reporting Bugs](#reporting-bugs)
- [Requesting Features](#requesting-features)

---

## Code of Conduct

Be respectful, constructive, and welcoming. We follow the [Contributor Covenant](https://www.contributor-covenant.org/version/2/1/code_of_conduct/).

---

## Getting Started

### Prerequisites

| Tool           | Version                      |
|----------------|------------------------------|
| JDK            | 21+                          |
| Android Studio | Hedgehog (2023.1.1) or later |
| Xcode          | 15+ (for iOS/macOS targets)  |
| Kotlin         | 2.3.x (managed by Gradle)    |

### Cloning

```bash
git clone https://github.com/abraga/verdandi.git
cd verdandi
```

Open the project in Android Studio. Gradle will sync automatically.

---

## Project Structure

```
verdandi/
├── src/
│   ├── commonMain/        # Shared source code
│   │   └── kotlin/
│   │       └── com/github/abraga/verdandi/
│   │           ├── api/       # Public API — stable, versioned
│   │           └── internal/  # Implementation — may change between releases
│   ├── commonTest/        # Tests that run on all platforms
│   ├── androidMain/       # Android-specific implementations
│   ├── jvmMain/           # JVM-specific implementations
│   ├── appleMain/         # iOS + macOS shared implementations (Kotlin/Native)
│   ├── linuxMain/         # Linux-specific implementations (Kotlin/Native)
│   └── wasmJsMain/        # WebAssembly/JS implementations
│
├── compose/               # Optional Compose Multiplatform integration module
├── sampleApp/             # Compose Multiplatform demo app
├── sampleAndroidApp/      # Android-only demo app
│
├── build.gradle.kts       # Main build configuration
├── gradle/
│   └── libs.versions.toml # Version catalog
└── gradle.properties      # Gradle and JVM options
```

> **Rule:** Public API lives under `api/`. Internal implementation lives under `internal/` and may change without notice between releases. When in doubt, keep it internal.

---

## Development Workflow

### Branching

| Branch         | Purpose                      |
|----------------|------------------------------|
| `main`         | Stable, always releasable    |
| `feat/<name>`  | New features                 |
| `fix/<name>`   | Bug fixes                    |
| `chore/<name>` | Build, CI, docs, refactoring |

Always branch off from `main`:

```bash
git checkout main
git pull
git checkout -b feat/my-feature
```

### Commit Messages

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: add support for bi-weekly recurrence
fix: correct zero-padding in VerdandiMoment.toString()
docs: add timezone conversion example to README
chore: upgrade Kotlin to 2.3.10
test: add DST fall-back edge case to DstTest
```

- `feat` — new public API
- `fix` — bug fix
- `docs` — documentation only
- `test` — tests only
- `chore` — build, CI, dependencies, refactoring (no behavior change)
- `break` — breaking change (requires discussion via issue first)

---

## Running Tests

### All tests on JVM (fastest feedback loop)

```bash
./gradlew jvmTest
```

### All platforms (requires Linux)

```bash
./gradlew jvmTest linuxX64Test
```

### Apple targets (requires macOS + Xcode)

```bash
./gradlew iosSimulatorArm64Test macosArm64Test
```

### Single test class

```bash
./gradlew jvmTest --tests "io.github.abraga.verdandi.RecurrenceTest"
```

### Full build

```bash
./gradlew assemble
```

> **Tip:** CI runs JVM + Linux on Ubuntu and iOS + macOS on macOS. You don't need to test every target locally. Just JVM is enough for most changes.

---

## Code Style

Verdandi follows the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html) with a few project-specific rules:

### Formatting

- Indent: 4 spaces (no tabs)
- Max line length: 120 characters
- `kotlin.code.style=official` is set in `gradle.properties` — Android Studio will apply it automatically

### Exceptions

Use the typed exception helpers from `ExceptionExtensions.kt`:

```kotlin
// Good
verdandiRequireValidation(day in 1..31) { "Day out of range: $day" }
verdandiInputError("Unexpected input: $value")

// Avoid
throw IllegalArgumentException("...")
require(...) { "..." }
```

Exception hierarchy:

| Type                             | Use when                                           |
|----------------------------------|----------------------------------------------------|
| `VerdandiValidationException`    | A value is outside valid range (e.g. month 13)     |
| `VerdandiParseException`         | A string could not be parsed                       |
| `VerdandiInputException`         | Caller passed semantically invalid input           |
| `VerdandiStateException`         | Internal state is inconsistent (should not happen) |
| `VerdandiConfigurationException` | Library misconfiguration at setup time             |

### Documentation

- All public classes and functions must have KDoc
- Include at least one code example in the KDoc of public API types
- Reference related types with `[TypeName]`
- Document `@throws` for every exception the function can throw

### Tests

- Name test functions with backtick strings: `` `every week on mondays should adjust to monday` ``
- One assertion per test where possible
- Use explicit assertion messages: `assertEquals(expected, actual, "explanation")`
- Always test both the happy path and at least one error/edge case

---

## Submitting a Pull Request

1. **Open an issue first** for anything beyond a trivial fix, so we can align on the approach before you invest time coding.

2. **Fork** the repository and create your branch from `main`.

3. **Write tests** for every behavior change or new feature.

4. **Update the CHANGELOG.md** under `[Unreleased]` following the existing format.

5. **Open a PR** against `main` using the PR template. Fill in all sections.

6. **One reviewer approval** is required before merging.

> PRs that break existing tests, miss KDoc on new public API, or change public API without prior discussion will be asked for revisions.

---

## Reporting Bugs

Use the [Bug Report](.github/ISSUE_TEMPLATE/bug_report.md) template. The most important thing to include is a **minimal reproducible example** — a short Kotlin snippet that demonstrates the unexpected behavior.

---

## Requesting Features

Use the [Feature Request](.github/ISSUE_TEMPLATE/feature_request.md) template. Sketch the API you have in mind — even a rough draft helps a lot.

---

## License

By contributing, you agree that your contributions will be licensed under the [Apache License 2.0](LICENSE).
