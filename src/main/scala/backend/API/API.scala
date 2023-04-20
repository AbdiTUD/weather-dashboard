package backend.API

import scala.io.Source
import scala.util.{Success, Failure, Using}

import backend.API.AirPollution.{APIResponse=>APResp}
import backend.API.WeatherData.{reportResponse => WDresp}
import backend.API.Forecast.{listReponse => ForeResp,finalResponse}

import io.circe.parser.decode
import io.circe.*, io.circe.parser.*, io.circe.syntax.*, io.circe.generic.auto.*

object API {
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

  def fetchData(url: String): String =
    apiResponseCounter()
    val givDada = Using(Source.fromURL(url)){
      JSN => JSN.getLines().mkString
    }
    
    givDada match
      case Success(file) => file
      case Failure(e) =>
        Console.err.println("Connection failed")
        throw e

  def decodeAP(data: String) =
    val deez = decode[APResp](data).toTry
    deez match
      case Success(out) => out
      case Failure(e) =>
        Console.err.print("Bad data or connection failed :D")
        e.printStackTrace()
        throw e

  def decodeWD(data: String) =
    val boom = decode[WDresp](data).toTry
    boom match
      case Success(deez) => WeatherData.DataResponse(deez)
      case Failure(e) =>
        Console.err.println("Bad data or connection failed :D weatherdata")
        e.printStackTrace()
        throw e
        
  def decodeFore(data:String) =
    val shakalaka = decode[ForeResp](data).toTry
    shakalaka match
      case Success(deez) => finalResponse(deez)
      case Failure(e) =>
        Console.err.println("Bad data or connection failed :D")
        e.printStackTrace()
        throw e

}
