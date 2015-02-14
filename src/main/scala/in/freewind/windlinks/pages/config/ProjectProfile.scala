package in.freewind.windlinks.pages.config

import com.xored.scalajs.react.util.TypedEventListeners
import com.xored.scalajs.react.{TypedReactSpec, scalax}
import in.freewind.windlinks.pages.common.Editable
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

    def updateLinksOfGroup(linkGroup: LinkGroup)(newLinks: Seq[Link]): Unit = {
      val newGroup = linkGroup.copy(links = newLinks)
      props.updateProject(project, project.copy(moreLinkGroups = project.moreLinkGroups.replace(linkGroup, newGroup)))
    }

    def updateGroupName(group: LinkGroup)(newName: String): Unit = {
      val newGroup = group.copy(name = newName)
      props.updateProject(project, project.copy(moreLinkGroups = project.moreLinkGroups.replace(group, newGroup)))
    }

  }

  override def render(self: This) = {
    @scalax val workaround = {
      val project = self.props.project

      val name = Editable.Input(project.name, self.updateProjectName)
      val desc = Editable.Textarea(project.description.getOrElse("..."), self.updateDesc)
      val basicLinks = {
        <div className="project-group">
          {ProfileLinks(ProfileLinks.Props(project.basicLinks, self.updateBasicLinks))}
        </div>
      }
      val moreGroups = project.moreLinkGroups.map(g =>
        <div className="project-group">
          {Editable.Input(g.name, self.updateGroupName(g))}
          {ProfileLinks(ProfileLinks.Props(g.links, self.updateLinksOfGroup(g)))}
        </div>
      )

      <div>
        {name}{desc}{basicLinks}{moreGroups}
      </div>
    }
    workaround
  }

}
