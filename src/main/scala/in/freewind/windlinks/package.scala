package in.freewind

package object windlinks {

  implicit class RichSeq[T](seq: Seq[T]) {
    def replace(old: T, newOne: T): Seq[T] = {
      seq.map {
        case item if item == old => newOne
        case other => other
      }
    }
  }

}
