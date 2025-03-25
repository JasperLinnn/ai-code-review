package com.jasper.sdk.domain.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class Message {
    @JSONField(name = "role")
    private String role;
    
    @JSONField(name = "content")
    private String content;
}