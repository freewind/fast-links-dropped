package in.freewind.fastlinks.chrome_app.main

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.common.{Stars, Editable}
import in.freewind.fastlinks.{LinkGroup, Link, Project}
import in.freewind.fastlinks.chrome_app.main.profile.{ProfileLinkGroup, NewLinkGroup, EditingStars, ProfileLinks}

object ProjectProfile extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(allowEditing: Boolean, project: Project, updateProject: (Project, Project) => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    val project = props.project

    def updateDesc(desc: String) = {
      props.updateProject(project, project.copy(description = Option(desc).filterNot(_.isEmpty)))
    }

    def updateBasicLinks(newLinks: Seq[Link]): Unit = {
      val newProject = project.copy(basicLinks = newLinks)
      props.updateProject(project, newProject)
    }

    def updateProjectName(newName: String): Unit = {
      props.updateProject(project, project.copy(name = newName))
    }

    def updateStars(value: String): Unit = {
      props.updateProject(project, project.copy(stars = Some(value.toInt).filter(_ > 0)))
    }

    def newGroupName(name: String): Unit = {
      val newGroup = new LinkGroup(name, Nil)
      props.updateProject(project, project.copy(moreLinkGroups = project.moreLinkGroups :+ newGroup))
    }

    def updateLinkGroup(group: LinkGroup)(newGroup: Option[LinkGroup]): Unit = {
      newGroup match {
        case Some(g) => props.updateProject(project, project.copy(moreLinkGroups = project.moreLinkGroups.replace(group, g)))
        case _ => props.updateProject(project, project.copy(moreLinkGroups = project.moreLinkGroups.filterNot(_ == group)))
      }
    }

  }

  @scalax
  override def render(self: This) = {
    val project = self.props.project
    val allowEditing = self.props.allowEditing
    <div>
      {Editable.Input(allowEditing, Some(project.name), self.updateProjectName, Some("project-name"))}
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
        <div className="project-group">
          {ProfileLinks(ProfileLinks.Props(allowEditing, project.basicLinks, self.updateBasicLinks))}
        </div>
      }
      {
        project.moreLinkGroups.map(group => ProfileLinkGroup(ProfileLinkGroup.Props(allowEditing, group, self.updateLinkGroup(group))))
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
