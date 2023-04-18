package dashboard.GUI

import backend.API.*
import javafx.scene.control.Alert
import javafx.scene.layout.{AnchorPane, Priority}
import scalafx.scene.layout.{BorderPane, GridPane, HBox, VBox}
import scalafx.scene.Scene
import scalafx.application.JFXApp3
import scalafx.Includes.*
import scalafx.scene.control.{Button, CheckMenuItem, ContextMenu, Label, MenuButton, MenuItem, TextField, ToggleButton, Tooltip}
import scalafx.scene.paint.Color
import scalafx.scene.text.Font
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.scene.Node

import scala.collection.mutable.Buffer
import scalafx.geometry.Pos
import scalafx.scene.Group
import scalafx.scene.input.{ContextMenuEvent, MouseButton, MouseEvent}



object DashboardUI extends JFXApp3 {
  val data1 = Seq((1.2, 1.3), (3.1, 4.5), (5.3, 11.5))
  val data2 = Seq(10, 20, 30)
  val data3 = Seq(Seq(1.0, 2.0, 3.3), Seq(2.2, 3.0, 4.1), Seq(3.3, 4.1, 5.3))
  val data3x = ObservableBuffer("A", "B", "C")


  override def start(): Unit = {
    val scatterPlot = new ScatterPlot(data1)
    val pie = new Pie(data2, Seq("A", "B", "C"))
    val columnChart = ColumnChart(data3, data3x, "Y")

    val scatterPlotBox = new VBox {
      id = "scatterPlotBox"
      managed = true
      vgrow = Priority.SOMETIMES
      spacing = 5
      children = Seq(
        new Label {
          text = "Scatter Plot"
          font = Font.font(20)
          textFill = Color.Black
        },
        scatterPlot.component
      )
    }

    var scatterCount = 1
    val duplicatedScatter = new VBox()

    val scatterMenu = new ContextMenu(new MenuItem("Duplicate"))
    scatterPlotBox.onContextMenuRequested = (deez: ContextMenuEvent) => {
      scatterMenu.show(scatterPlotBox, deez.screenX, deez.screenY)
    }
    scatterMenu.items.head.onAction = (_) => {
      if scatterCount == 1 then
        val duplicated = new VBox {
          id = "scatterPlotBox"
          managed = true
          vgrow = Priority.SOMETIMES
          spacing = 5
          children = Seq(
            new Label {
              text = s"Copy Scatter Plot $scatterCount"
              font = Font.font(20)
              textFill = Color.Black
            },
            scatterPlot.duplicate(scatterPlot).component
          )
        }
        duplicatedScatter.children.add(duplicated)
        scatterCount += 1
      else
        val alert = new Alert(Alert.AlertType.INFORMATION){
        setTitle("Duplicate Component")
        setHeaderText("Already duplicated this component")
      }
        alert.showAndWait()
    }


    val pieBox = new VBox {
      id = "pieBox"
      spacing = 3
      managed = true
      vgrow = Priority.SOMETIMES
      children = Seq(
        new Label {
          text = "Pie Chart"
          font = Font.font(20)
          textFill = Color.Black
        },
        pie.component
      )
    }
    var pieCount = 1
    val duplicatedPie = new VBox()

    val pieMenu = new ContextMenu(new MenuItem("Duplicate"))
    pieBox.onContextMenuRequested = (deez: ContextMenuEvent) => {
      pieMenu.show(pieBox, deez.screenX, deez.screenY)
    }
    pieMenu.items.head.onAction = (_) => {
      if pieCount == 1 then
        val duplicated = new VBox {
          id = "pieBox"
          managed = true
          vgrow = Priority.SOMETIMES
          spacing = 5
          children = Seq(
            new Label {
              text = s"Copy Pie Chart $pieCount"
              font = Font.font(20)
              textFill = Color.Black
            },
            pie.duplicatePie(pie).component
          )
        }
        duplicatedPie.children.add(duplicated)
        pieCount += 1
      else
        val alert = new Alert(Alert.AlertType.INFORMATION){
          setTitle("Duplicate Component")
          setHeaderText("Already duplicated this component")
        }
        alert.showAndWait()
    }
    pieMenu.items.add(new MenuItem("Remove"))
    pieBox.onContextMenuRequested = (deez: ContextMenuEvent) => {
      pieMenu.show(pieBox, deez.screenX, deez.screenY)
    }
    pieMenu.items.last.onAction = (_) => {
      pie.removePie()
    }

    val columnChartBox = new VBox {
      id = "columnChartBox"
      spacing = 3
      managed = true
      vgrow = Priority.SOMETIMES
      children = Seq(
        new Label {
          text = "Column Chart"
          font = Font.font(20)
          textFill = Color.Black
        },
        columnChart.component
      )
    }

    var columnChartCount = 1
    var duplicatedColumnChart = new VBox()

    val columnChartMenu = new ContextMenu(new MenuItem("Duplicate"))
    columnChartBox.onContextMenuRequested = (deez: ContextMenuEvent) => {
      columnChartMenu.show(columnChartBox, deez.screenX, deez.screenY)
    }
    columnChartMenu.items.head.onAction = (_) => {
      if columnChartCount == 1 then
        val duplicated = new VBox {
          id = "columnChartBox2"
          managed = true
          vgrow = Priority.SOMETIMES
          spacing = 5
          children = Seq(
            new Label {
              text = s"Copy Column Chart $columnChartCount"
              font = Font.font(20)
              textFill = Color.Black
            },
            columnChart.duplicateChart(columnChart).component
          )
        }
        duplicatedColumnChart.children += duplicated
        columnChartCount += 1
      else
        val alert = new Alert(Alert.AlertType.INFORMATION){
          setTitle("Duplicate Component")
          setHeaderText("Already duplicated this component")
        }
        alert.showAndWait()
    }

    //draggable groups from youtube
    val draggableNodes: Buffer[Node] = Buffer(scatterPlotBox, pieBox, columnChartBox, duplicatedScatter, duplicatedPie, duplicatedColumnChart)
    var mouseStartX: Double = 0
    var mouseStartY: Double = 0
    draggableNodes.foreach { node =>
      node.onMousePressed = (event) => {
        mouseStartX = event.sceneX
        mouseStartY = event.sceneY
      }
      node.onMouseDragged = (event) => {
        val deltaX = event.sceneX - mouseStartX
        val deltaY = event.sceneY - mouseStartY

        node.translateX = node.translateX() + deltaX
        node.translateY = node.translateY() + deltaY

        mouseStartX = event.sceneX
        mouseStartY = event.sceneY
      }
    }

    val scatterPlotToggle = new CheckMenuItem {
      text = "ScatterPlot"
      selected = true
      onAction = () => {
        scatterPlotBox.visible = !scatterPlotBox.visible.value
      }
    }

    val pieToggle = new CheckMenuItem {
      text = "PieChart"
      selected = true
      onAction = () => {
        pieBox.visible = !pieBox.visible.value
      }
    }

    val columnChartToggle = new CheckMenuItem {
      text = "ColumnChart"
      selected = true
      onAction = () => {
        columnChartBox.visible = !columnChartBox.visible.value
      }
    }

    val buttonBox = new MenuButton("Hide/Show Charts"){
      alignment = Pos.TopLeft
      items = Seq(columnChartToggle, pieToggle, scatterPlotToggle)

    }

    val searchLocation = new TextField{
      promptText = "Search City"
    }
    var currentCity = ""
    searchLocation.text.onChange{(_,_,newValue) => searchLocation.text = newValue}

    val dashboard = new GridPane {
          style = "-fx-background-color: #f0e68c;"
          hgap = 8
          vgap = 8
          padding = Insets(20)
          add(buttonBox, 0, 0)
          add(columnChartBox, 0, 1)
          add(duplicatedColumnChart,0,2)
          add(pieBox, 5, 1)
          add(duplicatedPie, 5, 2)
          add(scatterPlotBox, 7, 1)
          add(duplicatedScatter, 7, 2)
        }
    val sideBar = new VBox{
      minWidth = 100
      spacing = 8
      children = Seq(new Button{
        text = "Weather Dashboard"
        onAction = () => dashboard.toFront()
      },searchLocation)
    }
    stage = new JFXApp3.PrimaryStage {
      title = "Dashboard"
      width = 1600
      height = 1000
      scene = new Scene {
        root = new BorderPane {
          left = sideBar
          center = dashboard
        }

      }
    }
  }
}
