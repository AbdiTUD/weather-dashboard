package dashboard.GUI
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import dashboard.GUI.Components
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import scalafx.scene.layout.*
import scalafx.Includes._


import scala.collection.mutable.Buffer
import scalafx.collections.ObservableBuffer

class ScatterPlot(val dataSet: Seq[(Double,Double)]) extends Components{
  var series = new XYChart.Series[Number,Number]{
    name = "scatter"
    data() ++= dataSet.map{case (x: Double,y: Double) => XYChart.Data[Number, Number](x,y)}
  }
  private var visible = true
  def toggleVisibility() : Unit = {
    visible = !visible
    component.visible = visible
  }
  override def component: Pane = new Pane {
    children = new LineChart(NumberAxis(), NumberAxis()){
    data() += series
  }
  }
}
