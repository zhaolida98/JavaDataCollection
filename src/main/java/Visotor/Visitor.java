package Visotor;

import Projects.GitHubProject;
import Projects.GitLabProject;
import Projects.GoogleProject;
import Projects.LocalProject;

public interface Visitor {

  void doGitHubProject(GitHubProject project);
  void doGitLabProject(GitLabProject project);
  void doGoogleCodeProject(GoogleProject project);
  void doLocalProject(LocalProject project);

}
