package Projects;

import Visotor.DownloadVisitor;

public class MvnCentralProject implements Project{

  public Platform platform = Platform.MVN_CENTRAL;

  public String groupId;
  public String artifactId;
  public String version;

  public MvnCentralProject(String groupId, String artifactId, String version) {
    this.groupId = groupId;
    this.artifactId = artifactId;
    this.version = version;
  }

  public boolean isUseful() {
    return true;
  }

  public void acceptDownload(DownloadVisitor visitor) {
    visitor.doMvnCentralProject(this);
  }

  @Override
  public void resolveProperties() {

  }

}
