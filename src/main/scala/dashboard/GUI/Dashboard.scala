package dashboard.GUI

import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.Scene
import scalafx.application.JFXApp3
import scalafx.Includes.*
import scalafx.scene.control.{Label, ToggleButton}
import scalafx.scene.paint.Color
import scalafx.scene.text.Font
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.Node
import scala.collection.mutable.Buffer
import scalafx.geometry.Pos
import scalafx.scene.Group


object DashboardUI extends JFXApp3 {
  val data1 = Seq((1.2, 1.3), (3.1, 4.5), (5.3, 11.5))
  val data2 = Seq(10, 20, 30)
  val data3 = Seq(Seq(1.0, 2.0, 3.3), Seq(2.2, 3.0, 4.1), Seq(3.3, 4.1, 5.3))
  val data3x = ObservableBuffer("A", "B", "C")

  override def start(): Unit = {
    val scatterPlot = new ScatterPlot(data1)
    val pie = new Pie(data2, Seq("A", "B", "C"))
    val columnChart = new ColumnChart(data3, data3x, "Y")

    val scatterPlotBox = new VBox {
      spacing = 10
      children = Seq(
        new Label {
          text = "Scatter Plot"
          font = Font.font(20)
          textFill = Color.Black
        },
        scatterPlot.component
      )
    }

    val pieBox = new VBox {
      spacing = 10
      children = Seq(
        new Label {
          text = "Pie Chart"
          font = Font.font(20)
          textFill = Color.Black
        },
        pie.component
      )
    }

    val columnChartBox = new VBox {
      spacing = 10
      children = Seq(
        new Label {
          text = "Column Chart"
          font = Font.font(20)
          textFill = Color.Black
        },
        columnChart.component
      )
    }


    val scatterPlotToggle = new ToggleButton {
      text = "Hide/Show"
      selected = true
      onAction = () => {
        scatterPlotBox.visible = !scatterPlotBox.visible.value
      }
    }

    val pieToggle = new ToggleButton {
      text = "Hide/Show"
      selected = true
      onAction = () => {
        pieBox.visible = !pieBox.visible.value
      }
    }

    val columnChartToggle = new ToggleButton {
      text = "Hide/Show"
      selected = true
      onAction = () => {
        columnChartBox.visible = !columnChartBox.visible.value
      }
    }

    stage = new JFXApp3.PrimaryStage {
      title = "Dashboard"
      width = 1600
      height = 900
      scene = new Scene {
        root = new VBox {
          spacing = 10
          padding = Insets(20)
          children = Seq(
            new HBox {
              spacing = 10
              resizable = true
              children = Seq(
                columnChartBox,
                scatterPlotBox,
                pieBox
              )
            },
            new HBox {
              spacing = 10
              children = Seq(
                columnChartToggle,
                scatterPlotToggle,
                pieToggle
              )
            }
          )
        }
      }
    }
  }
}
