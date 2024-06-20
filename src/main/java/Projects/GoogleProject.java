package Projects;

import Visotor.DownloadVisitor;

public class GoogleProject implements Project{

  @Override
  public boolean isUseful() {
    return false;
  }

  @Override
  public void acceptDownload(DownloadVisitor visitor) {

  }

  @Override
  public void resolveProperties() {

  }
}
