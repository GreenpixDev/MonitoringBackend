package ru.greenpix.monitoring.exception

open class PublishLimitException(val seconds: Long) : Exception() {
}