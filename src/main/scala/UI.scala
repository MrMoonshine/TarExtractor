import backend.{InfoLabel, TarGz, TarXz}

import java.awt.{Color, Font}
import java.io.File
import scala.swing._

class UI extends Frame {
  title = "Tarball Extractor"

  val destinfo = new InfoLabel("Your Destination: ", new File("").toPath.toAbsolutePath.toString)
  val ininfo = new InfoLabel("Input file: ", "")
  var srcar: String = ""
  var dstloc: String = ""

  contents = new BoxPanel(Orientation.Vertical) {
    contents += new Label("Tar.gz and Tar.xz Extractor") {
      font = new Font(null, Font.BOLD, 20)
      foreground = new Color(109, 13, 150)
    }
    contents += new FlowPanel() {
      contents += new Button("tar.gz or tar.xz archive"){
        reactions += { case event.ButtonClicked(_) =>
          srcar= new FileSelect().selectFile()
          ininfo.alterValue(srcar)
        }
      }
    }
    contents += new FlowPanel() {
      contents += new Label(" Extract to: ")
      contents += new Button("destination") {
        reactions += { case event.ButtonClicked(_) =>
          dstloc = new FileSelect().selectDirectory()
          destinfo.alterValue(dstloc)
        }
    }

      contents += new Button("extract") {
        reactions += { case event.ButtonClicked(_) => {
            try {
              if(dstloc.length < 1) throw new Exception("Invalid Filepath")
              val arch:(TarXz,TarGz) = (new TarXz(srcar),new TarGz(srcar))
              arch._1.dumpInto(new File(dstloc).toPath)
              arch._2.dumpInto(new File(dstloc).toPath)
            }catch{
              case e : Throwable => println("Error occured while extracting")
            }
          }
        }
      }
    }
    contents += ininfo
    contents += destinfo
  }

  override def closeOperation(): Unit = dispose()

  //pack()
  size = new Dimension(540, 180)
  centerOnScreen()
  open()
}
