package com.dpr.hello_market.di

/*
Retention:
SOURCE— Annotation is only valid in compile time and is removed in binary output. This is similar to Java’s SOURCE retention.
BINARY— Annotation persists in binary output but cannot be accessed via reflection.
RUNTIME — Annotation persists in binary output and can be used via reflection. This is the default retention policy.
 */

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ServiceApi