package UI

import scala.swing.{GridPanel, MainFrame}

class MainWindow extends MainFrame{
  //Exit on close
  override def closeOperation(): Unit = dispose()

  val cards = new GridPanel(1,2){
    contents += new Compress
    contents += new Extract
    hGap = 20
  }

  title = "Tarball Extractor"
  contents = cards
  centerOnScreen()
  open()
}
