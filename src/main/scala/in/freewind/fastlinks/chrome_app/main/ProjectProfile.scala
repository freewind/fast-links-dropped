package in.freewind.fastlinks.chrome_app.main

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.fastlinks.common.Editable
import in.freewind.fastlinks.{LinkGroup, Link, Project}
import in.freewind.fastlinks.chrome_app.main.profile.{ProfileStars, ProfileLinks}

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

    def updateLinkGroup(group: LinkGroup)(newGroup: LinkGroup): Unit = {
      val newProject = project.copy(moreLinkGroups = project.moreLinkGroups.replace(group, newGroup))
      props.updateProject(project, newProject)
    }

    def updateProjectName(newName: String): Unit = {
      props.updateProject(project, project.copy(name = newName))
    }

    def updateStars(value: Int): Unit = {
      props.updateProject(project, project.copy(stars = Some(value)))
    }

    def updateLinksOfGroup(linkGroup: LinkGroup)(newLinks: Seq[Link]): Unit = {
      val newGroup = linkGroup.copy(links = newLinks)
      props.updateProject(project, project.copy(moreLinkGroups = project.moreLinkGroups.replace(linkGroup, newGroup)))
    }

    def updateGroupName(group: LinkGroup)(newName: String): Unit = {
      val newGroup = group.copy(name = newName)
      props.updateProject(project, project.copy(moreLinkGroups = project.moreLinkGroups.replace(group, newGroup)))
    }

  }

  @scalax
  override def render(self: This) = {
    val project = self.props.project
    val allowEditing = self.props.allowEditing
    <div>
      {Editable.Input(allowEditing, project.name, self.updateProjectName)}
      {ProfileStars(ProfileStars.Props(allowEditing, project.stars, self.updateStars))}
      {Editable.Textarea(allowEditing, project.description.getOrElse("..."), self.updateDesc)}
      {
        <div className="project-group">
          {ProfileLinks(ProfileLinks.Props(allowEditing, project.basicLinks, self.updateBasicLinks))}
        </div>
      }
      {
        project.moreLinkGroups.map(group =>
          <div className="project-group">
            {Editable.Input(allowEditing, group.name, self.updateGroupName(group))}
            {ProfileLinks(ProfileLinks.Props(allowEditing, group.links, self.updateLinksOfGroup(group)))}
          </div>
        )
      }
    </div>
  }

}
