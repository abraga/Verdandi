package com.github.abraga.verdandi.api.model.relative

import com.github.abraga.verdandi.internal.extension.currentLocale
import com.github.abraga.verdandi.internal.factory.component.platform.VerdandiUnitLabels
import com.github.abraga.verdandi.internal.factory.component.platform.LocalizedUnitLabels
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

sealed interface VerdandiRelativeUnit {

    val value: Int

    fun resolve(): String
}

@JvmInline
@Serializable
value class Year(override val value: Int = 1) : VerdandiRelativeUnit {

    override fun resolve(): String {
        return labels().year.singular
    }
}

@JvmInline
@Serializable
value class Month(override val value: Int = 1) : VerdandiRelativeUnit {

    override fun resolve(): String {
        return labels().month.singular
    }
}

@JvmInline
@Serializable
value class Week(override val value: Int = 1) : VerdandiRelativeUnit {

    override fun resolve(): String {
        return labels().week.singular
    }
}

@JvmInline
@Serializable
value class Day(override val value: Int = 1) : VerdandiRelativeUnit {

    override fun resolve(): String {
        return labels().day.singular
    }
}

@JvmInline
@Serializable
value class Hour(override val value: Int = 1) : VerdandiRelativeUnit {

    override fun resolve(): String {
        return labels().hour.singular
    }
}

@JvmInline
@Serializable
value class Minute(override val value: Int = 1) : VerdandiRelativeUnit {

    override fun resolve(): String {
        return labels().minute.singular
    }
}

@JvmInline
@Serializable
value class Second(override val value: Int = 1) : VerdandiRelativeUnit {

    override fun resolve(): String {
        return labels().second.singular
    }
}

@JvmInline
@Serializable
value class Years(override val value: Int) : VerdandiRelativeUnit {

    override fun resolve(): String {
        return labels().year.plural
    }
}

@JvmInline
@Serializable
value class Months(override val value: Int) : VerdandiRelativeUnit {

    override fun resolve(): String {
        return labels().month.plural
    }
}

@JvmInline
@Serializable
value class Weeks(override val value: Int) : VerdandiRelativeUnit {

    override fun resolve(): String {
        return labels().week.plural
    }
}

@JvmInline
@Serializable
value class Days(override val value: Int) : VerdandiRelativeUnit {

    override fun resolve(): String {
        return labels().day.plural
    }
}

@JvmInline
@Serializable
value class Hours(override val value: Int) : VerdandiRelativeUnit {

    override fun resolve(): String {
        return labels().hour.plural
    }
}

@JvmInline
@Serializable
value class Minutes(override val value: Int) : VerdandiRelativeUnit {

    override fun resolve(): String {
        return labels().minute.plural
    }
}

@JvmInline
@Serializable
value class Seconds(override val value: Int) : VerdandiRelativeUnit {

    override fun resolve(): String {
        return labels().second.plural
    }
}

private fun labels(): LocalizedUnitLabels {
    return VerdandiUnitLabels.get(currentLocale())
}
