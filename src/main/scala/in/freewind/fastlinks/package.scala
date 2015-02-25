package in.freewind

package object fastlinks {

  implicit class RichSeq[T](seq: Seq[T]) {
    def replace(old: T, newOne: T): Seq[T] = {
      seq.map {
        case item if item == old => newOne
        case other => other
      }
    }
  }

  implicit class RichString(raw: String) {
    def empty2option: Option[String] = Option(raw).filterNot(_.isEmpty)
  }

  implicit class RichAny(raw: Any) {
    def unit(): Unit = ()
  }

}
