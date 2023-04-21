package backend.API

import java.util.Date
import java.text.SimpleDateFormat


object DateFormat {

  /**
   *
   * @param dt is given in the JSON file, of the API response. It is time of data calculation,
   *           give in unix, UTC. With this method we reformat it and turn it into GMT+3 time.
   */


  case class clouds(all: Int)
  case class weather(id: Int, main: String, description: String, icon: String)
  case class coord(lat: Double, lon: Double)


  def getHour(dt: Long) =
    val formated = new SimpleDateFormat("H.00")
    val date = new java.util.Date(1000L*dt)
    formated.setTimeZone(java.util.TimeZone.getTimeZone("GMT+3"))
    formated.format(date)

  def getWeekday(dt: Long) =
    val formatted = new SimpleDateFormat("E")
    val whatDay = new Date(dt*1000L)
    formatted.format(whatDay)

  def getDay(dt: Long) =
    val formated = new SimpleDateFormat("d.M.y")
    val what = new Date(dt*1000L)
    formated.format(what)


}
