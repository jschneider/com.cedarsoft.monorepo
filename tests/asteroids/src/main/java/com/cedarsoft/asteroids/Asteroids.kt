package com.cedarsoft.asteroids

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.entity.Entities
import com.almasb.fxgl.input.UserAction
import com.almasb.fxgl.settings.GameSettings
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.input.KeyCode
import javafx.scene.text.Text


fun main(args: Array<String>) {
  GameApplication.launch(Asteroids::class.java, *args)
}

/**
 * FXGLAsteroids
 */
class Asteroids : GameApplication() {

  private lateinit var player: MovingThing
  private val missiles: MutableList<MovingThing> = mutableListOf()
  private val numberMissiles = SimpleIntegerProperty()
  private var lastUpdate = System.nanoTime()
  private var lastMissileFired = 0L

  override fun initSettings(settings: GameSettings) {
    settings.width = gameWidth.toInt()
    settings.height = gameHeight.toInt()
    settings.title = "Asteroids"
    settings.version = "0.1"
  }


  override fun initInput() {
    val input = input

    input.addAction(object : UserAction("rotate right") {
      override fun onAction() {
        player.entity.rotateBy(rotationSpeed)
        val rotation = player.entity.rotation
        player.entity.rotation = rotation % 360
        gameState.setValue("currentRotation", rotation)
      }
    }, KeyCode.D)

    input.addAction(object : UserAction("rotate left") {
      override fun onAction() {
        player.entity.rotateBy(-rotationSpeed)
        var rotation = player.entity.rotation
        if (rotation < 0) {
          rotation += 360
        }
        player.entity.rotation = rotation % 360
        gameState.setValue("currentRotation", rotation)
      }
    }, KeyCode.A)

    input.addAction(object : UserAction("move forward") {
      override fun onAction() {
        calculateVelocities(player, acceleration, maxSpeedPlayer)
      }
    }, KeyCode.W)

    input.addAction(object : UserAction("Fire") {
      override fun onAction() {
        val currentTime = System.nanoTime()
        if ((missileCadence - (currentTime - lastMissileFired)) > 0) {
          return
        }
        lastMissileFired = currentTime
        val newMissile = createMissile(player.entity, gameWorld)
        missiles.add(newMissile)
      }
    }, KeyCode.SPACE)
  }

  override fun initGameVars(vars: MutableMap<String, Any>) {
    vars["pixelsMoved"] = 0.0
    vars["currentRotation"] = 0.0
  }

  override fun initGame() {

    val playerEntity = Entities.builder()
      .at(400.0, 300.0)
      .viewFromTexture("spaceship.png")
      .buildAndAttach(gameWorld)

    player = MovingThing(playerEntity)
  }

  override fun initUI() {
    val rotationInfo = Text()
    rotationInfo.translateX = 10.0
    rotationInfo.translateY = 20.0
    rotationInfo.textProperty().bind(gameState.doubleProperty("currentRotation").asString())

    val speedXInfo = Text()
    speedXInfo.translateX = 10.0
    speedXInfo.translateY = 35.0
    speedXInfo.textProperty().bind(player.speedX.asString())

    val speedYInfo = Text()
    speedYInfo.translateX = 10.0
    speedYInfo.translateY = 50.0
    speedYInfo.textProperty().bind(player.speedY.asString())

    val missileInfo = Text()
    missileInfo.translateX = 10.0
    missileInfo.translateY = 65.0
    missileInfo.textProperty().bind(numberMissiles.asString())

    gameScene.addUINodes(rotationInfo, speedXInfo, speedYInfo, missileInfo)
  }

  override fun onUpdate(tpf: Double) {
    val currentTime = System.nanoTime()
    val sleepTime = updateInterval - (currentTime - lastUpdate)
    if (sleepTime > 0) {
      return
    }
    lastUpdate = currentTime

    move(player, true)

    val missilesToRemove: MutableList<MovingThing> = mutableListOf()

    for (missile in missiles) {
      move(missile, missileWrap)

      // missile hit the border
      if (!missile.entity.isActive) {
        missilesToRemove.add(missile)
        continue
      }

      // check missile travel range
      if (missile.distanceTravelled.get() > missileTravelDistance) {
        missile.entity.removeFromWorld()
        missilesToRemove.add(missile)
      }
    }

    missiles.removeAll(missilesToRemove)
    numberMissiles.set(missiles.size)
  }
}
