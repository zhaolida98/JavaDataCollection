package Utils;

import Config.ConfigManager;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class RequestUtil {
  private static final OkHttpClient client = new OkHttpClient();
  private static ConfigManager configManager = ConfigManager.getInstance();

  public static JSONObject makeRequest(String url) throws IOException {
    Request request = new Request.Builder()
        .url(url)
        .header("Authorization", "token " + configManager.getGitHubToken())
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
      return new JSONObject(response.body().string());
    }
  }

  public static JSONArray makeRequestArray(String url) throws IOException {
    Request request = new Request.Builder()
        .url(url)
        .header("Authorization", "token " + configManager.getGitHubToken())
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
      return new JSONArray(response.body().string());
    }
  }

}
