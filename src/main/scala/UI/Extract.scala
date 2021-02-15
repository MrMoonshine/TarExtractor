package UI
import UI.Utils.{MyButton, MyLabel}
import backend.{TarGz, TarXz}

import java.io.File
import scala.swing.BorderPanel
import scala.swing._

class Extract extends BorderPanel{
    private def arinfoStr(ar:String,dest:String = ""): String = {
      s"<html>This archive will be extracted: <br/>$ar<br/>to: $dest</html>"
    }

    private var srcar: String = "(no archive selected)"
    private var dstloc: String = ""
    var compdir = ""
    private val arinfo = new MyLabel(arinfoStr(srcar,dstloc))
    add(arinfo, BorderPanel.Position.North)
  add(new MyLabel(""), BorderPanel.Position.South)
    add(new MyButton("Destination"){
      reactions += { case event.ButtonClicked(_) =>
        dstloc = new FileSelect().selectDirectory()
        arinfo.text = arinfoStr(srcar,dstloc)
      }
    }, BorderPanel.Position.Center)
    add(new MyButton("Extract"){
      reactions += { case event.ButtonClicked(_) => {
        try {
          add(new MyLabel("..."), BorderPanel.Position.South)
          if(dstloc.length < 1) throw new Exception("Invalid Filepath")
          val arch:(TarXz,TarGz) = (new TarXz(srcar),new TarGz(srcar))
          arch._1.dumpInto(new File(dstloc).toPath)
          arch._2.dumpInto(new File(dstloc).toPath)
          add(new MyLabel("Success"), BorderPanel.Position.South)
        }catch{
          //The world's ugliest dialog
          case e : Throwable => new ErrorDialog("Fatal error!: Unable to open file. Probably wrong compression")
        }
      }
      }
    }, BorderPanel.Position.East)
    add(new MyButton("Select Archive"){
      reactions += { case event.ButtonClicked(_) =>
        srcar= new FileSelect().selectFile()

        if(dstloc.length < 1){
          dstloc = new File(srcar).getParent.toString
        }
        arinfo.text = arinfoStr(srcar,dstloc)
      }
    }, BorderPanel.Position.West)
}
