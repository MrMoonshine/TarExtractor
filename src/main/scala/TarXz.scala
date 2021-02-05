import org.apache.commons.compress.compressors.CompressorInputStream
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream

import java.io.InputStream
import scala.util.{Success, Try}

class TarXz(filename:String) extends TarGz(filename){
  override protected def uncompress(cstream: InputStream): Either[UnarchiverError, CompressorInputStream] = {
    Try {
      //Just in case the input stream has been tampered
      new XZCompressorInputStream(cstream)
      //new CompressorStreamFactory()
      //  .createCompressorInputStream(new BufferedInputStream(xzstream))
    } match {
      case Success(stream) => Right(stream)
      //case Failure(err: CompressorException) => Left(CompressorError(err))
      //case Failure(err) => Left(UnexpectedUnarchiverError(err))
    }
  }
}
