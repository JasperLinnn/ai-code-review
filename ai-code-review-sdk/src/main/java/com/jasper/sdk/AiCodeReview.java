package com.jasper.sdk;

import com.alibaba.fastjson2.JSON;
import com.jasper.sdk.domain.model.ApiResponse;
import com.jasper.sdk.domain.model.ChatRequest;
import com.jasper.sdk.utils.FeiShuUtils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * @author: zihong@micous.com
 * @date: 2025/3/24 16:10
 **/
@Slf4j
public class AiCodeReview {
    public static void main(String[] args) {
        ProcessBuilder pb = new ProcessBuilder("git", "diff", "HEAD~1", "HEAD");
        pb.directory(new File("."));
        // 获取 actions token
        String token = System.getenv("GITHUB_TOKEN");
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("GITHUB_TOKEN is empty");
        }
        try {
            Process p = pb.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            StringBuilder diffCode = new StringBuilder();
            while ((line = br.readLine()) != null) {
                diffCode.append(line).append("\n");
            }
            int exitCode = p.waitFor();
            System.out.println("Exited with code:" + exitCode);
            System.out.println("评审代码:" + diffCode.toString());
            // 大模型代码评审
            String log = codeReview(diffCode.toString());
            System.out.println("code review:" + log);
            // 写入日志仓库
            String logUrl = writeLog(log, token);
            // 发送飞书消息
            FeiShuUtils.sendRobotCardMessage("代码评审","仓库地址: " + logUrl);
        } catch (Exception e) {
            log.error("git diff error", e);
        }
    }

    private static String codeReview(String code) throws Exception {
        URL url = new URL("https://api.deepseek.com/chat/completions");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer sk-1019a9c026e245298170c04be82f029b");
        conn.setDoOutput(true);

        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel("deepseek-chat");
        ChatRequest.Message system = ChatRequest.Message.builder().role("system").content("You are a helpful assistant.").build();
        ChatRequest.Message user = ChatRequest.Message.builder().role("user").content("请对以下的代码进行评审。代码为:" + code).build();
        chatRequest.setMessages(Arrays.asList(system, user));
        String jsonInputString = JSON.toJSONString(chatRequest);
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }
        int responseCode = conn.getResponseCode();
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();
        System.out.println(content);
        ApiResponse apiResponse = JSON.parseObject(content.toString(), ApiResponse.class);
        return apiResponse.getChoices().get(0).getMessage().getContent();
    }

    /**
     * 写入日志仓库
     * @param log 日志内容
     * @param token token
     */
    public static String writeLog(String log, String token) throws Exception {
        // 定义GitHub上的日志仓库URL
        String gitUrl = "https://github.com/JasperLinnn/ai-code-review-log.git";

        // 克隆日志仓库到本地，使用提供的token进行身份验证
        Git git = Git.cloneRepository()
                .setURI(gitUrl)
                .setDirectory(new File("repo"))
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, ""))
                .call();

        // 创建根据日期命名的文件夹，如果不存在则创建
        String dateFolderName = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File dateFolder = new File("repo/" + dateFolderName);
        if (!dateFolder.exists()) {
            dateFolder.mkdirs();
        }

        // 生成唯一的日志文件名，并创建文件
        String logFileName = UUID.randomUUID() + ".md";
        File logFile = new File(dateFolder, logFileName);

        // 将日志内容写入文件
        try (FileWriter fileWriter = new FileWriter(logFile)){
            fileWriter.write(log);
        }

        // 将日志文件添加到Git仓库并提交
        git.add().addFilepattern(dateFolderName+ "/" + logFileName).call();
        git.commit().setMessage("add log").call();

        // 将更改推送到远程仓库
        git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, "")).call();
        git.close();

        // 返回日志文件在GitHub上的URL
        return "https://github.com/JasperLinnn/ai-code-review-log/blob/main/" + dateFolderName + "/" + logFileName;
    }

}
