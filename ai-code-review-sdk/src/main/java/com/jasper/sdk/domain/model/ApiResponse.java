package com.jasper.sdk.domain.model;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class ApiResponse {
    @JSONField(name = "choices")
    private List<Choice> choices;
    
    @JSONField(name = "object")
    private String object;
    
    @JSONField(name = "usage")
    private Usage usage;
    
    @JSONField(name = "created")
    private Long created;
    
    @JSONField(name = "system_fingerprint")
    private String systemFingerprint;
    
    @JSONField(name = "model")
    private String model;
    
    @JSONField(name = "id")
    private String id;
}