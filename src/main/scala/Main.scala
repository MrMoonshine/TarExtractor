

object Main{
  def main(args: Array[String]): Unit = {
    val filegz:String = "assets/MorseserverThreaded_0.tar.gz"
    val fileoida:String = "assets/targarbage.tar.gz"
    val filexz:String = "assets/test.tar.xz"

    new UI

    //println("Extracting an Tar Gz archive...")
    //val tgz1 = new TarGz(filegz)
    //tgz1.dumpInto(new File("assets/dump/").toPath)

    //println("Extracting an Tar Xz archive...")
    //val txz1 = new TarXz(filexz)
    //txz1.dumpInto(new File("assets/dump/").toPath)
  }
}