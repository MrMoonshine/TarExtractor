package backend

sealed trait ArchiverError {
  val e:Throwable
}
