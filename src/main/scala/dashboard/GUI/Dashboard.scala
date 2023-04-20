package dashboard.GUI

import backend.API.WeatherData
import backend.API.DateFormat.*
import backend.API.Forecast
import backend.API.AirPollution

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

class DataModeler(place: String):
  var getWDfrom: WeatherData.DataResponse = WeatherData.getWDData(place)
  var getForecast: Forecast.finalResponse = Forecast.getForeData(place)
  var getAirPol: AirPollution.APIResponse = AirPollution.getAPData(place)

  def update(): Unit =
    getWDfrom = WeatherData.getWDData(place)
    getForecast = Forecast.getForeData(place)
    getAirPol = AirPollution.getAPData(place)


  val temp = getWDfrom.main.temp
  val rain = getWDfrom.rained.`1h`
  val forelist = getForecast.list

  var datarain = Seq[Seq[Double]]()
  var obsrain = ObservableBuffer[String]("Rain", "Temp")
  val foreCasts =
    for i <- 0 to getForecast.list.size/2 do
      val thisFore = forelist(i)
      datarain = datarain :+ Seq(thisFore.rained.`3h`,thisFore.main.temp)

  var dataTemps = Seq[(Double,Double)]()
  val temperatureFore =
    for i <- getForecast.list.indices do
      val current = forelist(i)
      dataTemps = dataTemps :+ (i.toDouble, current.resp.main.temp)

  var AirPollutionNames = Seq("co","no","no2","o3","so2","pm2_5","pm10","nh3")
  val currentAP = getAirPol.list.head.components
  var airPollutiondata = Seq[Int](currentAP.co.toInt, currentAP.no.toInt, currentAP.no2.toInt, currentAP.o3.toInt,
                         currentAP.s02.toInt, currentAP.pm2_5.toInt, currentAP.pm10.toInt, currentAP.nh3.toInt)



var currentCity = ""

object DashboardUI extends JFXApp3 {
  var data1 = Seq((1.2, 1.3), (3.1, 4.5), (5.3, 11.5))
  var data2 = Seq(10, 20, 30)
  var data3 = Seq(Seq(1.0, 2.0, 3.3), Seq(2.2, 3.0, 4.1), Seq(3.3, 4.1, 5.3))
  var data3x = ObservableBuffer("Rain levels/temp")


