package Projects;

import static Utils.RequestUtil.makeRequestArray;

import Utils.RequestUtil;
import Visotor.DownloadVisitor;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

public class GitHubProject implements Project{

  public Platform platform = Platform.GITHUB;

  public String url;
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

  public final int thresh_latest_update = 30;
  public final int thresh_issue = 10;
  public final int thresh_star = 100;
  public final int thresh_fork = 100;
  public final int thresh_commit = 100;
  public final int thresh_pr = 10;

  public final String[] thresholds = {
    "thresh_latest_update",
    "thresh_issue",
    "thresh_star",
    "thresh_fork",
    "thresh_commit",
    "thresh_pr"
  };

  public GitHubProject(String groupName, String artifactName, String version) {
    this.groupName = groupName;
    this.artifactName = artifactName;
    this.version = version;
    this.url = "https://github.com/" + groupName + "/" + artifactName;
  }

  public boolean isUseful() {

    return true;
  }

  public void acceptDownload(DownloadVisitor visitor) {
    visitor.doGitHubProject(this);
  }

  public void resolveProperties() {
    JSONObject repoResponse = null;
    JSONArray commits = null;
    JSONArray issues = null;
    JSONArray prs = null;
    try {
      repoResponse = RequestUtil.makeRequest("https://api.github.com/repos/" + this.groupName + "/" + this.artifactName);
      commits = makeRequestArray("https://api.github.com/repos/" + this.groupName + "/" + this.artifactName + "/commits");
      issues = makeRequestArray("https://api.github.com/repos/" + this.groupName + "/" + this.artifactName + "/issues");
      prs = makeRequestArray("https://api.github.com/repos/" + this.groupName + "/" + this.artifactName + "/pulls");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    this.recently_update = repoResponse.getString("pushed_at");
    this.create_date = repoResponse.getString("created_at");
    this.open_issue_cnt = repoResponse.getInt("open_issues_count");
    this.stars_cnt = repoResponse.getInt("stargazers_count");
    this.fork_cnt = repoResponse.getInt("forks_count");
    this.language = repoResponse.optString("language");
    this.default_branch = repoResponse.getString("default_branch");
    this.commit_cnt = commits.length();
    this.issue_cnt = issues.length();
    this.pr_cnt = prs.length();
  }


}
