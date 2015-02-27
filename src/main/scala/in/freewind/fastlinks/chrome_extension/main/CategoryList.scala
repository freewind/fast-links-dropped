package in.freewind.fastlinks.chrome_extension.main

import com.xored.scalajs.react.{scalax, TypedReactSpec}
import com.xored.scalajs.react.util.TypedEventListeners
import in.freewind.fastlinks.Category

object CategoryList extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(categories: Seq[Category])

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    val onChange = input.onChange(e => {
      println(e.value)
    })
  }

  @scalax
  override def render(self: This) = {
    <div>
      {
        self.props.categories.map { category =>
          <div>
            <div>{category.name}</div>
            {
              ProjectList(ProjectList.Props(category.projects))
            }
          </div>
        }
      }
    </div>
  }

}
