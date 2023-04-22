package dashboard.GUI

import backend.API.{AirPollution, DateFormat, Forecast, WeatherData}
import backend.API.DateFormat.*
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.scene.control.Alert
import javafx.scene.layout.{AnchorPane, Priority}
import javafx.stage.FileChooser
import scalafx.scene.layout.{BorderPane, GridPane, HBox, VBox}
import scalafx.scene.Scene
import scalafx.application.JFXApp3
import scalafx.Includes.*
import scalafx.scene.control.{Button, CheckMenuItem, ContextMenu, Label, MenuButton, MenuItem, Slider, TextField, ToggleButton, Tooltip}
import scalafx.scene.paint.Color
import scalafx.scene.text.Font
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.{Insets, Orientation, Pos}
import scalafx.scene.Node

import scala.collection.mutable.Buffer
import scalafx.scene.Group
import scalafx.scene.input.{ContextMenuEvent, MouseButton, MouseEvent}

import scala.io.Source
import java.io.*
import scala.util.{Failure, Success, Using}
import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import scalafx.scene.control.Alert.AlertType




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
  var obsrain = ObservableBuffer[String]("Rain (mm)", "Temp (°C)")
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
                         currentAP.so2.toInt, currentAP.pm2_5.toInt, currentAP.pm10.toInt, currentAP.nh3.toInt)


var currentCity = ""

object DashboardUI extends JFXApp3 {
  var data1 = Seq((1.2, 1.3), (3.1, 4.5), (5.3, 11.5))
  var data2 = Seq(10, 20, 30)
  var data3 = Seq(Seq(1.0, 2.0, 3.3), Seq(2.2, 3.0, 4.1), Seq(3.3, 4.1, 5.3))
  var cardData = Seq(Seq(1.0, 2.0, 3.3), Seq(2.2, 3.0, 4.1), Seq(3.3, 4.1, 5.3))
  var cardTemp = 12.0
  var data3x = ObservableBuffer("Rain levels/temp")
  var data5 = Seq[Int]()
  var data6 = Seq[String]()


