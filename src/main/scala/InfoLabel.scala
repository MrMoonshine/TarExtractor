import scala.swing.Label

class InfoLabel(descr:String, path:String) extends Label(descr + path){
  def alterValue(vlu:String): Unit ={
    text = descr + vlu
  }
}
