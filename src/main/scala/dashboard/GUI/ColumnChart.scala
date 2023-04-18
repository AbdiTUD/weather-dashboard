package dashboard.GUI

import scalafx.scene.chart.{BarChart, CategoryAxis, NumberAxis, XYChart}
import scalafx.scene.layout._
import scalafx.collections.ObservableBuffer

import scalafx.Includes.observableList2ObservableBuffer
import scalafx.collections.CollectionIncludes.observableList2ObservableBuffer
import scalafx.scene.control.Tooltip

class ColumnChart(val dataSet: Seq[Seq[Double]], var dataSetx: ObservableBuffer[String], val yTitle: String) extends Components {

  val xAxis: CategoryAxis = CategoryAxis(dataSetx)
  val yAxis: NumberAxis = NumberAxis(yTitle, 0.0, 5.0, 10.0)

  def parser(datay: Seq[Double]) = ObservableBuffer.from(dataSetx zip datay).map(xy => XYChart.Data[String, Number](xy._1, xy._2))

  private var series = dataSet.map(d => XYChart.Series("", parser(d)) )
  var d: Seq[javafx.scene.chart.XYChart.Series[String, Number]] =
    Seq[javafx.scene.chart.XYChart.Series[String, Number]]() ++ series


  private var visible = true
  def toggleVisibility(): Unit = {
    visible = !visible
    component.visible = visible
  }

  override def component: Pane = new Pane {
    children = new BarChart[String, Number](xAxis, yAxis) {
      data() ++= d
      d.foreach{XY => XY.getData.foreach{d=>
        val showData = new Tooltip()
        showData.setText(s"${d.getXValue}: ${d.getYValue}")
        Tooltip.install(d.getNode,showData)
        d.getNode.setOnMouseEntered(_ => d.getNode.getStyleClass.add("Highlight"))
        d.getNode.setOnMouseExited(_ => d.getNode.getStyleClass.removeAll("Highlight"))
      }
      }

      categoryGap = 10.0d
    }
  }

  def duplicateChart(columnChart: ColumnChart): ColumnChart = {
    val newData = columnChart.dataSet.map(d => XYChart.Series("", columnChart.parser(d)))
    val newD = Seq[javafx.scene.chart.XYChart.Series[String, Number]]() ++ newData

    new ColumnChart(columnChart.dataSet, columnChart.dataSetx, columnChart.yTitle) {
      d = newD

  }
}

}