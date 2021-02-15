package UI

import java.awt.Color
import scala.swing._

class ErrorDialog(msg:String) extends Dialog{
  val fbutt = new Button("close")
  println(msg)
  //"Fatal error!: Unable to open file. Probably wrong compression"
    title = "Fatal Error"
    contents = new BoxPanel(Orientation.Vertical){
      //"Fatal error!: Unable to open file. Probably wrong compression"
      contents += new Label(msg){
        preferredSize = new Dimension(500,200)
      }
      contents += fbutt
    }

  fbutt.reactions += {
    case event.ButtonClicked(_) => dispose()
  }
  centerOnScreen()
  background = Color.red
  open()
}
