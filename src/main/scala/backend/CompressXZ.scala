package backend

import org.apache.commons.compress.archivers.tar.{TarArchiveEntry, TarArchiveOutputStream}
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream
import org.apache.commons.io.IOUtils

import java.io.{BufferedOutputStream, File, FileInputStream}
import java.nio.file.Files

class CompressXZ(indir: File, destination: File) {
  private def sorter(f1:File,f2:File) ={
    if(f1.isDirectory ^ f2.isDirectory){
      f1.isDirectory
    }else{
      false
    }
  }

  private def listDir(dir: File): List[File] = {
    println(s"[XZ] Listing Directory: ${dir.toString}")
    if (!dir.isDirectory) {
      println("[XZ] Info. You haven't selected a directory. Hence, the archive will only contain a single file.")
      return List[File](dir)
    }
    val these = dir.listFiles()
    val rlis = (these ++ these.filter(_.isDirectory).flatMap(listDir)).toList
    rlis.sortWith(sorter)
  }

  val ostream = Files.newOutputStream(destination.toPath)
  val buffout = new BufferedOutputStream(ostream)
  val xzout = new XZCompressorOutputStream(buffout)
  val tout:TarArchiveOutputStream = new TarArchiveOutputStream(xzout) {

  val basename = indir.getName

    for (inf <- listDir(indir)) {
      val entry = new TarArchiveEntry(inf.getAbsoluteFile, basename + "/" + indir.toPath.relativize(inf.toPath).toString)
      println(s"Entry: ${entry.getName} will be compressed from ${entry.getFile.toString}")
      //println(s"relativized: ${indir.toPath.relativize(inf.toPath)}")
      putArchiveEntry(entry)
      if (!Files.isDirectory(inf.toPath)) {
        val fistream:FileInputStream = new FileInputStream(inf)
        IOUtils.copy(fistream, this)
        fistream.close()
      }
      closeArchiveEntry()
    }
  }
  tout.finish()
  tout.close()
  buffout.close()
  ostream.close()
}
