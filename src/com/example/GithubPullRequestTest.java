package com.example;

import com.jcabi.github.*;
import org.apache.commons.codec.binary.Base64;

import javax.json.Json;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GithubPullRequestTest {

    public static void main(String[] args) {

        // get oath
        try {
            String oath = getOathKey();
            System.out.println(oath);
            createPullRequest("Testing automated pull requests", "testGithubPR", "master", "jmoh3/adventure", oath);
//            System.out.println(oath);
            createPullRequest("Testing automated pull requests", "testGithubPR", "master", "jmoh3/adventure", "f92f1d647adeb12b52a07e15d970691d3c504742");
        } catch (Exception e) {
            System.out.println("oops");
        }
    }

    public static void createPullRequest(String pullRequestName, String head, String base, String repoCoordinates, String oath) {
        Github github = new RtGithub(oath);
        Repo repo = github.repos().get(
                new Coordinates.Simple(repoCoordinates)
        );

        final String sha;
        Contents contents = repo.contents();

        final String path = "/src/com/example/GithubPullRequestTest.java";

        boolean exists = false;

        try {
            exists = contents.exists(path, head);
        } catch (Exception e) {
            System.out.println("not found");
        }

        if (exists) {
            try {
                Content pathContent = repo.contents().get(path, head);
                final Content.Smart content = new Content.Smart(pathContent);
                sha = content.sha();

                File file = new File("/src/com/example/GithubPullRequestTest.java");
                byte[] fileContent = Files.readAllBytes(file.toPath());

//                System.out.println(content.decoded());

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
                try {
                    Pull pullRequest = pulls.create(pullRequestName, head, base);
                    PullComments comments = pullRequest.comments();

                    comments.post(generatePullComment("test", "change"), newCommit.sha(), path, 1);

                } catch (Exception e) {
                    System.out.println(e.toString());
                }

            } catch (Exception e) {
                System.out.println("Couldn't get contents");
            }
        } else {
            // create
            System.out.println("Creating new file");

            File file = new File("/Users/jackieoh/IdeaProjects/adventure/src/com/example/GithubPullRequestTest.java");

            try {
                System.out.println("here");
                byte[] fileContent = convertToByteArray(file);
                System.out.println(fileContent.toString());

                final String enc = Base64.encodeBase64String(fileContent);
                System.out.println("here2");
                Content newContent = repo.contents().create(
                        Json.createObjectBuilder()
                                .add("message", "hello!")
                                .add("content", enc)
                                .add("branch", head)
                                .add("path", path)
<<<<<<< HEAD
=======
                                .add(
                                        "committer",
                                        Json.createObjectBuilder()
                                                .add("name", "Jackie Oh")
                                                .add("email", "jmoh3@illinois.edu")
                                )
>>>>>>> d5717f78659e5dc55254265123d119272d204d7c
                                .build()
                );

                Content.Smart smartContent = new Content.Smart(newContent);
                System.out.println("here3");
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
                try {
                    Pull pullRequest = pulls.create(pullRequestName, head, base);
                    PullComments comments = pullRequest.comments();

                    comments.post(generatePullComment("test", "change"), newCommit.sha(), path, 1);

                } catch (Exception e) {
                    System.out.println(e.toString());
                }


            } catch (Exception e) {
                System.out.println(e.toString());
                System.out.println("An error has occurred");
            }
        }
    }

    public static String generatePullComment(String testName, String changelog) {
        String title = "## What is the purpose for this change?";

        String purpose = "\n Fixing flaky test " +  testName;

        String changeLog = "\n ## Brief changelog \n " + changelog;

        System.out.println(title + purpose + changeLog);
        return title + purpose + changeLog;
    }

    public static String readFileAsString(String fileName) throws Exception
    {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }

    public static String getOathKey() throws Exception {
        String oath = readFileAsString("/Users/jackieoh/IdeaProjects/adventure/APIKey.txt");

        return oath;
    }

    public static byte[] convertToByteArray(File file)
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

}
