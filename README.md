![img_logo.png](.github/img_logo.png)

# Verdandi

[![Kotlin](https://img.shields.io/badge/Kotlin-Multiplatform-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/docs/multiplatform.html)
[![Android](https://img.shields.io/badge/Android-✓-34A853)](#)
[![Apple](https://img.shields.io/badge/Apple-✓-ffffff)](#)
[![JVM](https://img.shields.io/badge/JVM-✓-853241)](#)
[![Linux](https://img.shields.io/badge/Linux-✓-FCC624)](#)
[![Wasm/JS](https://img.shields.io/badge/Wasm%2FJS-✓-778FFE)](#)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

A date & time DSL for KMP (Kotlin Multiplatform). Type-safe, immutable, reads like plain English.

## Quickstart

```kotlin
val now = Verdandi.now()
// Result: 2026-02-13T21:00:00:+00:00 (timezone from system default)

val tomorrow = now adjust { add one day }
// Result: 2026-02-14T21:00:00:+00:00
// Also: now + 1.days

val formatted = tomorrow format { yyyy/MM/dd..HH.mm }
// Result: 2026/02/14 21:00
// Also: tomorrow format "yyyy/MM/dd HH:mm"

val interval = now..tomorrow
// Result: [2026-02-13T21:00:00:+00:00, 2026-02-14T21:00:00:+00:00)

val lastThirtyDays = Verdandi.interval { last thirty days from now }
// Result: [2026-01-14T21:00:00:+00:00, 2026-02-13T21:00:00:+00:00)

val weekdays = Verdandi.recurrence(now) { every weekdays until(birthday) }
// Result: 2026-02-16, 2026-02-17, 2026-02-18, 2026-02-19...

val relative = now.relativeTo(event) format {
    onPast { "$it ago" }
    onNow { "right now" }
    onFuture { "in $it" }
}
// Result (example): "1 day, 4 hours and 26 minutes ago"

```

---

## Installation

```kotlin
dependencies {
    implementation("com.github.abraga:verdandi:0.1.1")

    // Or for Compose projects:
    implementation("com.github.abraga:verdandi-compose:0.1.1")
}
```

---

## API Reference

<details>
<summary><b>Creating moments</b></summary>

A `VerdandiMoment` wraps epoch milliseconds with optional timezone context.

```kotlin
Verdandi.now()                                          // current instant
Verdandi()                                              // shorthand
Verdandi.at(2026, 6, 15, 14, 30)                        // from components
Verdandi.from(1750000000000L)                           // from epoch ms
Verdandi.fromSeconds(1750000000L)                       // from epoch seconds
Verdandi.from("2026-06-15T14:30:00Z")                   // from ISO-8601
Verdandi.parse("15/06/2026 14:30", "dd/MM/yyyy HH:mm")  // custom pattern
```

</details>

<details>
<summary><b>Adjusting</b></summary>

Every operation returns a new immutable instance. Grammatical number is enforced at compile time: `one day` compiles, `one days` does not.

```kotlin
now adjust { subtract two days }

now adjust {
    add five hours
    subtract thirtyFour minutes
}

// Alignment
now adjust { at startOf day }

now adjust { at endOf month }

now adjust {
    weekStartsOn = Sunday
    at startOf week
}

// Direct assignment
now adjust { atYear(2026); atMonth(12); atDay(31) }
```

</details>

<details>
<summary><b>Formatting</b></summary>

```kotlin
moment format { yyyy/MM/dd T HH.mm }       // "2026/02/11 21:24"
moment format("yy-MM-dd")                  // "26-02-13"
moment format "EEEE, MMMM dd, yyyy"        // "Sunday, June 15, 2026"
```

| Directive             | Description                   | Example                  |
|-----------------------|-------------------------------|--------------------------|
| `yyyy` / `yy`         | Year (4 / 2 digits)           | `2026` / `26`            |
| `MMMM` / `MMM` / `MM` | Month (full / abbr / padded)  | `January` / `Jan` / `01` |
| `dd` / `d`            | Day (padded / variable)       | `09` / `9`               |
| `EEEE` / `EEE`        | Weekday (full / abbr)         | `Monday` / `Mon`         |
| `HH` / `hh`           | Hour (0-23 / 1-12)            | `14` / `02`              |
| `mm` / `ss` / `SSS`   | Minute / Second / Millisecond | `05` / `09` / `456`      |
| `a`                   | AM/PM                         | `PM`                     |
| `Q`                   | Quarter                       | `2`                      |
| `Z`                   | UTC offset                    | `+09:00` / `Z`           |

Escape literals with single quotes: `'at'` → **at**

</details>

<details>
<summary><b>Operators & comparisons</b></summary>

```kotlin
moment + 2.hours              // kotlin.time.Duration
moment - 3.days
moment + 1.months             // DateDuration (calendar-aware)
moment1..moment2              // VerdandiInterval

tomorrow.isTomorrow()
tomorrow.isToday()
tomorrow.isWeekend()
tomorrow isSameDayAs now

moment1 isBefore moment2
moment1 isAfter moment2
moment.isBetween(start, end)
```

</details>

<details>
<summary><b>Relative time</b></summary>

```kotlin
val relative = now.relativeTo(adjusted) format {
    onPast { "$it ago" }
    onNow { "right now" }
    onFuture { "in $it" }
}

println(relative) // "1 day, 4 hours and 26 minutes ago"

// Control granularity
val short = now.relativeTo(adjusted) format {
    maxUnits = 1
    onPast { "$it ago" }
    onNow { "right now" }
    onFuture { "in $it" }
}

println(short) // "1 day ago"
```

</details>

<details>
<summary><b>Timezone conversion</b></summary>

The underlying instant stays the same — only decomposed components change.

```kotlin
val utc = Verdandi.from("2026-06-15T12:00:00Z")
val tokyo = utc inTimeZone VerdandiTimeZone.of("Asia/Tokyo")

utc.inMilliseconds == tokyo.inMilliseconds   // true
tokyo.component.hour                         // 21
```

</details>

<details>
<summary><b>Intervals</b></summary>

A `VerdandiInterval` is a half-open range `[start, end)`.

```kotlin
val interval = today..tomorrow
Verdandi.interval { last thirty days }
Verdandi.interval { next two weeks }

interval.contains(moment)
interval.overlaps(other)
interval.intersection(other)
interval.union(other)
interval.duration()

val expanded = interval adjust { expandBoth(2.days) }

val aligned = interval adjust {
    shiftBoth(2.hours)
    shiftEnd(30.minutes)
    alignToFullDays()
}
```

</details>

<details>
<summary><b>Recurrence</b></summary>

`VerdandiRecurrenceMoments` implements `List<VerdandiMoment>`.

```kotlin
Verdandi.recurrence(today) {
    every weekdays at { 9.hours } until deadline
}

Verdandi.recurrence(start, limit = 10) { every day indefinitely }

// Filter + format chain
Verdandi.recurrence(lastMonth) {
    every day on fridays until(deadline)
} filter { it.component.day.value == 13 } format { MM-dd }
// Result: will return all Friday the 13ths from the last month until the deadline

// O(1) matching
recurrence.matches(someMoment)

// Exclusions
recurrence.exclude(holiday)
recurrence.exclude(listOf(holiday1, holiday2))
```

</details>

<details>
<summary><b>Components</b></summary>

```kotlin
val component = moment.component
component.year.value        // 2026
component.month.value       // 6
component.day.value         // 15
component.dayOfWeek.value   // 7 (ISO: 1 = Mon, 7 = Sun)
component.quarter.value     // 2
```

> [!TIP]
> Components are lazily computed and cached on first access.

</details>

---

## Compose Integration

<details>
<summary><b>Available helpers</b></summary>

```kotlin
// Live clock
val now by rememberCurrentMoment(refreshInterval = 1.seconds)
Text(now format { HH.mm.ss })

// Stable across recompositions
val snapshot = rememberMoment()
val birthday = rememberMoment(year = 1990, month = 5, day = 20)

// Survives configuration changes
val saved = rememberSavableMoment()

// Two-way binding
val (moment, setMoment) = rememberMutableMomentState()
Button(
    onClick = {
        setMoment(moment adjust { add one day })
    }
) {
    Text("Next day")
}

// Re-evaluated when source changes
val startOfDay = rememberAdjustedMoment(selectedDate) { at startOf day }

// Intervals
val interval = rememberInterval(startMoment, endMoment)
val saveable = rememberSavableInterval(startMoment, endMoment)

// Composition local
CompositionLocalProvider(LocalVerdandiMoment provides live) {
    Text(LocalVerdandiMoment.current.toString())
}
```

</details>

---

## Configuration

```kotlin
Verdandi.config.configure {
    defaultTimeZone = VerdandiTimeZone.UTC
    defaultWeekStart = Sunday
}
```

> [!IMPORTANT]
> Call once at application startup. Thread-safe, but affects all subsequent calls.

## Serialization

`VerdandiMoment`, `VerdandiInterval`, and `VerdandiRecurrenceMoments` support `kotlinx.serialization`.

```json
{ "epoch": 1750000000000, "timeZoneId": "America/Sao_Paulo" }
```

## Architecture

```
verdandi/
├── api/        # Public API — stable, versioned
└── internal/   # Implementation — may change between releases
```

Immutable operations, sealed/inline value classes for type safety, `@VerdandiDslMarker` for DSL isolation, and `expect`/`actual` for platform-specific timezone resolution.

## License

Apache 2.0 — see [LICENSE](LICENSE) for details.
