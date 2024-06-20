package Config;

public class ConfigManager {

  public static ConfigManager configManager;

  public static ConfigManager getInstance() {
    if (configManager == null) {
      configManager = new ConfigManager();
    }
    return configManager;
  }


  public String getGitHubToken() {
    return "";
  }



}
