package Utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class ThreadedStreamHandler extends Thread {

  private final Logger logger = LogManager.getLogger(this.getClass());

  InputStream inputStream;
  StringBuilder outputBuffer = new StringBuilder();

  ThreadedStreamHandler(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public void run() {
    BufferedReader bufferedReader = null;
    try {
      bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      String line = null;
      while ((line = bufferedReader.readLine()) != null) {
        outputBuffer.append(line);
        outputBuffer.append(System.lineSeparator());
      }
    } catch (IOException ioe) {
      logger.error("Error: " + ioe.getMessage());
    } finally {
      try {
        bufferedReader.close();
      } catch (IOException e) {
        // ignore this one
      }
    }
  }

  public StringBuilder getOutputBuffer() {
    return outputBuffer;
  }
}