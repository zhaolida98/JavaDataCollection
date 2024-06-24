import Projects.Platform;
import Projects.Project;
import Projects.ProjectFactory;
import Visotor.DownloadVisitor;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

  private static Logger logger = LogManager.getLogger(Main.class);

  public static void main(String[] args) {
    String inputCSV = "/home/nryet/JavaDataCollection/src/test/java/mvncentrorepo_test.csv";
    Platform platform = Platform.MVN_CENTRAL;

    // Create a fixed thread pool based on the number of available processors
    // int cores = Runtime.getRuntime().availableProcessors();
    int cores = 4;
    ExecutorService executor = Executors.newFixedThreadPool(cores);

    try (Reader reader = new FileReader(inputCSV)) {
      CSVReaderHeaderAware csvReader = new CSVReaderHeaderAware(reader);

      Map<String, String> record;
      while ((record = csvReader.readMap()) != null) {
        final Map<String, String> finalRecord = record; // Needed for use in lambda
        Runnable task = () -> {
          String groupName = finalRecord.get("group");
          String artifactName = finalRecord.get("artifact");
          String version = finalRecord.get("version");
          if (version.contains(":")) {
            String[] versionList = version.split(":");
            for (String v : versionList) {
              Project p = ProjectFactory.createProject(platform, groupName, artifactName, v);
              DownloadVisitor visitor = new DownloadVisitor();
              p.acceptDownload(visitor);
            }
          } else {
            Project p = ProjectFactory.createProject(platform, groupName, artifactName, version);
            DownloadVisitor visitor = new DownloadVisitor();
            p.acceptDownload(visitor);
          }
        };
        executor.submit(task);
      }
    } catch (IOException e) {
      logger.error("Error reading CSV file");
      e.printStackTrace();
    } catch (CsvException e) {
      logger.error("Error parsing CSV file");
      e.printStackTrace();
    } finally {
      executor.shutdown();
      try {
        // Wait for all tasks to finish
        if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
          executor.shutdownNow();
        }
      } catch (InterruptedException e) {
        executor.shutdownNow();
      }
    }
  }
}
