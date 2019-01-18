package com.cedarsoft.asteroids

import java.util.concurrent.TimeUnit

const val gameWidth = 1280.0
const val gameHeight = 960.0

// timing
val updateInterval = TimeUnit.MILLISECONDS.toNanos(5)
val missileCadence = TimeUnit.MILLISECONDS.toNanos(100)

// player
const val maxSpeedPlayer = 8.0
const val acceleration = 0.2
const val rotationSpeed = 4.0

// missiles
const val maxSpeedMissile = 32.0
const val missileTravelDistance = 1000
const val missileWrap = false
