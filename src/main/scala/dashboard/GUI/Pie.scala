package dashboard.GUI


import scalafx.scene.Scene
import scalafx.scene.chart.{PieChart, XYChart}
import scalafx.Includes.jfxXYChartData2sfx
import scalafx.scene.SceneIncludes.jfxXYChartData2sfx
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.Pane
import scalafx.scene.layout.*
import scalafx.scene.control.Tooltip


class Pie(val dataSet: Seq[Int], var dataString: Seq[String]) extends Components {

  var sequence: Seq[javafx.scene.chart.PieChart.Data] = Seq(PieChart.Data(dataString(0), dataSet(0).doubleValue()))
  for i <- 1 until dataSet.size do
    sequence :+= PieChart.Data(dataString(i), dataSet(i).doubleValue())

  private var visible = true
  def toggleVisibility(): Unit = {
    visible = !visible
    component.visible = visible
  }
  val newPie = new PieChart{
    data = sequence}
    sequence.foreach{ d =>
      val showData = new Tooltip()
      showData.setText(s"${d.getName}: ${d.getPieValue}")
      Tooltip.install(d.getNode(), showData)
      d.pieValueProperty().addListener((observable, old, newVal) =>
        showData.setText( s"${newVal.doubleValue()}"))
    }

  override def component: Pane = new Pane {
    children = newPie
  }
  def removePie(): Unit =
    component.children.remove(component)

  def duplicatePie(pie: Pie): Pie = {
    val newDataSeq = pie.dataSet
    val newDataString = pie.dataString
    val newSequence = Seq.tabulate(newDataSeq.length)(i => PieChart.Data(newDataString(i), newDataSeq(i).doubleValue()))
    val newPie = new PieChart {
      data = newSequence
      newSequence.foreach{ d =>
        val showData = new Tooltip()
        showData.setText(s"${d.getName}: ${d.getPieValue}")
        Tooltip.install(d.getNode(), showData)
        d.pieValueProperty().addListener((observable, old, newVal) =>
          showData.setText( s"${newVal.doubleValue()}"))
      }
    }
    val newComponent = new Pane {
      children = newPie
    }
    new Pie(newDataSeq, newDataString) {
      override def component: Pane = newComponent
    }
  }
}