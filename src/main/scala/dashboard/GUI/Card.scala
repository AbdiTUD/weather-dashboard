package dashboard.GUI

import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.{Text, TextAlignment}

import scala.math.*

class Card(input: Seq[Seq[Double]],temp: Double) extends Components {

  val temperature: Double = temp
  val min: Int = input.map(_.min).min.toInt
  val max: Int = input.map(_.max).max.toInt
  val average: Int = if (input.nonEmpty) (input.map(_.sum).sum / input.length).toInt else 0
  val sum: Int = input.map(_.sum).sum.toInt
  val SD: Int = {
    val n = input.length
    val mean = average
    sqrt(input.map(x => x.map(d => pow(d - mean, 2)).sum).sum / n).toInt
  }

  override def component: Pane =
    val tempText = new Text(temperature.toString + " °C")
    tempText.style = "-fx-font-size: 24pt"

    val minText = new Text(s"min: $min mm")
    minText.style = "-fx-font-size: 9pt"

    val maxText = new Text(s"max: $max mm")
    maxText.style = "-fx-font-size: 9pt"

    val averageText = new Text(s"average: $average mm")
    averageText.style = "-fx-font-size: 9pt"

    val sumText = new Text(s"sum: $sum mm")
    sumText.style = "-fx-font-size: 9pt"

    val stdDevText = new Text(s"standard deviation: $SD mm")
    stdDevText.style = "-fx-font-size: 9pt"

    val Yheight = 350
    val Xwidth = 350

    val temperatureBox = new Rectangle {
      width = Xwidth.toDouble
      height = Yheight / 2.0
      fill = if (temperature > 0) Color.Red else Color.Blue
    }

    val infoBox = new Rectangle {
      width = Xwidth.toDouble
      height = Yheight / 2.0
      fill = Color.White
      translateY = Yheight / 2.0
    }

    new Pane {
      children = Seq(
        temperatureBox,
        tempText,
        infoBox,
        minText,
        maxText,
        averageText,
        sumText,
        stdDevText
      )

      minText.layoutX = Xwidth * 0.1
      minText.layoutY = Yheight * 0.55
      maxText.layoutX = Xwidth * 0.1
      maxText.layoutY = Yheight * 0.65
      averageText.layoutX = Xwidth * 0.1
      averageText.layoutY = Yheight * 0.75
      sumText.layoutX = Xwidth * 0.1
      sumText.layoutY = Yheight * 0.85
      stdDevText.layoutX = Xwidth * 0.1
      stdDevText.layoutY = Yheight * 0.95

      tempText.layoutX = Xwidth/2.5
      tempText.layoutY = temperatureBox.getHeight/2.0

      prefWidth = Xwidth.toDouble
      prefHeight = Yheight.toDouble

      prefWidth = Xwidth.toDouble
      prefHeight = Yheight.toDouble
    }

}
