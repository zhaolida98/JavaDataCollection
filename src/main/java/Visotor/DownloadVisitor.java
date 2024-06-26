package Visotor;

import Config.ConfigManager;
import Projects.GitHubProject;
import Projects.GitLabProject;
import Projects.GoogleProject;
import Projects.LocalProject;
import Projects.MvnCentralProject;
import Utils.CommandUtil;
import Utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.lingala.zip4j.ZipFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.errors.NoRemoteRepositoryException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class DownloadVisitor implements Visitor {

    private static final Logger logger = LogManager.getLogger(DownloadVisitor.class);
    ConfigManager configManager = ConfigManager.getInstance();


    public void doGitHubProject(GitHubProject project) {
        String dataRoot = Paths.get(configManager.getDataRoot(), "github").toString();
        String groupId = project.groupName;
        String artifactId = project.artifactName;
        String projName = groupId + "@" + artifactId;
        Path groupFolder = Paths.get(dataRoot, projName);
        try {
            if (Files.exists(groupFolder)) {
                logger.info("Folder already exists");
                return;
            }
//      FileUtil.deleteDirectoryRecursively(groupFolder);
            CredentialsProvider cp = new UsernamePasswordCredentialsProvider("PRIVATE-TOKEN",
                    configManager.getGitHubToken());

            Git.cloneRepository()
                    .setCredentialsProvider(cp)
                    .setURI(project.url)
                    .setDirectory(groupFolder.toFile())
                    .call();
            logger.info("finish downloading " + project.url);
            Thread.sleep(750);
        } catch (InvalidRemoteException e) {
            logger.warn(project.url + " is unavailable");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doMvnCentralProject(MvnCentralProject project) {
        String dataRoot = Paths.get(configManager.getDataRoot(), "mvn_central").toString();
        String groupId = project.groupId;
        String artifactId = project.artifactId;
        String version = project.version;
        String sourceJarName = groupId + "@" + artifactId + "@" + version;
        String projName = groupId + "@" + artifactId;
        Path groupFolder = Paths.get(dataRoot, projName);



        // 1. prepare folder
        try {
            if (!Files.exists(groupFolder)) {
                Files.createDirectories(groupFolder);
                Git.init().setDirectory(groupFolder.toFile()).call();
            }
        } catch (Exception e) {
            logger.error("Error in creating folder: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // 2. list all version tag and write to a file "version.txt"
        logger.info("Listing tags");
        try {
            Git git = Git.open(groupFolder.toFile());
            List<String> tags = git.tagList().call().stream().map(Ref::getName)
                    .map(s -> s.replace("refs/tags/", "")).collect(Collectors.toList());
            if (tags.contains(version)) {
                logger.info("Tag already exists");
                return;
            }
            tags.add(version);
            Path versionFile = Paths.get(groupFolder.toString(), "version.txt");
            Files.write(versionFile, tags);
        } catch (Exception e) {
            logger.error("Error in listing tags: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // 3. delete all files in the folder except .git
        try {
            Files.list(groupFolder).forEach(path -> {
                // Check if the path is the .git directory
                if (!path.getFileName().toString().equals(".git") && !path.getFileName().toString().equals("version.txt")) {
                    try {
                        FileUtil.deleteDirectoryRecursively(path);
                    } catch (IOException e) {
                        logger.error("Error in deleting folder: " + e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (IOException e) {
            logger.error("Error in listing files: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        //4. download
        String downloadJarPath = String.format("%s/.m2/repository/%s/%s/%s/%s",
                System.getProperty("user.home"), groupId.replace(".", File.separator),
                artifactId, version, String.format("%s-%s-sources.jar", artifactId, version));
        logger.info(downloadJarPath);
        List<String> cmd = Arrays.asList(
                "mvn",
                "dependency:get",
                String.format("-Dartifact=%s:%s:%s:jar:sources", groupId, artifactId, version),
                "-Dtransitive=false");

        try {
            CommandUtil.runExecutableWithArgs(cmd, null);
        } catch (Exception e) {
            return;
        }

        // 5. unzip the downloaded file to the folder
        logger.info("Unzip jars");
        try {
            while (!Files.exists(Paths.get(downloadJarPath))) {
                Thread.sleep(3000);
                logger.info("Waiting for downloading to unzip file: " + downloadJarPath);
            }
            //    unzip -o {download_dst_path}/{lib} -d {decompose_dst_path}/{lib.replace('.jar', '')}""
            ZipFile zipFile = new ZipFile(downloadJarPath);
            zipFile.extractAll(groupFolder.toString());
        } catch (Exception e) {
            logger.error("Error in unzipping file: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // 6. commit the changes and set version tag
        logger.info("Committing changes");
        try {
            Git git = Git.open(groupFolder.toFile());
            git.add().addFilepattern(".").call();
            git.commit().setMessage(version).call();
            git.tag().setName(version).call();
        } catch (Exception e) {
            logger.error("Error in committing changes: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        logger.info("Finish downloading mvn central project");
    }

    public void doGitLabProject(GitLabProject project) {
        // TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void doGoogleCodeProject(GoogleProject project) {
        // TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void doLocalProject(LocalProject project) {
        // TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
