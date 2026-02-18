# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.1] - 2026-02-13

### Added

- `Verdandi` singleton entry point with factory methods: `now()`, `at()`, `from()`, `fromSeconds()`, `parse()`
- `VerdandiMoment` — immutable, serializable date/time wrapper over epoch milliseconds
- Natural-language adjustment DSL with compile-time grammatical number enforcement (`one day`, `two hours`)
- Alignment operations: `startOf day`, `endOf month`, `startOf week` (configurable week start)
- Format DSL (`yyyy/MM/dd T HH.mm`) and pattern-string formatting
- Operator overloads: `+`/`-` with `Duration` and `DateDuration`, `..` for intervals
- Comparison helpers: `isBefore`, `isAfter`, `isBetween`, `isSameDayAs`, `isToday`, `isTomorrow`, `isWeekend`
- `VerdandiInterval` — half-open range `[start, end)` with `contains`, `overlaps`, `intersection`, `union`
- Interval factory DSL: `last thirty days`, `next two weeks`
- Interval adjustment DSL: `expandBoth`, `shiftBoth`, `shiftEnd`, `alignToFullDays`
- `VerdandiRecurrenceMoments` — recurring moment generation with `every`, `at`, `until`, `on`, `indefinitely`
- Recurrence operations: `matches` (O(1)), `exclude`, `filter`, `format` chaining
- Relative time formatting with `relativeTo` and `maxUnits` granularity control
- Timezone conversion via `inTimeZone` with `VerdandiTimeZone`
- Lazy, cached component decomposition (`year`, `month`, `day`, `dayOfWeek`, `quarter`)
- `DateDuration` and `DateTimeDuration` for calendar-aware arithmetic
- `kotlinx.serialization` support for `VerdandiMoment`, `VerdandiInterval`, `VerdandiRecurrenceMoments`
- Global configuration via `Verdandi.configure { }` (default timezone, week start)
- Compose integration module (`:compose`):
  - `rememberCurrentMoment` — live clock with configurable refresh interval
  - `rememberMoment`, `rememberSavableMoment` — stable/saveable moment state
  - `rememberMutableMomentState` — two-way binding for date pickers
  - `rememberAdjustedMoment` — derived moment recomputed on source change
  - `rememberInterval`, `rememberSavableInterval` — interval state helpers
  - `LocalVerdandiMoment` — CompositionLocal provider
- KMP targets: Android (minSdk 26), iOS (x64, arm64, simulatorArm64), macOS (x64, arm64), JVM, WasmJs, Linux (x64, arm64)
