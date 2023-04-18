package backend.API
import backend.API.DateFormat.*
import API.*



object Forecast :
  case class Wind(speed: Double, deg: Int, gust: Double = 0.0)
  case class snow(`1h`: Double = 0.0)
  case class rain(`1h`: Double = 0.0)
  case class sys(`type`: Int, id:Int,country: String,sunrise: Int, sunset:Int )
  case class main(temo: Double, feels_like:Double,temp_min:Double,temp_max:Double,
    pressure:Int,sea_level: Int,grnd_level: Int, humidity: Int,temp_kf:Double)
  case class RespWind(speed: Double, deg: Int, gust: Option[Double])
  case class city(id:Int, name: String,coord:coord,country:String,population:Int,timezone:Int,
                  sunrise:Int,sunset:Int)
  case class listReponse(cod: Int, message: Int, cnt:Int, list:Array[forecastResponse],city:city)

  case class forecastResponse(
                   dt: Long,
                   main: main,
                   weather: Array[weather],
                   clouds: clouds,
                   wind: RespWind,
                   visibility: Int,
                   sys: sys,
                   dt_txt: String,
                   rain: Option[rain],
                   snow: Option[snow])
  
  case class finalResponse(forecast: listReponse):
    val cod = forecast.cod
    val message = forecast.message
    val cnt = forecast.cnt
    val city = forecast.city
    val list = forecast.list.map(Forecast(_))

  case class Forecast(resp: forecastResponse):
    val dt      = resp.dt
    val main    = resp.main
    val weather = resp.weather
    val clouds  = resp.clouds
    val wind    = resp.wind.gust match
      case Some(w) => Wind(resp.wind.speed, resp.wind.deg, w)
      case None => Wind(resp.wind.speed, resp.wind.deg)
    val visibility = resp.visibility
    val sys     = resp.sys
    val dt_txt  = resp.dt_txt
    val rained    = resp.rain match
      case Some(r) => r
      case None => rain()
    val snowed    = resp.snow match
      case Some(s) => s
      case None => snow()



