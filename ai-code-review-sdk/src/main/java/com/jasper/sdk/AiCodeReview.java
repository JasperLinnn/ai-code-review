package com.jasper.sdk;

import com.alibaba.fastjson.JSON;
import com.jasper.sdk.domain.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author: zihong@micous.com
 * @date: 2025/3/24 16:10
 **/
@Slf4j
public class AiCodeReview {
    public static void main(String[] args) {
        ProcessBuilder pb = new ProcessBuilder("git", "diff", "HEAD~1", "HEAD");
        pb.directory(new File("."));
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
        } catch (Exception e) {
            log.error("git diff error", e);
        }
    }

    private static String codeReview(String code) throws Exception {
        URL url = new URL("https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer sk-b2ade0b7b59b49b6bf4d1bc1d8ac0425");
        conn.setDoOutput(true);

        String jsonInputString = "{\n" +
                "    \"model\": \"qwen-plus\",\n" +
                "    \"messages\": [\n" +
                "        {\n" +
                "            \"role\": \"system\",\n" +
                "            \"content\": \"You are a helpful assistant.\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"role\": \"user\",\n" +
                "            \"content\": \"请对以下的代码进行评审。代码为:" + code + "\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

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
}
