package dashboard.GUI
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import dashboard.GUI.Components
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import scalafx.scene.layout.*
import scalafx.Includes._


import scala.collection.mutable.Buffer
import scalafx.collections.ObservableBuffer

class ScatterPlot(dataSet: Seq[Number]) extends Components(dataSet) {
  var series = new XYChart.Series[Number,Number]{
    name = "scatter"
    data() ++= dataSet.map{case (x: Double,y: Double) => XYChart.Data[Number, Number](x,y)}
  }
  new LineChart(NumberAxis(), NumberAxis()){
    data() += series
  }
}

/*
class ScatterPlot(data: Seq[Number], title: String) extends Components(data) {
  val xAxis = NumberAxis(0.0, 10.0, 1.0)
  val yAxis = NumberAxis(0.0, 10.0, 1.0)
  val scatter = new ScatterChart(xAxis, yAxis)
  val setTitle = scatter.setTitle(title)

  def build() =
    val chartData = new XYChart.Series()
    val newData = parserData(data)
}

  def parserData(data: Seq[(Double, Double)]) = XYChart.Series[Number, Number](ObservableBuffer.from(data.map(
    (x, y) => XYChart.Data[Number,Number](x,y))))

object Main extends JFXApp3 {
  val data = Seq((1.0, 2.0),(2.0, 3.0),(3.0, 4.0))


  def start(): Unit =
    val numberData = data.map(x => parserData(x))
    val plot = new ScatterPlot(numberData, "bob")
    plot.build()

    stage = new JFXApp3.PrimaryStage {
      title = "Scatter Plot"
      scene = new Scene {
      content = plot.scatter
    }
  }
}*/