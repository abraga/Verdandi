# Verdandi - Consumer ProGuard Rules

# Keep all public API classes
-keep class com.github.abraga.verdandi.Verdandi { *; }
-keep class com.github.abraga.verdandi.api.** { *; }

# Keep kotlinx.serialization @Serializable classes
-keepclassmembers class com.github.abraga.verdandi.api.** {
    *** Companion;
}
-keepclasseswithmembers class com.github.abraga.verdandi.api.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep @Serializable companion serializer() for all serializable models
-if @kotlinx.serialization.Serializable class com.github.abraga.verdandi.api.**
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}
-if @kotlinx.serialization.Serializable class com.github.abraga.verdandi.api.**
-keepclassmembers class <1>$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}
