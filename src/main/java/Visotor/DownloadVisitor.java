package Visotor;

import Config.ConfigManager;
import Projects.GitHubProject;
import Projects.GitLabProject;
import Projects.GoogleProject;
import Projects.LocalProject;
import Projects.Project;

public class DownloadVisitor implements Visitor{

    ConfigManager configManager = ConfigManager.getInstance();

    public void doGitHubProject(GitHubProject project) {
      // TODO implement this method
      throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void doGitLabProject(GitLabProject project) {
      // TODO implement this method
      throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void doGoogleCodeProject(GoogleProject project) {
      // TODO implement this method
      throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void doLocalProject(LocalProject project) {
      // TODO implement this method
      throw new UnsupportedOperationException("Not implemented yet.");
    }

}
