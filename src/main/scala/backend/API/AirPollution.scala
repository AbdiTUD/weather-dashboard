package backend.API

import backend.API.DateFormat.*
import backend.API.WeatherData
import backend.API.API.*

object AirPollution {

  case class APIResponse(coord: coord,
                         list: Array[APdata]
                        )

  case class APdata(main: Main,
                    components: Components,
                    dt: Int
                     )

  case class Main(aqi: Int)
  case class Components(
                        co: Double,
                        no: Double,
                        no2: Double,
                        o3: Double,
                        s02: Double,
                        pm2_5: Double,
                        pm10: Double,
                        nh3: Double
                        )

  def getAirPollutionData(coord: coord) =
    println(s"http://api.openweathermap.org/data/2.5/air_pollution?lat=${coord.lat}&lon=${coord.lon}&appid=${apiKey}")
    val data = API.fetchData(s"http://api.openweathermap.org/data/2.5/air_pollution?lat=${coord.lat}&lon=${coord.lon}&appid=${apiKey}")
    API.decodeAP(data)

  def getAPData(name: String) =
    val giv = WeatherData.getWDData(name)
    getAirPollutionData(giv.coord)

}
