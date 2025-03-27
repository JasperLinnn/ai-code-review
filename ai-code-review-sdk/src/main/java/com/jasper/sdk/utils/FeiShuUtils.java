package com.jasper.sdk.utils;


import com.alibaba.fastjson2.JSON;
import com.jasper.sdk.domain.model.FeiShuMessageDTO;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 飞书工具类
 *
 * @author: zihong@micous.com
 * @date: 2025/3/26 16:25
 **/
public class FeiShuUtils {

    private static final String FEISHU_ROBOT_WEBHOOK;

    /**
     * 发送飞书机器人文本消息
     *
     * @param message 消息内容
     */
    public static void sendRobotTextMessage(String message) throws Exception {
        URL url = new URL(FEISHU_ROBOT_WEBHOOK);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        FeiShuMessageDTO feiShuMessageDTO = FeiShuMessageDTO.builder()
                .msgType("text")
                .content(FeiShuMessageDTO.Content.builder().text(message).build())
                .build();
        String jsonInputString = JSON.toJSONString(feiShuMessageDTO);
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }
        int responseCode = conn.getResponseCode();
        System.out.println("robot Response Code : " + responseCode);

    }

    /**
     * 发送飞书机器人卡片消息
     *
     * @param title  卡片标题
     * @param params 卡片内容
     */
    public static void sendRobotCardMessage(String title, Map<String, String> params) throws Exception {
        URL url = new URL(FEISHU_ROBOT_WEBHOOK);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        List<FeiShuMessageDTO.CardElement> elements = new ArrayList<>(params.size());
        params.forEach((k, v) -> {
            elements.add(FeiShuMessageDTO.CardElement.builder()
                    .tag("div")
                    .text(FeiShuMessageDTO.CardElementText.builder()
                            .content(k + ": " + v)
                            .tag("lark_md")
                            .build())
                    .build());
        });
        FeiShuMessageDTO feiShuMessageDTO = FeiShuMessageDTO.builder()
                .msgType("interactive")
                .card(FeiShuMessageDTO.Card.builder()
                        .header(FeiShuMessageDTO.CardHeader.builder()
                                .title(FeiShuMessageDTO.CardHeaderTitle.builder()
                                        .content(title)
                                        .tag("plain_text")
                                        .build())
                                .template("green")
                                .build())
                        .elements(elements)
                        .build())
                .build();
        String jsonInputString = JSON.toJSONString(feiShuMessageDTO);
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }
        int responseCode = conn.getResponseCode();
        System.out.println("robot Response Code : " + responseCode);

    }

    /**
     * 类初始化时从环境配置中获取 webhook
     */
    static {
        FEISHU_ROBOT_WEBHOOK = System.getenv("FEISHU_ROBOT_WEBHOOK");
    }

}
