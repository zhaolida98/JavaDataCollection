package Projects;

public class ProjectFactory {

  public static Project createProject(Platform platform, String groupName, String artifactName, String version) {
    if (platform == Platform.MVN_CENTRAL) {
      return new MvnCentralProject(groupName, artifactName, version);
    } else if (platform == Platform.GITHUB) {
      return new GitHubProject(groupName, artifactName, version);
    } else {
      return null;
    }
  }

}
