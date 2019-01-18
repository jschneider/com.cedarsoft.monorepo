package com.cedarsoft.asteroids


fun calculateVelocities(movingThing: MovingThing, velocityIncrement: Double, maxSpeed: Double) {

  val deltaSpeedX: Double
  val deltaSpeedY: Double

  val rotation = movingThing.entity.rotation

  when {
    rotation > 0 && rotation <= 90    -> {
      val rotationRadians = Math.toRadians(rotation)
      deltaSpeedX = Math.sin(rotationRadians) * velocityIncrement
      deltaSpeedY = -Math.cos(rotationRadians) * velocityIncrement
    }
    rotation > 90 && rotation <= 180  -> {
      val rotationRadians = Math.toRadians(rotation - 90)
      deltaSpeedX = Math.cos(rotationRadians) * velocityIncrement
      deltaSpeedY = Math.sin(rotationRadians) * velocityIncrement
    }
    rotation > 180 && rotation <= 270 -> {
      val rotationRadians = Math.toRadians(rotation - 180)
      deltaSpeedX = -Math.sin(rotationRadians) * velocityIncrement
      deltaSpeedY = Math.cos(rotationRadians) * velocityIncrement
    }
    else                              -> {
      // rotation > 270
      val rotationRadians = Math.toRadians(rotation - 270)
      deltaSpeedX = -Math.cos(rotationRadians) * velocityIncrement
      deltaSpeedY = -Math.sin(rotationRadians) * velocityIncrement
    }
  }

  val speedX = movingThing.speedX.get()
  val newSpeedX = speedX + deltaSpeedX

  if (Math.abs(newSpeedX) > maxSpeed) {
    if (newSpeedX < 0) {
      movingThing.speedX.set(-maxSpeed)
    } else {
      movingThing.speedX.set(maxSpeed)
    }
  } else {
    movingThing.speedX.set(newSpeedX)
  }

  val speedY = movingThing.speedY.get()
  val newSpeedY = speedY + deltaSpeedY

  if (Math.abs(newSpeedY) > maxSpeed) {
    if (newSpeedY < 0) {
      movingThing.speedY.set(-maxSpeed)
    } else {
      movingThing.speedY.set(maxSpeed)
    }
  } else {
    movingThing.speedY.set(newSpeedY)
  }
}
