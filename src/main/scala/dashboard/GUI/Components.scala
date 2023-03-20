package dashboard.GUI
import scalafx.scene.chart.NumberAxis
import scalafx.scene.Node
import scalafx.scene.Scene

abstract class Components(val dataSet: Seq[Number]) {
  
  def start(): Unit
  val setTitle: Unit
  val xAxis: NumberAxis
  val yAxis: NumberAxis
}
