package com.jasper.sdk.utils;


import com.alibaba.fastjson2.JSON;
import com.jasper.sdk.domain.model.LarkMessage;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 飞书工具类
 *
 * @author: zihong@micous.com
 * @date: 2025/3/26 16:25
 **/
public class FeiShuUtils {

    private final static String FEISHU_ROBOT_WEBHOOK = "https://open.feishu.cn/open-apis/bot/v2/hook/92d540fb-ff9f-4aea-a440-b1139d2de77a";

    /**
     * 发送飞书机器人消息
     * @param message 消息内容
     */
    public static void sendRobotTextMessage(String message) throws Exception {
        URL url = new URL(FEISHU_ROBOT_WEBHOOK);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        LarkMessage larkMessage = LarkMessage.builder()
                .msgType("text")
                .content(LarkMessage.Content.builder().text(message).build())
                .build();
        String jsonInputString = JSON.toJSONString(larkMessage);
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }
        int responseCode = conn.getResponseCode();
        System.out.println("robot Response Code : " + responseCode);

    }
}
