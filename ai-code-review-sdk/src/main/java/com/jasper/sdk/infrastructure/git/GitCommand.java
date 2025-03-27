package com.jasper.sdk.infrastructure.git;

import com.jasper.sdk.utils.RandomStringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Git 操作
 *
 * @author: zihong@micous.com
 * @date: 2025/3/27 11:28
 **/
@Slf4j
@AllArgsConstructor
@Data
public class GitCommand {
    // 日志仓库地址
    private final String logUri;
    // token
    private final String token;
    // 项目名称
    private final String project;
    // 分支
    private final String branch;
    // 作者
    private final String author;
    // git commit message
    private final String message;


    /**
     * 获取最近一次提交与当前工作目录之间的差异
     *
     * @return 字符串形式的差异信息，包含最近一次提交的所有更改
     */
    public String diff() throws IOException, InterruptedException {
        // 创建ProcessBuilder以获取最近一次提交的哈希值
        ProcessBuilder processBuilder = new ProcessBuilder("git", "log", "-1", "--pretty=format:%H");
        processBuilder.directory(new File("."));
        Process process = processBuilder.start();

        // 读取并保存最近一次提交的哈希值
        BufferedReader logReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String latestCommitHash = logReader.readLine();
        logReader.close();
        process.waitFor();

        // 创建ProcessBuilder以获取最近一次提交与它的父提交之间的差异
        ProcessBuilder diffProcessBuilder = new ProcessBuilder("git", "diff", latestCommitHash + "^", latestCommitHash);
        diffProcessBuilder.directory(new File("."));
        Process diffProcess = diffProcessBuilder.start();
        BufferedReader diffReader = new BufferedReader(new InputStreamReader(diffProcess.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder();
        // 读取差异信息并构建字符串
        while ((line = diffReader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        diffReader.close();

        // 检查差异获取是否成功，如果失败，抛出异常
        int exitCode = diffProcess.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("git diff failed");
        }
        // 返回差异信息字符串
        return sb.toString();
    }

    /**
     * 将评审内容提交到Git仓库并推送
     *
     * @return 返回新文件在远程仓库的URL路径
     */
    public String commitAndPush(String review) throws Exception {
        // 克隆远程Git仓库
        Git git = Git.cloneRepository()
                .setURI(logUri + ".git")
                .setDirectory(new File("repo"))
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, ""))
                .call();
        // 创建基于当前日期的文件夹
        String dateFolderName = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File dateFolder = new File("repo/" + dateFolderName);
        if (!dateFolder.exists()) {
            dateFolder.mkdirs();
        }
        // 生成文件名并创建文件
        String fileName = project + "_" + branch + "_" + RandomStringUtils.generateRandomString(4) + ".md";
        File file = new File(dateFolder, fileName);
        // 将推荐内容写入文件
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(review);
        }
        // 将文件添加到Git仓库并提交
        git.add().addFilepattern(dateFolderName + "/" + fileName).call();
        git.commit().setMessage("add code review new file" + fileName).call();
        // 推送更改到远程仓库
        git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, "")).call();
        // 日志记录提交和推送完成的信息
        log.info("code-review git commit and push done {}", fileName);
        // 返回新文件的远程URL路径
        return logUri + "/blob/main/" + dateFolderName + "/" + fileName;
    }
}
