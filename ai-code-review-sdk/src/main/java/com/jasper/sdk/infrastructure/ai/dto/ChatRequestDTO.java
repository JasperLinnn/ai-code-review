package com.jasper.sdk.infrastructure.ai.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
public class ChatRequestDTO {
    @JSONField(name = "model")
    private String model;
    
    @JSONField(name = "messages")
    private List<Message> messages;

    @Data
    @Builder
    public static class Message {
        @JSONField(name = "role")
        private String role;
        
        @JSONField(name = "content")
        private String content;
    }
}