package dashboard.GUI
import scalafx.scene.Scene
import scalafx.scene.chart.{PieChart, XYChart}
import scalafx.Includes.jfxXYChartData2sfx
import scalafx.scene.SceneIncludes.jfxXYChartData2sfx
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.Pane
import scalafx.scene.layout.*


class Pie(val dataSet: Seq[Int], var dataString: Seq[String]) extends Components{
  var sequence= Seq(PieChart.Data(dataString(0), dataSet(0).doubleValue()))
  val datas =
    for i <- 1 until dataSet.size do
      sequence :+ PieChart.Data(dataString(i), dataSet(i).doubleValue())
  /*val newPie: PieChart = new PieChart {
    data = sequence
  }*/
  private var visible = true
  def toggleVisibility() : Unit = {
    visible = !visible
    component.visible = visible
  }

  override def component: Pane = new VBox {
    spacing = 10
    children = Seq {
      new PieChart {
        data = sequence
      }
    }
  }

}