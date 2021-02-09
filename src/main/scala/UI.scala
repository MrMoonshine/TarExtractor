import backend.{CompressXZ, TarGz, TarXz}

import java.awt.{Color, Font}
import java.io.File
import scala.swing._

class UI extends Frame {
  title = "Tarball Extractor"

  private val destinfo = new InfoLabel("Your Destination: ", new File("").toPath.toAbsolutePath.toString)
  private val ininfo = new InfoLabel("Input file: ", "")
  private var srcar: String = ""
  private var dstloc: String = ""

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
                  //The world's ugliest dialog
              case e : Throwable => new ErrorDialog("Fatal error!: Unable to open file. Probably wrong compression")
            }
          }
        }
      }
    }
    contents += ininfo
    contents += destinfo

    /*--------------------------------------------------*/
    /*              Compressor UI                       */
    /*--------------------------------------------------*/
    var compdir = ""
    val folinfo = new Label("<no folder selected>")
    contents += new Label("Compress to tar.xz"){
      foreground = new Color(252, 140, 3)
      font = new Font(null, Font.BOLD, 20)
    }
    contents += new Label("This program can only compress tar.xz beacuse it's better than tar.gz :3")

    contents += folinfo
    contents += new Button("Select Your Directory"){
      reactions += {
        case event.ButtonClicked(_) => {
          compdir = new FileSelect().selectDirectory()
          if(compdir.length > 0){
            println(compdir)
            folinfo.text = compdir
          }
        }
      }
    }
    contents += new Button("compress"){
      reactions += {
        case event.ButtonClicked(_) => {
          if(compdir.length > 0){
            val target = new File(compdir)
            val dest = new File(target.toString + ".tar.xz")
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
    }
  }

  override def closeOperation(): Unit = dispose()

  //pack()
  size = new Dimension(540, 300)
  centerOnScreen()
  open()
}
