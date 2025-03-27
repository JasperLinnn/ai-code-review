package com.jasper.sdk.infrastructure.ai.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class MessageDTO {
    @JSONField(name = "role")
    private String role;
    
    @JSONField(name = "content")
    private String content;
}