package dashboard.GUI
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import dashboard.GUI.Components
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import scalafx.scene.layout.*
import scalafx.Includes._
import javafx.scene.Node
import javafx.scene.control.Tooltip
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
    series.getData.foreach{d =>
      val showData = new Tooltip()
      showData.setText(s"x: ${d.getXValue.doubleValue()}, y: ${d.getYValue.doubleValue()}")
      Tooltip.install(d.getNode.delegate, showData)
      d.getNode.setOnMouseEntered(_ => d.getNode.getStyleClass.add("Highlight"))
      d.getNode.setOnMouseExited(_ => d.getNode.getStyleClass.removeAll("Highlight"))
    }
    }
  }
  def removeScatter(): Unit =
    component.children.remove(component)
    
  def duplicate(scatterPlot: ScatterPlot): ScatterPlot = {
    val newData = scatterPlot.dataSet.map(p => (p._1, p._2))
    val newSeries = new XYChart.Series[Number, Number] {
      name = "scatter duplicate"
      data() ++= newData.map { case (x: Double, y: Double) => XYChart.Data[Number, Number](x, y) }
    }
    val newScatterPlot = new ScatterPlot(newData) {
      override def component: Pane = new Pane {
        children = new LineChart(NumberAxis(), NumberAxis()) {
          data() += newSeries
          newSeries.getData.foreach{d =>
            val showData = new Tooltip()
            showData.setText(s"x: ${d.getXValue.doubleValue()}, y: ${d.getYValue.doubleValue()}")
            Tooltip.install(d.getNode.delegate, showData)
            d.getNode.setOnMouseEntered(_ => d.getNode.getStyleClass.add("Highlight"))
            d.getNode.setOnMouseExited(_ => d.getNode.getStyleClass.removeAll("Highlight"))
          }
        }
      }
    }
    newScatterPlot
  }

}

  /*def createScatterPlotBox(dataSet: Seq[(Double, Double)]): VBox = {
  new VBox {
    id = "scatterPlotBox"
    managed = true
    vgrow = Priority.ALWAYS
    spacing = 5
    children = Seq(
      new Label {
        text = "Scatter Plot"
        font = Font.font(20)
        textFill = Color.Black
      },
      new ScatterPlot(dataSet).component
    )
  }*/


