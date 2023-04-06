package dashboard.GUI

import scalafx.scene.chart.{BarChart, CategoryAxis, NumberAxis, XYChart}
import scalafx.scene.Node
import javafx.scene.chart
import scalafx.scene.chart.Axis
import scalafx.scene.layout.*
import scalafx.collections.ObservableBuffer

class ColumnChart(val dataSet: Seq[Seq[Double]], var dataSetx: ObservableBuffer[String], val yTitle: String)extends Components{

  val xAxis: Axis[String] = CategoryAxis(dataSetx)
  val yAxis: Axis[Number] = NumberAxis("Rain", 0.0, 5.0, 10.0)

  val newData = ObservableBuffer[chart.XYChart.Series[String, Number]]()
  def parser(datay: Seq[Double]): ObservableBuffer[chart.XYChart.Data[String, Number]] =
    ObservableBuffer.from( (dataSetx zip datay).map(xy => XYChart.Data(xy._1, xy._2) ) )
  val series = dataSet.map(d => XYChart.Series("", parser(d)) )
  val nChart: BarChart[String, Number] = new BarChart[String, Number](xAxis, yAxis) {
    data() = ObservableBuffer(series: _*)
    categoryGap = 10.0d
  }
  private var visible = true
  def toggleVisibility() : Unit = {
    visible = !visible
    component.visible = visible
  }
  override def component: Pane = new VBox{
    spacing = 10
    children = Seq(nChart)
  }



}