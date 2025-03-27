package com.jasper.sdk.infrastructure.ai.impl;

import com.alibaba.fastjson2.JSON;
import com.jasper.sdk.infrastructure.ai.IAI;
import com.jasper.sdk.infrastructure.ai.dto.ChatRequestDTO;
import com.jasper.sdk.infrastructure.ai.dto.ChatResponseDTO;
import lombok.Data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


/**
 * @author: zihong@micous.com
 * @date: 2025/3/27 11:55
 **/
@Data
public class DeepSeek implements IAI {

    private final String apiHost;

    private final String apiKeySecret;

    /**
     * 获取模型回复
     */
    @Override
    public ChatResponseDTO completions(ChatRequestDTO chatRequest) throws Exception {
        // URL 构建
        URL url = new URL(apiHost);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + apiKeySecret);
        conn.setDoOutput(true);

        // 将聊天请求转换为JSON字符串
        String jsonInputString = JSON.toJSONString(chatRequest);
        // 获取连接的输出流以发送请求体
        try (OutputStream os = conn.getOutputStream()) {
            // 将JSON字符串转换为字节数组并写入输出流
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }
        // 获取HTTP响应码
        int responseCode = conn.getResponseCode();
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        // 读取响应内容并存储到StringBuilder中
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        // 关闭读取器和连接
        in.close();
        conn.disconnect();
        // 将响应内容解析为ChatResponseDTO对象并返回
        return JSON.parseObject(content.toString(), ChatResponseDTO.class);
    }
}
