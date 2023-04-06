package backend.API

import backend.API.DateFormat.*
import backend.API.WeatherData


object AirPollution {

  case class APIResponse(coord: coord, rest: Array[APdata])

  case class APdata(dt: Int, main: Main, components: Components)

  case class Main(aqi: Int)

  case class Components(co: Double, no: Double, no2: Double,
                        o3: Double, s02: Double, pm2_5: Double,
                        pm10: Double, nh3: Double)

  /*def getAirPollutionData(coord: Coord): Response =
    println(s"http://api.openweathermap.org/data/2.5/air_pollution?lat=${coord.lat}&lon=${coord.lon}&appid=${apiKey}")
    val data = APIConnection.fetch(s"http://api.openweathermap.org/data/2.5/air_pollution?lat=${coord.lat}&lon=${coord.lon}&appid=${apiKey}")
    APIConnection.decodePollution(data)*/


}