  override def start(): Unit = {
    var scatterPlot = new ScatterPlot(data1)
    var pie = new Pie(data2, Seq("A", "B", "C"))
    var columnChart = new ColumnChart(data3, data3x, "Y")

    var scatterPlotBox = new VBox {
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
        val duplicateManu = new ContextMenu(new MenuItem("Remove"))
        duplicated.onContextMenuRequested = (deez: ContextMenuEvent) => {
          duplicateManu.show(duplicated, deez.screenX, deez.screenY)
        }
        duplicateManu.items.head.onAction = (_) => {
          scatterPlot.duplicate(scatterPlot).removeScatter()
          duplicated.children.remove(duplicated.children.head)
          duplicated.children.remove(duplicated.children.last)
          scatterCount-=1
        }
      else
        val alert = new Alert(Alert.AlertType.INFORMATION){
          setTitle("Duplicate Component")
          setHeaderText("Already duplicated this component")
        }
        alert.showAndWait()
    }

    scatterMenu.items.add(new MenuItem("Remove"))
    scatterPlotBox.onContextMenuRequested = (deez: ContextMenuEvent) => {
      scatterMenu.show(scatterPlotBox, deez.screenX, deez.screenY)
    }
    scatterMenu.items.last.onAction = (_) => {
      scatterPlot.removeScatter()
      scatterPlotBox.children.remove(scatterPlotBox.children.last)
      scatterPlotBox.children.remove(scatterPlotBox.children.head)
    }

    var pieBox = new VBox {
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
        val duplicateManu = new ContextMenu(new MenuItem("Remove"))
        duplicated.onContextMenuRequested = (deez: ContextMenuEvent) => {
          duplicateManu.show(duplicated, deez.screenX, deez.screenY)
        }
        duplicateManu.items.head.onAction = (_) => {
          duplicated.children.remove(duplicated.children.last)
          duplicated.children.remove(duplicated.children.head)
          pieCount-=1
        }

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
      pieBox.children.remove(pieBox.children.head)
    }
    var columnChartBox = new VBox {
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
        val duplicateManu = new ContextMenu(new MenuItem("Remove"))
        duplicated.onContextMenuRequested = (deez: ContextMenuEvent) => {
          duplicateManu.show(duplicated, deez.screenX, deez.screenY)
        }
        duplicateManu.items.head.onAction = (_) => {
          duplicated.children.remove(duplicated.children.last)
          duplicated.children.remove(duplicated.children.head)
          columnChartCount-=1
        }
      else
        val alert = new Alert(Alert.AlertType.INFORMATION){
          setTitle("Duplicate Component")
          setHeaderText("Already duplicated this component")
        }
        alert.showAndWait()
    }

    columnChartMenu.items.add(new MenuItem("Remove"))
    columnChartBox.onContextMenuRequested = (deez: ContextMenuEvent) => {
      columnChartMenu.show(columnChartBox, deez.screenX, deez.screenY)
    }
    columnChartMenu.items.last.onAction = (_) => {
      columnChart.removeColumn()
      columnChartBox.children.remove(columnChartBox.children.last)
      columnChartBox.children.remove(columnChartBox.children.head)
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
      onAction = () => {
        currentCity = text.value
        var currentWeather = DataModeler(currentCity)
        println(currentWeather.temp.toString)
        println(currentWeather.datarain)
        //data3 = currentWeather.datarain
        //data3x = currentWeather.obsrain
        data3 = currentWeather.datarain
        data3x = currentWeather.obsrain
        columnChart = new ColumnChart(data3, data3x, "x")
        columnChartBox.children.remove(columnChartBox.children.last)
        columnChartBox.children.add(columnChart.component)

        data1 = currentWeather.dataTemps
        scatterPlot = new ScatterPlot(data1)
        scatterPlotBox.children.remove(scatterPlotBox.children.last)
        scatterPlotBox.children.add(scatterPlot.component)

        pie = new Pie(currentWeather.airPollutiondata,currentWeather.AirPollutionNames)
        pieBox.children.remove(pieBox.children.last)
        pieBox.children.add(pie.component)

      }
    }

    val dashboard = new GridPane {
      style = "-fx-background-color: #f0e68c;"
      hgap = 5
      vgap = 5
      padding = Insets(20)
      add(buttonBox, 0, 0)
      add(new Label("Pick a chart from 'Add Charts' from the left sidepanel") {
        managed = false
        font = Font.font(20)
        textFill = Color.Black
      }, 0, 1, 8, 1)
      add(duplicatedColumnChart, 0, 2)
      add(duplicatedPie, 5, 2)
      add(duplicatedScatter, 7, 2)
    }

    val chartMenu = new MenuButton("Add charts"){
      alignment = Pos.BottomLeft
      items = Seq(
        new MenuItem{
          text = "Add Column Chart"
          onAction = () => dashboard.add(columnChartBox,0,1)
        },
        new MenuItem{
          text = "Add Pie Chart"
          onAction = () => dashboard.add(pieBox,5,1)
        },
        new MenuItem{
          text = "Add Scatter Chart"
          onAction = () => dashboard.add(scatterPlotBox,7,1)
        }
      )
    }
    val sideBar = new VBox{
      minWidth = 100
      spacing = 8
      children = Seq(new Button{
        text = "Weather Dashboard"
        onAction = () => dashboard.toFront()
      },searchLocation,chartMenu)
    }
    stage = new JFXApp3.PrimaryStage {
      title = "Dashboard"
      width = 1800
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