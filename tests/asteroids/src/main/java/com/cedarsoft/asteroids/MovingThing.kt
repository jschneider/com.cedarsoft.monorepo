package com.cedarsoft.asteroids

import com.almasb.fxgl.entity.Entity
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty

data class MovingThing(val entity: Entity) {
  var speedX : DoubleProperty = SimpleDoubleProperty(0.0)
  var speedY : DoubleProperty = SimpleDoubleProperty(0.0)
  var distanceTravelled : DoubleProperty = SimpleDoubleProperty(0.0)
}
