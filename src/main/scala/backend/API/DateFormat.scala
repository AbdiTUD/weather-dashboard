package backend.API

import java.util.Date
import java.text.SimpleDateFormat


object DateFormat {
  case class Coord(lat: Double, lon: Double)
  case class Weather(id: Int, main: String, description: String, icon: String)


  /**
   *
   * @param dt is given in the JSON file, of the API response. It is time of data calculation,
   *           give in unix, UTC. With this method we reformat it and turn it into GMT+3 time.
   */
  def getHour(dt: Long) =
    val formated = new SimpleDateFormat("H.00")
    val date = new java.util.Date(1000L*dt)
    formated.setTimeZone(java.util.TimeZone.getTimeZone("GMT+3"))
    formated.format(date)


}
