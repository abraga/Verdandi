package com.github.abraga.verdandi.api.model.relative

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class VerdandiRelativeTest {

    @Test
    fun `Year singleton should have value 1`() {
        val expectedValue = 1

        assertEquals(expectedValue, Year().value, "Year singleton should have value 1.")
    }

    @Test
    fun `Year resolve should return singular label`() {
        val resolved = Year().resolve()

        assertNotNull(resolved, "Year resolve should return a non-null string.")
        assertEquals(true, resolved.isNotEmpty(), "Year resolve should return a non-empty string.")
    }

    @Test
    fun `Month singleton should have value 1`() {
        val expectedValue = 1

        assertEquals(expectedValue, Month().value, "Month singleton should have value 1.")
    }

    @Test
    fun `Week singleton should have value 1`() {
        val expectedValue = 1

        assertEquals(expectedValue, Week().value, "Week singleton should have value 1.")
    }

    @Test
    fun `Day singleton should have value 1`() {
        val expectedValue = 1

        assertEquals(expectedValue, Day().value, "Day singleton should have value 1.")
    }

    @Test
    fun `Hour singleton should have value 1`() {
        val expectedValue = 1

        assertEquals(expectedValue, Hour().value, "Hour singleton should have value 1.")
    }

    @Test
    fun `Minute singleton should have value 1`() {
        val expectedValue = 1

        assertEquals(expectedValue, Minute().value, "Minute singleton should have value 1.")
    }

    @Test
    fun `Second singleton should have value 1`() {
        val expectedValue = 1

        assertEquals(expectedValue, Second().value, "Second singleton should have value 1.")
    }

    @Test
    fun `Years should store provided value`() {
        val inputValue = 5
        val years = Years(inputValue)

        assertEquals(inputValue, years.value, "Years should have value $inputValue.")
    }

    @Test
    fun `Months should store provided value`() {
        val inputValue = 3
        val months = Months(inputValue)

        assertEquals(inputValue, months.value, "Months should have value $inputValue.")
    }

    @Test
    fun `Weeks should store provided value`() {
        val inputValue = 2
        val weeks = Weeks(inputValue)

        assertEquals(inputValue, weeks.value, "Weeks should have value $inputValue.")
    }

    @Test
    fun `Days should store provided value`() {
        val inputValue = 10
        val days = Days(inputValue)

        assertEquals(inputValue, days.value, "Days should have value $inputValue.")
    }

    @Test
    fun `Hours should store provided value`() {
        val inputValue = 24
        val hours = Hours(inputValue)

        assertEquals(inputValue, hours.value, "Hours should have value $inputValue.")
    }

    @Test
    fun `Minutes should store provided value`() {
        val inputValue = 45
        val minutes = Minutes(inputValue)

        assertEquals(inputValue, minutes.value, "Minutes should have value $inputValue.")
    }

    @Test
    fun `Seconds should store provided value`() {
        val inputValue = 30
        val seconds = Seconds(inputValue)

        assertEquals(inputValue, seconds.value, "Seconds should have value $inputValue.")
    }

    @Test
    fun `Years resolve should return plural label`() {
        val years = Years(5)
        val resolved = years.resolve()

        assertNotNull(resolved, "Years resolve should return a non-null string.")
        assertEquals(true, resolved.isNotEmpty(), "Years resolve should return a non-empty string.")
    }

    @Test
    fun `VerdandiRelativeComponents year should return null when years is zero`() {
        val components = VerdandiRelativeComponents(
            years = 0,
            months = 1,
            weeks = 0,
            days = 0,
            hours = 0,
            minutes = 0,
            seconds = 0
        )

        assertNull(components.year, "Year should be null when years is 0.")
    }

    @Test
    fun `VerdandiRelativeComponents year should return Year for single year`() {
        val components = VerdandiRelativeComponents(
            years = 1,
            months = 0,
            weeks = 0,
            days = 0,
            hours = 0,
            minutes = 0,
            seconds = 0
        )

        assertEquals(Year(), components.year, "Year should return Year singleton for value 1.")
    }

    @Test
    fun `VerdandiRelativeComponents year should return Years for multiple years`() {
        val inputYears = 5
        val components = VerdandiRelativeComponents(
            years = inputYears,
            months = 0,
            weeks = 0,
            days = 0,
            hours = 0,
            minutes = 0,
            seconds = 0
        )

        val year = components.year
        assertNotNull(year, "Year should not be null for years > 0.")
        assertEquals(inputYears, year.value, "Year value should be $inputYears.")
    }

    @Test
    fun `VerdandiRelativeComponents dominant should return first non-null unit`() {
        val components = VerdandiRelativeComponents(
            years = 0,
            months = 2,
            weeks = 1,
            days = 5,
            hours = 0,
            minutes = 0,
            seconds = 0
        )

        val dominant = components.dominant

        assertEquals(2, dominant.value, "Dominant should be months with value 2.")
    }

    @Test
    fun `VerdandiRelativeComponents toList should return all non-null units`() {
        val components = VerdandiRelativeComponents(
            years = 1,
            months = 2,
            weeks = 0,
            days = 3,
            hours = 0,
            minutes = 5,
            seconds = 0
        )

        val list = components.toList()

        assertEquals(4, list.size, "List should contain 4 non-null units.")
    }

    @Test
    fun `VerdandiRelativeComponents toList should return empty-ish list when only one unit`() {
        val components = VerdandiRelativeComponents(
            years = 0,
            months = 0,
            weeks = 0,
            days = 0,
            hours = 0,
            minutes = 0,
            seconds = 30
        )

        val list = components.toList()

        assertEquals(1, list.size, "List should contain 1 unit.")
        assertEquals(30, list.first().value, "The unit should be seconds with value 30.")
    }
}

