package Utils;

import java.util.Arrays;
import java.util.List;

public class ExecutableOutput {

  private int returnCode = 0;
  private final String standardOutput;
  private final String errorOutput;

  public ExecutableOutput(
      final int returnCode, final String standardOutput, final String errorOutput) {
    this.returnCode = returnCode;
    this.standardOutput = standardOutput;
    this.errorOutput = errorOutput;
  }

  public ExecutableOutput(final String standardOutput, final String errorOutput) {
    this(0, standardOutput, errorOutput);
  }

  public List<String> getStandardOutputAsList() {
    return Arrays.asList(standardOutput.split(System.lineSeparator()));
  }

  public List<String> getErrorOutputAsList() {
    return Arrays.asList(errorOutput.split(System.lineSeparator()));
  }

  public String getStandardOutput() {
    return standardOutput;
  }

  public String getErrorOutput() {
    return errorOutput;
  }

  public int getReturnCode() {
    return returnCode;
  }

  @Override
  public String toString() {
    // print only brief of std and err output
    String standardOutputBrief = standardOutput;
    if (standardOutput.length() > 100) {
      standardOutputBrief = standardOutput.substring(0, 100);
    }
    String errorOutputBrief = errorOutput;
    if (errorOutput.length() > 100) {
      errorOutputBrief = errorOutput.substring(0, 100);
    }
    return "ExecutableOutput{"
        + "returnCode="
        + returnCode
        + ", standardOutput='"
        + standardOutputBrief
        + '\''
        + ", errorOutput='"
        + errorOutputBrief
        + '\''
        + '}';
  }
}
