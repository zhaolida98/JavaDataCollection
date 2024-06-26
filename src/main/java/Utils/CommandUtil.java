package Utils;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;

public class CommandUtil {
  private static final Logger logger = LogManager.getLogger(CommandUtil.class);

  public static ExecutableOutput runExecutableWithArgs(List<String> cmds, File working_dir) throws Exception {
    if (working_dir == null) {
      working_dir = new File(System.getProperty("user.dir"));
    }
    try {
      final ProcessBuilder pb = new ProcessBuilder(cmds).directory(working_dir);
      final Process process = pb.start();
      InputStream inputStream = process.getInputStream();
      InputStream errorStream = process.getErrorStream();
      ThreadedStreamHandler inputStreamHandler = new ThreadedStreamHandler(inputStream);
      ThreadedStreamHandler errorStreamHandler = new ThreadedStreamHandler(errorStream);
      inputStreamHandler.start();
      errorStreamHandler.start();

      process.waitFor();

      int exitCode = process.exitValue();
      logger.info("finish execution with exit code: " + exitCode);

      inputStreamHandler.interrupt();
      errorStreamHandler.interrupt();
      inputStreamHandler.join();
      errorStreamHandler.join();

      String stdout = inputStreamHandler.getOutputBuffer().toString().trim();
      String stderr = errorStreamHandler.getOutputBuffer().toString().trim();

      ExecutableOutput result = new ExecutableOutput(exitCode, stdout, stderr);
      logger.debug("finish execution: " + result.getStandardOutput());
      return result;
    } catch (final Exception e) {
      logger.error("Error in commands: \n{}", ExceptionUtils.getMessage(e) + ExceptionUtils.getStackTrace(e));
      throw new Exception("Error in command execution");
    }
  }

}
