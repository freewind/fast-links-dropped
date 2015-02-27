package in.freewind.fastlinks.chrome_app.main

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.chrome_app.AppBackend
import in.freewind.fastlinks.common.Dialog.DialogContext
import in.freewind.fastlinks.common.{Stars, Editable}
import in.freewind.fastlinks.{LinkGroup, Link, Project}
import in.freewind.fastlinks.chrome_app.main.profile.{ProfileLinkGroup, NewLinkGroup, EditingStars, ProfileLinks}

object ProjectProfile extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(allowEditing: Boolean, project: Project, updateProject: (Project, Project) => Unit, deleteProject: () => Unit, backend: AppBackend)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) extends MoveUpSupport {

    import self._

    val project = props.project

    def updateDesc(desc: String) = {
      props.updateProject(project, project.copy(description = Option(desc).filterNot(_.isEmpty)))
    }

    def updateProjectName(newName: String): Unit = {
      props.updateProject(project, project.copy(name = newName))
    }

    def updateStars(value: String): Unit = {
      props.updateProject(project, project.copy(stars = Some(value.toInt).filter(_ > 0)))
    }

    def newGroupName(name: String): Unit = {
      val newGroup = new LinkGroup(name, Nil)
      props.updateProject(project, project.copy(linkGroups = project.linkGroups :+ newGroup))
    }

    def updateLinkGroup(group: LinkGroup)(newGroup: Option[LinkGroup]): Unit = {
      newGroup match {
        case Some(g) => props.updateProject(project, project.copy(linkGroups = project.linkGroups.replace(group, g)))
        case _ => props.updateProject(project, project.copy(linkGroups = project.linkGroups.remove(group)))
      }
    }

    val confirmDeletion = button.onClick(e => {
      props.backend.showDialog(DialogContext("Are you sure to delete this project?", props.deleteProject))
    })

    def moveLinkUp(link: Link): Unit = {
      val groups = project.linkGroups
      updateProjectWithGroups(moveLinkUp(groups, link))
    }

    private def updateProjectWithGroups(newGroups: Seq[LinkGroup]): Unit = {
      props.updateProject(project, project.copy(linkGroups = newGroups))
    }

    def moveLinkDown(link: Link): Unit = {
      val groups = reverseAll(project.linkGroups)
      updateProjectWithGroups(reverseAll(moveLinkUp(groups, link)))
    }

    def moveGroupUp(group: LinkGroup): Unit = {
      props.updateProject(project, project.copy(linkGroups = moveUp(project.linkGroups, group)))
    }

    def moveGroupDown(group: LinkGroup): Unit = {
      props.updateProject(project, project.copy(linkGroups = moveUp(project.linkGroups.reverse, group).reverse))
    }

    private def reverseAll(groups: Seq[LinkGroup]) = {
      groups.map(g => g.copy(links = g.links.reverse)).reverse
    }

    private def moveLinkUp(groups: Seq[LinkGroup], link: Link): Seq[LinkGroup] = {
      val links = groups.flatMap(_.links :+ separateLink).init
      val movedLinks = moveUp(links, link)
      groups.zip(splitBy(movedLinks, separateLink)).map { case (g, l) => g.copy(links = l) }
    }

    private def splitBy(links: Seq[Link], sepLink: Link): Seq[Seq[Link]] = {
      def go(restLinks: Seq[Link], result: Seq[Seq[Link]]): Seq[Seq[Link]] = {
        restLinks match {
          case Nil => result
          case ll => val (left, right) = ll span (!_.eq(sepLink))
            go(if (right.isEmpty) right else right.tail, result :+ left)
        }
      }
      go(links, Nil)
    }

    private val separateLink = Link(name = Some("sep"), url = "___separator-only___")

  }

  @scalax
  override def render(self: This) = {
    import self._
    val project = self.props.project
    val allowEditing = self.props.allowEditing
    <div>
      <div>
        { Editable.Input(allowEditing, Some(project.name), self.updateProjectName, Some("project-name")) }
        {
          if (allowEditing) {
            <button onClick={self.confirmDeletion}>delete this project</button>
          } else None
        }
      </div>
      <div className="project-stars">
        {
          Editable.Input(allowEditing, None, self.updateStars,
            normalPlaceholder = Some(Stars(Stars.Props(allowEditing, project.stars))),
            editingPlaceholder = Some(EditingStars(EditingStars.Props(project.stars)))
          )
        }
      </div>
      {Editable.Textarea(allowEditing, project.description, self.updateDesc, Some("project-description"))}
      {
        project.linkGroups.map(group => ProfileLinkGroup(ProfileLinkGroup.Props(
          allowEditing, group, self.updateLinkGroup(group), self.moveLinkUp, self.moveLinkDown, () => self.moveGroupUp(group), () => self.moveGroupDown(group), props.backend
        )))
      }
      {
        if (allowEditing) {
          NewLinkGroup(NewLinkGroup.Props(self.newGroupName))
        } else {
          None
        }
      }
    </div>
  }

}
