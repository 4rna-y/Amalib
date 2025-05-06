package net.arnay.amalib.configuration

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfigEntry(val propertyName: String)
