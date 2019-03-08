package com.example;

import com.jcabi.github.*;
import org.apache.commons.codec.binary.Base64;

import javax.json.Json;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A class that tests automated pull requests using the jcabi github api.
 */
public class GithubPullRequestTest {

    private static final String FILE_NAME = "/src/com/example/GithubPullRequestTest.java";
    private static final String HEAD = "testGithubPR";
    private static final String BASE = "master";
    private static final String COORDINATES = "jmoh3/adventure";

    public static void main(String[] args) {
        try {
            String oath = getOathKey();
            System.out.println(oath);
            createPullRequest("Testing automated pull requests", HEAD, BASE, COORDINATES, FILE_NAME, oath);
        } catch (Exception e) {
            System.out.println("Couldn't retrieve oath");
        }
    }

    /**
     * Creates a pull request for changes in a given file.
     *
     * @param pullRequestName name for pull request.
     * @param head branch that we are currently working in.
     * @param base branch that we want to merge into.
     * @param repoCoordinates coordinates of repository.
     * @param oath oath token (used for authentication with api).
     */
    private static void createPullRequest(String pullRequestName, String head, String base, String repoCoordinates, String path, String oath) {
        Github github = new RtGithub(oath);
        Repo repo = github.repos().get(
                new Coordinates.Simple(repoCoordinates)
        );

        final String sha;
        Contents contents = repo.contents();

        boolean exists = false;

        try {
            exists = contents.exists(path, head);
        } catch (Exception e) {

        }

        File file = new File(path);

        if (exists) {

            boolean success = commitAndPullRequest(repo, path, head, base, pullRequestName, file);

            if (success) {
                System.out.println("Pull Request Successful");
            } else {
                System.out.println("Pull Request Unsuccessful");
            }

        } else {
            System.out.println("Creating new file");

            boolean success = commitAndPullNewFile(repo, path, head, base, pullRequestName, file);

            if (success) {
                System.out.println("Pull Request Successful");
            } else {
                System.out.println("Pull Request Unsuccessful");
            }
        }
    }

    /**
     * Generates a string describing the pull request to be made using template.
     *
     * @param testName name of flaky test we are fixing.
     * @param changelog changelog.
     * @return String to be used for pull comment.
     */
    private static String generatePullComment(String testName, String changelog) {
        String title = "## What is the purpose for this change?";

        String purpose = "\n Fixing flaky test " +  testName;

        String changeLog = "\n ## Brief changelog \n " + changelog;

        System.out.println(title + purpose + changeLog);
        return title + purpose + changeLog;
    }

    /**
     * Helper that reads a file as string.
     *
     * @param fileName name of file to be read.
     * @return String of contents of file.
     * @throws Exception
     */
    private static String readFileAsString(String fileName) throws Exception
    {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }

    /**
     * Gets the oath key.
     *
     * @return oath key.
     * @throws Exception
     */
    private static String getOathKey() throws Exception {
        return readFileAsString("/Users/jackieoh/IdeaProjects/adventure/APIKey.txt");
    }

    /**
     * Converts a given file to a byte array.
     *
     * @param file file to use.
     * @return byte array.
     */
    private static byte[] convertToByteArray(File file)
    {
        byte[] fileBytes = new byte[(int) file.length()];
        try(FileInputStream inputStream = new FileInputStream(file))
        {
            inputStream.read(fileBytes);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return fileBytes;
    }

    /**
     * Generates commit and pull request for file that already exists in github repository.
     *
     * @param repo repository we are using.
     * @param path path to file in local repository.
     * @param pullRequestName name of pull requrest.
     * @param head branch that we are currently working in.
     * @param base branch that we want to merge into.
     * @param file file we changed.
     * @return true if succeeded, false otherwise.
     */
    private static boolean commitAndPullRequest(Repo repo, String path, String pullRequestName, String head, String base, File file) {
        try {
            Content pathContent = repo.contents().get(path, head);
            final Content.Smart content = new Content.Smart(pathContent);
            String sha = content.sha();

            byte[] fileContent = Files.readAllBytes(file.toPath());

            final String enc = Base64.encodeBase64String(content.decoded());
            RepoCommit newCommit = repo.contents().update(
                    path,
                    Json.createObjectBuilder()
                            .add("message", "hello!")
                            .add("content", enc)
                            .add("sha", sha)
                            .add("branch", head)
                            .add(
                                    "committer",
                                    Json.createObjectBuilder()
                                            .add("name", "Jackie Oh")
                                            .add("email", "jmoh3@illinois.edu")
                            )
                            .build()
            );

            Pulls pulls = repo.pulls();

            if (!generatePullComment(pulls, newCommit, head,  base, path, pullRequestName)) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * Generates commit and pull request for file that does not exist in github repository.
     *
     * @param repo repository we are using.
     * @param path path to file in local repository.
     * @param pullRequestName name of pull requrest.
     * @param head branch that we are currently working in.
     * @param base branch that we want to merge into.
     * @param file file we changed.
     * @return true if succeeded, false otherwise.
     */
    private static boolean commitAndPullNewFile(Repo repo, String path, String head, String base, String pullRequestName, File file) {
        try {
            byte[] fileContent = convertToByteArray(file);

            final String enc = Base64.encodeBase64String(fileContent);

            Content newContent = repo.contents().create(
                    Json.createObjectBuilder()
                            .add("message", "hello!")
                            .add("content", enc)
                            .add("branch", head)
                            .add("path", path)
                            .build()
            );

            Content.Smart smartContent = new Content.Smart(newContent);

            RepoCommit newCommit = repo.contents().update(
                    path,
                    Json.createObjectBuilder()
                            .add("message", "Added new file!")
                            .add("content", enc)
                            .add("sha", smartContent.sha())
                            .add("branch", head)
                            .add(
                                    "committer",
                                    Json.createObjectBuilder()
                                            .add("name", "Jackie Oh")
                                            .add("email", "jmoh3@illinois.edu")
                            )
                            .build()
            );

            Pulls pulls = repo.pulls();
            if (!generatePullComment(pulls, newCommit, head,  base, path, pullRequestName)) {
                return false;
            }

        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }

        return true;
    }

    /**
     * Posts comment for pull request.
     *
     * @param path path to file in local repository.
     * @param pullRequestName name of pull requrest.
     * @param head branch that we are currently working in.
     * @param base branch that we want to merge into.
     * @return true if succeeded, false otherwise.
     */
    public static boolean generatePullComment(Pulls pulls, RepoCommit newCommit, String head, String base, String path, String pullRequestName) {
        try {
            Pull pullRequest = pulls.create(pullRequestName, head, base);
            PullComments comments = pullRequest.comments();

            comments.post(generatePullComment("test", "change"), newCommit.sha(), path, 1);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
