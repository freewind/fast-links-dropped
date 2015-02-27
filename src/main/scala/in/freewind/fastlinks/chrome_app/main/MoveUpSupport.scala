package in.freewind.fastlinks.chrome_app.main

trait MoveUpSupport {
  def moveUp[T](list: Seq[T], item: T): Seq[T] = {
    list.span(_ != item) match {
      case (Nil, right) => right
      case (left, right) => left.init ++ Seq(right.head, left.last) ++ right.tail
    }
  }
}
