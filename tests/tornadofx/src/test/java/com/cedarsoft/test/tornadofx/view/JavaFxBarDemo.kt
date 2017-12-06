package com.cedarsoft.test.tornadofx.view

import javafx.animation.Animation.INDEFINITE
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Side
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.PieChart
import javafx.scene.chart.XYChart
import tornadofx.*


/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
fun main(args: Array<String>) {
  launch<JavaFxBarDemo>()
}

class JavaFxBarDemo : App(MyView::class) {

}

class MyView : View() {
  override val root = borderpane() {
    center {
      vbox {
        piechart("Imported Fruits") {
          data("Grapefruit", 12.0)
          data("Oranges", 25.0)
          data("Plums", 10.0)
          data("Pears", 22.0)
          data("Apples", 30.0)
        }

        barchart("Stock Monitoring, 2010", CategoryAxis(), NumberAxis()) {

          multiseries("Portfolio 1", "Portfolio 2") {
            data("Jan", 23, 10)
            data("Feb", 14, 5)
            data("Mar", 15, 8)
          }
        }

        separator { }

        val xAxis = NumberAxis()
        val yAxis = NumberAxis()
        xAxis.label = "Number of Month"
        //creating the chart
        val lineChart = LineChart(xAxis, yAxis)

        lineChart.title = "Stock Monitoring, 2010"
        //defining a series
        val series = XYChart.Series<Number, Number>()
        series.setName("My portfolio")
        //populating the series with data
        series.getData().add(XYChart.Data(1, 23))
        series.getData().add(XYChart.Data(2, 14))
        series.getData().add(XYChart.Data(3, 15))
        series.getData().add(XYChart.Data(4, 24))
        series.getData().add(XYChart.Data(5, 34))
        series.getData().add(XYChart.Data(6, 36))
        series.getData().add(XYChart.Data(7, 22))
        series.getData().add(XYChart.Data(8, 45))
        series.getData().add(XYChart.Data(9, 43))
        series.getData().add(XYChart.Data(10, 17))
        series.getData().add(XYChart.Data(11, 29))
        series.getData().add(XYChart.Data(12, 25))

        lineChart.data.add(series)
        add(lineChart)


        val timeline = Timeline(KeyFrame(1.seconds, EventHandler {
          val data = series.data
          data.add(XYChart.Data(data.size + 1, Math.random() * 50))
        }))

        timeline.cycleCount = INDEFINITE
        timeline.play()


      }
    }
  }
}