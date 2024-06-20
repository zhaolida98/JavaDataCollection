package Projects;

import Visotor.DownloadVisitor;

public interface Project {

  Platform platform = null;

  String groupName = null;
  String artifactName = null;
  String version = null;

  boolean isUseful();

  void acceptDownload(DownloadVisitor visitor);

  void resolveProperties();

}