  override def start(): Unit = {
    var scatterPlot = new ScatterPlot(data1)
    var pie = new Pie(data2, Seq("A", "B", "C"))
    var columnChart = new ColumnChart(data3, data3x, "Y")
    var newCard = new Card(cardData,cardTemp)

    var scatterPlotBox = new VBox {
      id = "scatterPlotBox"
      managed = true
      vgrow = Priority.SOMETIMES
      spacing = 5
      resize
      children = Seq(
        new Label {
          text = "Scatter Plot"
          font = Font.font(20)
          textFill = Color.Black
        },
        scatterPlot.component
      )
    }
    val scatterSlide = new Slider(0.5, 2.0, 1.0)
    scatterSlide.setOrientation(Orientation.HORIZONTAL)
    scatterSlide.setShowTickLabels(true)
    scatterSlide.setShowTickMarks(true)

    scatterSlide.valueProperty().addListener(new ChangeListener[Number] {
      override def changed(observable: ObservableValue[_ <: Number], oldValue: Number, newValue: Number): Unit = {
        scatterPlotBox.setScaleX(newValue.doubleValue())
        scatterPlotBox.setScaleY(newValue.doubleValue())
      }
    })
    scatterPlotBox.getChildren.add(scatterSlide)

    var scatterCount = 1
    val duplicatedScatter = new VBox()

    lazy val scatterMenu = new ContextMenu(new MenuItem("Duplicate"))
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

        val slider = new Slider(0.5, 2.0, 1.0)
        slider.setOrientation(Orientation.HORIZONTAL)
        slider.setShowTickLabels(true)
        slider.setShowTickMarks(true)

        slider.valueProperty().addListener(new ChangeListener[Number] {
          override def changed(observable: ObservableValue[_ <: Number], oldValue: Number, newValue: Number): Unit = {
            duplicated.setScaleX(newValue.doubleValue())
            duplicated.setScaleY(newValue.doubleValue())
          }
        })
        duplicated.getChildren.add(slider)

        duplicatedScatter.children.add(duplicated)
        scatterCount += 1
        val duplicateManu = new ContextMenu(new MenuItem("Remove"))
        duplicated.onContextMenuRequested = (deez: ContextMenuEvent) => {
          duplicateManu.show(duplicated, deez.screenX, deez.screenY)
        }
        duplicateManu.items.head.onAction = (_) => {
            duplicatedScatter.children.remove(duplicatedScatter.children.head)
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

    val pieSlider = new Slider(0.5, 2.0, 1.0)
    pieSlider.setOrientation(Orientation.HORIZONTAL)
    pieSlider.setShowTickLabels(true)
    pieSlider.setShowTickMarks(true)

    pieSlider.valueProperty().addListener(new ChangeListener[Number] {
      override def changed(observable: ObservableValue[_ <: Number], oldValue: Number, newValue: Number): Unit = {
        pieBox.setScaleX(newValue.doubleValue())
        pieBox.setScaleY(newValue.doubleValue())
      }
    })
    pieBox.getChildren.add(pieSlider)

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

        val slider = new Slider(0.5, 2.0, 1.0)
        slider.setOrientation(Orientation.HORIZONTAL)
        slider.setShowTickLabels(true)
        slider.setShowTickMarks(true)

        slider.valueProperty().addListener(new ChangeListener[Number] {
          override def changed(observable: ObservableValue[_ <: Number], oldValue: Number, newValue: Number): Unit = {
            duplicated.setScaleX(newValue.doubleValue())
            duplicated.setScaleY(newValue.doubleValue())
          }
        })

        duplicated.getChildren.add(slider)
        duplicatedPie.children.add(duplicated)
        pieCount += 1
        val duplicateManu = new ContextMenu(new MenuItem("Remove"))
        duplicated.onContextMenuRequested = (deez: ContextMenuEvent) => {
          duplicateManu.show(duplicated, deez.screenX, deez.screenY)
        }
        duplicateManu.items.head.onAction = (_) => {
          duplicatedPie.children.remove(duplicatedPie.children.head)
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

    val columnSlider = new Slider(0.5, 2.0, 1.0)
    columnSlider.setOrientation(Orientation.HORIZONTAL)
    columnSlider.setShowTickLabels(true)
    columnSlider.setShowTickMarks(true)

    columnSlider.valueProperty().addListener(new ChangeListener[Number] {
      override def changed(observable: ObservableValue[_ <: Number], oldValue: Number, newValue: Number): Unit = {
        columnChartBox.setScaleX(newValue.doubleValue())
        columnChartBox.setScaleY(newValue.doubleValue())
      }
    })

    columnChartBox.getChildren.add(columnSlider)


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
        val slider = new Slider(0.5, 2.0, 1.0)
        slider.setOrientation(Orientation.HORIZONTAL)
        slider.setShowTickLabels(true)
        slider.setShowTickMarks(true)

        slider.valueProperty().addListener(new ChangeListener[Number] {
          override def changed(observable: ObservableValue[_ <: Number], oldValue: Number, newValue: Number): Unit = {
            duplicated.setScaleX(newValue.doubleValue())
            duplicated.setScaleY(newValue.doubleValue())
          }
        })

        duplicated.getChildren.add(slider)

        duplicatedColumnChart.children += duplicated
        columnChartCount += 1
        val duplicateManu = new ContextMenu(new MenuItem("Remove"))
        duplicated.onContextMenuRequested = (deez: ContextMenuEvent) => {
          duplicateManu.show(duplicated, deez.screenX, deez.screenY)
        }
        duplicateManu.items.head.onAction = (_) => {
          duplicatedColumnChart.children.remove(duplicatedColumnChart.children.head)
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


    var cardBox = new VBox{
      id = "cardBox"
      spacing = 3
      managed = true
      vgrow = Priority.SOMETIMES
      children = newCard.component
    }

    //draggable groups from youtube
    val draggableNodes: Buffer[Node] = Buffer(scatterPlotBox, pieBox, columnChartBox, duplicatedScatter, duplicatedPie, duplicatedColumnChart,cardBox)
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

    val cardToggle = new CheckMenuItem {
      text = "Card"
      selected = true
      onAction = () => {
        cardBox.visible = !cardBox.visible.value
      }
    }

    val buttonBox = new MenuButton("Hide/Show Charts"){
      alignment = Pos.TopLeft
      items = Seq(columnChartToggle, pieToggle, scatterPlotToggle,cardToggle)
    }


    val dashboard = new GridPane {
      style = "-fx-background-color: #f0e68c;"
      hgap = 5
      vgap = 5
      padding = Insets(20)
      add(buttonBox, 0, 0)
      add(duplicatedColumnChart, 0, 2)
      add(duplicatedPie, 5, 2)
      add(duplicatedScatter, 7, 2)
    }
    scatterMenu.items.last.onAction = (_) => {
      dashboard.children.remove(scatterPlotBox)
    }
    pieMenu.items.last.onAction = (_) => {
      dashboard.children.remove(pieBox)
    }
    columnChartMenu.items.last.onAction = (_) => {
      dashboard.children.remove(columnChartBox)
    }


    var information = new Label("Type in a city name to get weather info...")
    var x = 0
    var y = 0
    var z = 0
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
        if x == 0 then
          columnChartBox.children.remove(columnChartBox.children(1))
          columnChartBox.children.add(columnChart.component)
          x=1
        else
          columnChartBox.children.remove(columnChartBox.children.last)
          columnChartBox.children.add(columnChart.component)


        data1 = currentWeather.dataTemps
        scatterPlot = new ScatterPlot(data1)
        scatterPlot.series.name = "Temp °C"

        if y == 0 then
          scatterPlotBox.children.remove(scatterPlotBox.children(1))
          scatterPlotBox.children.add(scatterPlot.component)
          y=1
        else
          scatterPlotBox.children.remove(scatterPlotBox.children.last)
          scatterPlotBox.children.add(scatterPlot.component)

        cardData = currentWeather.datarain
        cardTemp = currentWeather.temp
        newCard = new Card(cardData, cardTemp)
        cardBox.children.remove(cardBox.children.head)
        cardBox.children.add(newCard.component)
        data5 = currentWeather.airPollutiondata
        data6 = currentWeather.AirPollutionNames
        pie = new Pie(data5,data6)
        if z == 0 then
          pieBox.children.remove(pieBox.children(1))
          pieBox.children.add(pie.component)
          z=1
        else
          pieBox.children.remove(pieBox.children.last)
          pieBox.children.add(pie.component)

        information.setText(s"Location: $currentCity \nDate: ${DateFormat.getDay(currentWeather.getWDfrom.dt)}")
      }
    }


    val chartMenu = new MenuButton("Add charts"){
      alignment = Pos.BottomLeft
      items = Seq(
        new MenuItem{
          text = "Add Column Chart"
          onAction = () =>
            if !dashboard.children.contains(columnChartBox) then
              dashboard.add(columnChartBox,0,1)
            else new Alert(AlertType.Information, "Already added Column Chartt, \ncheck hide/show button if you've hid it.")
        },
        new MenuItem{
          text = "Add Pie Chart"
          if !dashboard.children.contains(pieBox) then
            onAction = () => dashboard.add(pieBox,5,1)
          else new Alert(AlertType.Information, "Already added Pie Chart, \ncheck hide/show button if you've hid it.")

        },
        new MenuItem{
          text = "Add Scatter Chart"
          onAction = () =>
            if !dashboard.children.contains(scatterPlotBox) then
              dashboard.add(scatterPlotBox,7,1)
            else new Alert(AlertType.Information, "Already added Scatter Chart, \ncheck hide/show button if you've hid it.")
        },
        new MenuItem{
          text = "Add Card"
          onAction = () =>
            if !dashboard.children.contains(cardBox) then
              dashboard.add(cardBox,11,1)
            else new Alert(AlertType.Information, "Already added this chart, \ncheck hide/show button if you've hid it.")

        }
      )
    }


    case class ScatterPlotBox(x: Double, y: Double, visible: Boolean)
    case class PieBox(x: Double, y: Double, visible: Boolean)
    case class ColumnChartBox(x: Double, y: Double, visible: Boolean)
    case class CardBox(x: Double, y: Double, visible: Boolean)
    case class Data(data1: Seq[(Double, Double)], data3: Seq[Seq[Double]], data3x: ObservableBuffer[String], cardData: Seq[Seq[Double]], data5: Seq[Int],data6: Seq[String])
    case class Dashboard(scatterPlotBox: ScatterPlotBox, pieBox: PieBox, columnChartBox: ColumnChartBox, data: Data, city:String, info: String,x:Int,y:Int,z:Int,cardtemp: Double,
                         card: CardBox,scatterToggle: Boolean, columnToggle: Boolean, pieToggle: Boolean, cardToggle: Boolean)

    //case class example from 15.4 in OS2, and the way I parsed through the data in the backend.API

    val saveAsButton = new Button("Save") {
      onAction = (_) => {
        val dashboardData = Data(data1, data3, data3x, cardData, data5, data6)
        val dashboard = Dashboard(ScatterPlotBox(scatterPlotBox.translateX(), scatterPlotBox.translateY(), scatterPlotBox.visible.value),
          PieBox(pieBox.translateX(), pieBox.translateY(), pieBox.visible.value),
          ColumnChartBox(columnChartBox.translateX(), columnChartBox.translateY(), columnChartBox.visible.value),
          dashboardData,currentCity, information.getText,x,y,z,cardTemp, CardBox(cardBox.translateX(), cardBox.translateY(), cardBox.visible.value),
          scatterPlotToggle.selected.value, columnChartToggle.selected.value, pieToggle.selected.value, cardToggle.selected.value)

        val writeThis = dashboard.asJson.noSpaces
        val writer = new PrintWriter(new File("dashboard.json"))
        writer.write(writeThis)
        writer.close()
      }
    }

    val loadButton = new Button("Load") {
      onAction = (_) => {
        val source = Source.fromFile("dashboard.json")
        val json = try source.mkString finally source.close()
        val getData = decode[Dashboard](json) match
          case Right(dashboard) => dashboard
          case Left(e) =>
            println("Cannot load this file :D")
            e.printStackTrace()
            throw e

        scatterPlotToggle.selected = getData.scatterToggle
        columnChartToggle.selected = getData.columnToggle
        pieToggle.selected = getData.pieToggle
        cardToggle.selected = getData.cardToggle

        scatterPlotBox.translateX() = getData.scatterPlotBox.x
        scatterPlotBox.translateY() = getData.scatterPlotBox.y
        scatterPlotBox.visible = getData.scatterPlotBox.visible

        pieBox.translateX() = getData.pieBox.x
        pieBox.translateY() = getData.pieBox.y
        pieBox.visible = getData.pieBox.visible

        columnChartBox.translateX() = getData.columnChartBox.x
        columnChartBox.translateY() = getData.columnChartBox.y
        columnChartBox.visible = getData.columnChartBox.visible
        information.setText(getData.info)

        cardBox.translateX = getData.card.x
        cardBox.translateY = getData.card.y
        cardBox.visible = getData.card.visible

        data1 = getData.data.data1
        println(data3.mkString)
        data3 = getData.data.data3
        data3x = getData.data.data3x
        cardData = getData.data.cardData
        data5 = getData.data.data5
        data6 = getData.data.data6
        cardData = getData.data.cardData
        cardTemp = getData.cardtemp

        columnChart = new ColumnChart(data3, data3x, "x")

        if dashboard.children.last != columnSlider then
          columnChartBox.children.remove(columnChartBox.children(1))
          columnChartBox.children.add(columnChart.component)
          x=1
        else
          columnChartBox.children.remove(columnChartBox.children.last)
          columnChartBox.children.add(columnChart.component)

        scatterPlot = new ScatterPlot(data1)
        scatterPlot.series.name = "Temp °C"

        if dashboard.children.last != scatterSlide then
          scatterPlotBox.children.remove(scatterPlotBox.children.last)
          scatterPlotBox.children.add(scatterPlot.component)
          y=1
        else
          scatterPlotBox.children.remove(scatterPlotBox.children(1))
          scatterPlotBox.children.add(scatterPlot.component)

        pie = new Pie(data5,data6)
        if dashboard.children.last != pieSlider then
          pieBox.children.remove(pieBox.children.last)
          pieBox.children.add(pie.component)
          z=1
        else
          pieBox.children.remove(pieBox.children(1))
          pieBox.children.add(pie.component)

        newCard = new Card(cardData,cardTemp)
        cardBox.children.remove(cardBox.children.head)
        cardBox.children.add(newCard.component)

      }
    }


    val sideBar = new VBox{
      minWidth = 100
      spacing = 8
      children = Seq(new Button{
        text = "Weather Dashboard"
        onAction = () => dashboard.toFront()
      },searchLocation,chartMenu,saveAsButton, loadButton, information)
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
