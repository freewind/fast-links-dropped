package in.freewind.windlinks.pages.config

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.windlinks.{LinkGroup, Link, Project}
import in.freewind.windlinks.pages.config.profile.ProfileLinks

object ProjectProfile extends TypedReactSpec with TypedEventListeners {

  case class State()

  case class Props(project: Project, updateProject: (Project, Project) => Unit)

  override def getInitialState(self: This) = State()

  implicit class Closure(self: This) {

    import self._

    val project = props.project

    def updateDesc(desc: String) = {
      props.updateProject(project, project.copy(description = Option(desc).filterNot(_.isEmpty)))
    }

    def updateBasicLink(oldLink: Link, newLink: Link): Unit = {
      val newProject = project.copy(basicLinks = project.basicLinks.replace(oldLink, newLink))
      props.updateProject(project, newProject)
    }

    def updateLinkInGroup(group: LinkGroup)(oldLink: Link, newLink: Link): Unit = {
      val newGroup = group.copy(links = group.links.replace(oldLink, newLink))
      val newProject = project.copy(moreLinkGroups = project.moreLinkGroups.replace(group, newGroup))
      props.updateProject(project, newProject)
    }

    def updateProjectName(newName: String): Unit = {
      props.updateProject(project, project.copy(name = newName))
    }
  }

  @scalax
  override def render(self: This) = {
    val project = self.props.project

    val name = ProjectName(ProjectName.Props(project.name, self.updateProjectName))
    val desc = ProjectDescription(ProjectDescription.Props(project.description, self.updateDesc))
    val basicLinks = ProfileLinks(ProfileLinks.Props(None, project.basicLinks, self.updateBasicLink))
    val moreGroups = project.moreLinkGroups.map(g => ProfileLinks(ProfileLinks.Props(Some(g.name), g.links, self.updateLinkInGroup(g))))

    <div>
      {name}{desc}{basicLinks}{moreGroups}
    </div>
  }

}
