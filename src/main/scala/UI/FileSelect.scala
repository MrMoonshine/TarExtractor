package UI

import java.io.File
import javax.swing.filechooser.FileFilter
import scala.swing.FileChooser

class FileSelect extends FileChooser{
  val mimeli:List[String] = List("tar.gz","tar.xz","tgz","txz")
  private val FS_DIRECTORY = 0
  private val FS_FILE = 1

  private def selectFS(mode:Int): String ={
    mode match {
      case FS_FILE => {
        fileSelectionMode = FileChooser.SelectionMode.FilesOnly
        fileFilter = new FileFilter {
          override def accept(f: File): Boolean = {
            if(f.isDirectory || f.toString == null){
              return true
            }

            for(el <- mimeli){
              if(f.toString.endsWith(el)){
                return true
              }
            }

            false
          }

          override def getDescription: String = "tar.gz and tar.xz archives"
        }
      }
      case FS_DIRECTORY => fileSelectionMode = FileChooser.SelectionMode.DirectoriesOnly
      case _ => throw new Exception("Invalid File Chooser call!")
    }

    if(showOpenDialog(null) == FileChooser.Result.Approve){
      selectedFile.toPath.toAbsolutePath.toString
    }else{
      ""
    }
  }

  def selectFile(): String ={
    title = "Select your Input File"
    selectFS(FS_FILE)
  }

  def selectDirectory(): String ={
    title = "Select Your Output File"
    selectFS(FS_DIRECTORY)
  }
}
