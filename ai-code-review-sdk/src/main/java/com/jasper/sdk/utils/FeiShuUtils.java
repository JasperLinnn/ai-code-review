package com.jasper.sdk.utils;


import com.alibaba.fastjson2.JSON;
import com.jasper.sdk.domain.model.FeiShuMessage;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * 飞书工具类
 *
 * @author: zihong@micous.com
 * @date: 2025/3/26 16:25
 **/
public class FeiShuUtils {

    private final static String FEISHU_ROBOT_WEBHOOK = "https://open.FeiShu.cn/open-apis/bot/v2/hook/92d540fb-ff9f-4aea-a440-b1139d2de77a";

    /**
     * 发送飞书机器人文本消息
     * @param message 消息内容
     */
    public static void sendRobotTextMessage(String message) throws Exception {
        URL url = new URL(FEISHU_ROBOT_WEBHOOK);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        FeiShuMessage feiShuMessage = FeiShuMessage.builder()
                .msgType("text")
                .content(FeiShuMessage.Content.builder().text(message).build())
                .build();
        String jsonInputString = JSON.toJSONString(feiShuMessage);
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }
        int responseCode = conn.getResponseCode();
        System.out.println("robot Response Code : " + responseCode);

    }

    /**
     * 发送飞书机器人卡片消息
     * @param title 卡片标题
     * @param content 卡片内容
     */
    public static void sendRobotCardMessage(String title, String content) throws Exception {
        URL url = new URL(FEISHU_ROBOT_WEBHOOK);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        FeiShuMessage feiShuMessage = FeiShuMessage.builder()
                .msgType("interactive")
                .card(FeiShuMessage.Card.builder()
                        .header(FeiShuMessage.CardHeader.builder()
                                .title(FeiShuMessage.CardHeaderTitle.builder()
                                        .content(title)
                                        .tag("plain_text")
                                        .build())
                                .template("green")
                                .build())
                        .elements(Collections.singletonList(FeiShuMessage.CardElement.builder()
                                .tag("div")
                                .text(FeiShuMessage.CardElementText.builder()
                                        .content(content)
                                        .tag("lark_md")
                                        .build())
                                .build()))
                        .build())
                .build();
        String jsonInputString = JSON.toJSONString(feiShuMessage);
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }
        int responseCode = conn.getResponseCode();
        System.out.println("robot Response Code : " + responseCode);

    }

}
