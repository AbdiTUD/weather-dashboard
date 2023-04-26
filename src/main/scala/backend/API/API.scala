package backend.API

import scala.io.Source
import scala.util.{Failure, Success, Using}
import backend.API.AirPollution.APIResponse as APResp
import backend.API.WeatherData.reportResponse as WDresp
import backend.API.Forecast.{finalResponse, listReponse as ForeResp}
import dashboard.GUI.DashboardUI
import io.circe.parser.decode
import io.circe.*
import io.circe.parser.*
import io.circe.syntax.*
import io.circe.generic.auto.*
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import dashboard.GUI.DashboardUI.stage

object API {
  // we keep track of the API responses
  private var responseCount = 0
  lazy val apiKey: String = {
    val getFile = "apiKey.txt"
    val read = Source.fromFile(getFile)
    val apikey = read.getLines().mkString
    read.close()
    apikey
  }

  def apiResponseCounter() =
    responseCount +=1
    if responseCount > 80 then
      throw new Exception("you're wasting calls")

  // we use circe to parse through the data here, if we cannot read from the URL we get an error. For example if the connection fails.
  def fetchData(url: String): String =
    apiResponseCounter()
    val givDada = Using(Source.fromURL(url)){
      JSN => JSN.getLines().mkString
    }
    
    givDada match
      case Success(file) => file
      case Failure(e) =>
        new Alert(AlertType.ERROR, "Connection failed").showAndWait()
        Console.err.println("Connection failed")
        throw e
  //if we find a JSON call that contains the same case classes as the APResp then we get a success else we get a failure
  def decodeAP(data: String) =
    val deez = decode[APResp](data).toTry
    deez match
      case Success(out) => out
      case Failure(e) =>
        Console.err.print("Bad data or connection failed :D")
        e.printStackTrace()
        new Alert(AlertType.ERROR, "Bad data or connection failed :D").showAndWait()
        throw e

  // same as above

  def decodeWD(data: String) =
    val boom = decode[WDresp](data).toTry
    boom match
      case Success(deez) => WeatherData.DataResponse(deez)
      case Failure(e) =>
        Console.err.println("Bad data or connection failed :D weatherdata")
        e.printStackTrace()
        new Alert(AlertType.ERROR, "Bad data or connection failed :D").showAndWait()
        throw e

  // same as above

  def decodeFore(data:String) =
    val shakalaka = decode[ForeResp](data).toTry
    shakalaka match
      case Success(deez) => finalResponse(deez)
      case Failure(e) =>
        Console.err.println("Bad data or connection failed :D")
        e.printStackTrace()
        new Alert(AlertType.ERROR, "Bad data or connection failed :D").showAndWait()
        throw e

}
