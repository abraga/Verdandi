package com.github.abraga.verdandi.compose

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.SaverScope
import com.github.abraga.verdandi.Verdandi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SaverTest {

    private val saverScope = SaverScope { true }

    @Test
    fun `VerdandiMomentSaver should round-trip epoch`() {
        val original = Verdandi.from(1750000000000L)

        val saved = with(VerdandiMomentSaver) { saverScope.save(original) }
        assertNotNull(saved)

        val restored = VerdandiMomentSaver.restore(saved)
        assertNotNull(restored)
        assertEquals(original.inMilliseconds, restored.inMilliseconds)
    }

    @Test
    fun `VerdandiIntervalSaver should round-trip start and end`() {
        val start = Verdandi.from(1750000000000L)
        val end = Verdandi.from(1750086400000L)
        val original = start..end

        val saved = with(VerdandiIntervalSaver) { saverScope.save(original) }
        assertNotNull(saved)

        val restored = VerdandiIntervalSaver.restore(saved)
        assertNotNull(restored)
        assertEquals(original.start.inMilliseconds, restored.start.inMilliseconds)
        assertEquals(original.end.inMilliseconds, restored.end.inMilliseconds)
    }

    @Test
    fun `MutableVerdandiMomentStateSaver should round-trip mutable state`() {
        val original = mutableStateOf(Verdandi.from(1750000000000L))

        val saved = with(MutableVerdandiMomentStateSaver) { saverScope.save(original) }
        assertNotNull(saved)

        val restored = MutableVerdandiMomentStateSaver.restore(saved)
        assertNotNull(restored)
        assertEquals(original.value.inMilliseconds, restored.value.inMilliseconds)
    }
}
