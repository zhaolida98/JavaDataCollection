package Projects;

import Visotor.DownloadVisitor;

public class LocalProject implements Project{

  public Platform platform = Platform.Local;

  public String path;
  public String groupName;
  public String artifactName;
  public String version;

  public String name;
  public String size;
  public String recently_update;
  public String create_date;
  public int open_issue_cnt;
  public int stars_cnt;
  public int fork_cnt;
  public int commit_cnt;
  public int issue_cnt;
  public int pr_cnt;
  public String language;
  public String default_branch;

  public LocalProject(String groupName, String artifactName, String path) {
    this.groupName = groupName;
    this.artifactName = artifactName;
    this.path = path;
  }

  public boolean isUseful() {
    return true;
  }

  public void acceptDownload(DownloadVisitor visitor) {
    visitor.doLocalProject(this);
  }

  @Override
  public void resolveProperties() {

  }

}
