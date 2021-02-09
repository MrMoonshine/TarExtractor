package backend

import org.apache.commons.compress.archivers.{ArchiveEntry, ArchiveException, ArchiveInputStream, ArchiveStreamFactory}
import org.apache.commons.compress.compressors.{CompressorException, CompressorInputStream, CompressorStreamFactory}
import org.apache.commons.io.input.CloseShieldInputStream

import java.io._
import java.nio.file.{Files, Path}
import scala.util.{Failure, Success, Try}

class TarGz(filename: String){
  /*-----------------------------------------------------
    Constructing Object
  ----------------------------------------------------- */
  val ifstream: FileInputStream = new FileInputStream(filename)
  //Creates an appropriate Iterator
  private def getIter(astream: ArchiveInputStream): Iterator[(ArchiveEntry, InputStream)] = {
    new Iterator[(ArchiveEntry, InputStream)] {
      private var latest: ArchiveEntry = _

      override def hasNext: Boolean = {
        latest = astream.getNextEntry
        latest != null
      }

      override def next(): (ArchiveEntry, InputStream) = (
        latest,
        new CloseShieldInputStream(astream)
      )
    }
  }

  //Uncompress the archive
  protected def uncompress(cstream: InputStream): Either[UnarchiverError, CompressorInputStream] = {
    Try {
      //Just in case the input stream has been tampered
      new CompressorStreamFactory()
        .createCompressorInputStream(new BufferedInputStream(cstream))
    } match {
      case Success(stream) => Right(stream)
      case Failure(err: CompressorException) => Left(CompressorError(err))
      case Failure(err) => Left(UnexpectedUnarchiverError(err))
    }
  }

  //Extract the archive
  private def extract(estream: InputStream): Either[UnarchiverError, ArchiveInputStream] = {
    Try {
      //Just in case the input stream has been tampered
      new ArchiveStreamFactory()
        .createArchiveInputStream(new BufferedInputStream(estream))
    } match {
      case Success(stream) => Right(stream)
      case Failure(err: ArchiveException) => Left(ArchiveFormatError(err))
      case Failure(err) => Left(UnexpectedUnarchiverError(err))
    }
  }

  //Open the Tar archive
  def open(): Either[UnarchiverError, Iterator[(ArchiveEntry, InputStream)]] = for {
    uncompressedInputStream <- uncompress(ifstream)
    archiveIstream <- extract(uncompressedInputStream)
    iterator = getIter(archiveIstream)
  } yield iterator

  //Dump a the GZ archive into a desired directory
  def dumpInto(destination: Path): Unit = {
    val it = open()

    try {
      Files.createDirectories(destination)
      if (!Files.isDirectory(destination)) throw new Exception("Destination must be a Directory")

      it match {
        case Left(x) => println("backend.UnarchiverError occured")
        case Right(x) => {
          println("Created Iterator")
          while (x.hasNext) {
            val tupItEl = x.next()

            //split tuple for readability
            val finfo: ArchiveEntry = tupItEl._1
            val istream: InputStream = tupItEl._2
            //Create Absolute Paths for all of the containing files
            val dumpf: Path = destination.resolve(finfo.getName).toAbsolutePath
            val destname: String = dumpf.toString

            if (finfo.isDirectory) {
              println(s"$destname is a directory")
              if (!Files.exists(dumpf)) Files.createDirectories(dumpf)
            } else {
              println(s"Extracting to: $destname")
              //println(s"Stream has ${istream.available()} Bytes available")

              //Copy Stream to a file of any type
              val bos = new BufferedOutputStream(new FileOutputStream(new File(destname)))
              istream.reset()
              LazyList.continually(istream.read)
                .takeWhile(_ != -1)
                .foreach(bos.write(_))
              bos.close()
            }
            istream.close()
          }
        }
      }
    } catch {
      case e: OutOfMemoryError => println("Out of Memory! File too big. " + e.getMessage)
      case e: EOFException => println("Malformed EOF " + e.getMessage)
      case e: IOException => println("IO Error! Nothing will be dumped. " + e.getMessage)
      case e: Exception => println(e.getMessage)
      case e: Throwable => println("Unknown Error: " + e.getMessage)
    }
  }
}