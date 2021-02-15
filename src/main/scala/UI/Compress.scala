package UI

import UI.Utils.{MyButton, MyLabel}
import backend.CompressXZ

import java.io.File
import scala.swing._
/*--------------------------------------------------*/
/*              Compressor UI                       */
/*--------------------------------------------------*/
class Compress extends BorderPanel{
  private def folinfoStr(dir:String): String = {
    s"<html>This directory will be compressed: <br/>$dir</html>"
  }
  var compdir = ""
  val folinfo = new MyLabel(folinfoStr("(no directory selected)"))
  val dstfinfo = new MyLabel("")

  add(folinfo, BorderPanel.Position.North)
  add(new MyButton("Select"){
    reactions += {
      case event.ButtonClicked(_) => {
        compdir = new FileSelect().selectDirectory()
        if(compdir.length > 0){
          println(compdir)
          folinfo.text = folinfoStr(compdir)
        }
      }
    }
  }, BorderPanel.Position.Center)
  add(new MyLabel("Compress to tar.xz"), BorderPanel.Position.West)
  add(new MyButton("Compress"){
    reactions += {
      case event.ButtonClicked(_) => {
        if(compdir.length > 0){
          val target = new File(compdir)
          val dest = new File(target.toString + ".tar.xz")
          add(new Label("Your directory is in: " + dest.getName), BorderPanel.Position.South)
          println(dest.toString)
          try{
            new CompressXZ(target,dest)
            compdir = ""
            folinfo.text = "Your directory is now in: " + dest.toString
          }catch{
            case e : Throwable => new ErrorDialog("Something went wrong")
          }

        }
      }
    }
  }, BorderPanel.Position.East)
  add(dstfinfo, BorderPanel.Position.South)
  border = Utils.consts.padborder
}
