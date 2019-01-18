package com.cedarsoft.asteroids

import com.almasb.fxgl.entity.Entities
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.GameWorld

fun move(movingThing: MovingThing, wrap: Boolean) {

  val translateX = movingThing.speedX.get()
  val translateY = movingThing.speedY.get()
  val entity = movingThing.entity

  when {
    entity.position.y + translateY < 0.0        -> if (wrap) entity.setPosition(entity.x, gameHeight) else entity.removeFromWorld()
    entity.position.y + translateY > gameHeight -> if (wrap) entity.setPosition(entity.x, 0.0) else entity.removeFromWorld()
    else                                        -> entity.translateY(translateY)
  }

  when {
    entity.position.x + translateX < 0.0       -> if (wrap) entity.setPosition(gameWidth, entity.y) else entity.removeFromWorld()
    entity.position.x + translateX > gameWidth -> if (wrap) entity.setPosition(0.0, entity.y) else entity.removeFromWorld()
    else                                       -> entity.translateX(translateX)
  }

  movingThing.distanceTravelled.set(Math.sqrt(translateX * translateX + translateY * translateY))
}

fun createMissile(player: Entity, gameWorld: GameWorld): MovingThing {
  val newMissileEntity = Entities.builder()
    .at(player.x, player.y)
    .viewFromTexture("missile.png")
    .rotate(player.rotation)
    .buildAndAttach(gameWorld)

  newMissileEntity.setProperty("distanceTravelled", 0.0)
  val movingThing = MovingThing(newMissileEntity)
  calculateVelocities(movingThing, maxSpeedMissile, maxSpeedMissile)
  return movingThing
}
