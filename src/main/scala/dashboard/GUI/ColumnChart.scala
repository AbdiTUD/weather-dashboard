package dashboard.GUI

import scalafx.scene.chart.{BarChart, CategoryAxis, NumberAxis, XYChart}
import scalafx.scene.Node
import javafx.scene.chart

import scalafx.scene.layout.*
import scalafx.collections.ObservableBuffer

class ColumnChart(dataSet: Seq[Number], var dataSetx:ObservableBuffer[String], val xTitle: String, val yTitle: String)extends Components(dataSet){

  val allDate = dataSetx.zip(dataSet)
  val xAxis = dataSetx
  val yAxis = NumberAxis("", 0.0d, 10.0d, 20.0d)
  ObservableBuffer.from( (dataSetx zip dataSet).map)
  /*val xAxis = new CategoryAxis{
    label = xTitle
    categories = ObservableBuffer.from(dataSetx)

  }
  val yAxis = new NumberAxis{
    label = yTitle
    tickLabelFormatter = NumberAxis.DefaultFormatter(this, "mm", "")
  }
  for i <- dataSet.indices do
    val series =
      new XYChart.Series[String, Number]{
        name = ""
          data = XYChart.Data[String, Number](dataSetx(i), dataSet(i))
      }

*/
}

