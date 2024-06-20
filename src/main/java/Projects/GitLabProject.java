package Projects;

import Visotor.DownloadVisitor;

public class GitLabProject implements Project{

  public Platform platform = Platform.GITLAB;

  public String url;
  public String groupName;
  public String artifactName;
  public String version;

  public GitLabProject(String groupName, String artifactName, String version) {
    this.groupName = groupName;
    this.artifactName = artifactName;
    this.version = version;
    this.url = "https://gitlab.com/" + groupName + "/" + artifactName;
  }

  public boolean isUseful() {
    return true;
  }

  public void acceptDownload(DownloadVisitor visitor) {
    visitor.doGitLabProject(this);
  }

  @Override
  public void resolveProperties() {

  }

}
