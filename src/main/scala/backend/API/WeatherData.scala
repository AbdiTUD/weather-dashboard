package backend.API
import API.*
import backend.API.DateFormat.*

object WeatherData {
  //we model out the layout of the json file
  case class wind(speed: Double, deg: Int)
  case class snow(`1h`: Double = 0.0)
  case class rain(`1h`: Double = 0.0)
  case class sys(`type`: Int, id:Int,country: String,sunrise: Int, sunset:Int )
  case class main(temp: Double, feels_like: Double, temp_min: Double, temp_max: Double,
                  pressure: Int, humidity:Int)
  case class reportResponse(
                       coord:coord,
                       weather: Array[weather],
                       base: String,
                       main: main,
                       visibility: Int,
                       wind: wind,
                       clouds: clouds,
                       dt:Long,
                       timezone: Int,
                       id: Int,
                       name:String,
                       cod:Int,
                       sys: sys,
                       snow: Option[snow],
                       rain:Option[rain]
                     )

  case class DataResponse(private val resp: reportResponse):
    val coord = resp.coord
    val weather = resp.weather
    val base = resp.base
    val main = resp.main
    val visibility = resp.visibility
    val wind = resp.wind
    val clouds = resp.clouds
    val dt = resp.dt
    val timezone = resp.timezone
    val id = resp.id
    val name = resp.name
    val cod = resp.cod
    val sys = resp.sys
    val snowed = resp.snow match
      case Some(s) => s
      case None => snow()
    val rained = resp.rain match
      case Some(r) => r
      case None => rain()

  //we use the parsing methods created in the API class
  def getWeatherData(coord: coord): DataResponse =
    val APIcall = s"https://api.openweathermap.org/data/2.5/weather?lat=${coord.lat}&lon=${coord.lon}&appid=${apiKey}&units=metric"
    val fetchData = API.fetchData(APIcall)
    API.decodeWD(fetchData)

  def getWDData(place:String): DataResponse =
    val APIcall =  s"https://api.openweathermap.org/data/2.5/weather?q=$place&appid=${apiKey}&units=metric"
    val fetchData = API.fetchData(APIcall)
    API.decodeWD(fetchData)
}