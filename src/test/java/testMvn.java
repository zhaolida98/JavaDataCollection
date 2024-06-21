import Projects.GitHubProject;
import Projects.MvnCentralProject;
import Visotor.DownloadVisitor;
import org.junit.Test;

public class testMvn {
  @Test
  public void testMvn() {
    MvnCentralProject mvnCentralProject = new MvnCentralProject("com.google.guava", "guava", "23.0");
    DownloadVisitor downloadVisitor = new DownloadVisitor();
    mvnCentralProject.acceptDownload(downloadVisitor);
    System.out.println("Test mvn");
  }

  @Test
  public void testGitHub() {
    GitHubProject gitHubProject = new GitHubProject("zhaolida98", "Flight", "");
    DownloadVisitor downloadVisitor = new DownloadVisitor();
    gitHubProject.acceptDownload(downloadVisitor);
    System.out.println("Test mvn");
  }

}
