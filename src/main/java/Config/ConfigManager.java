package Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

  public static ConfigManager configManager;

  private String githubToken;
  private String dataRootPath;

  private ConfigManager() {
    Properties props = new Properties();
    try (InputStream input = new FileInputStream("src/main/resources/config.properties")) { // Adjust the path if necessary
      // Load the properties file
      props.load(input);

      // Retrieve properties
      githubToken = props.getProperty("github_token");
      dataRootPath = props.getProperty("data_root_path");

    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public static ConfigManager getInstance() {
    if (configManager == null) {
      configManager = new ConfigManager();
    }
    return configManager;
  }


  public String getGitHubToken() {
    return githubToken;
  }

  public String getDataRoot() {
    return dataRootPath;
  }





}
