package com.jasper.sdk.infrastructure.ai.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class ChatResponseDTO {
    @JSONField(name = "choices")
    private List<ChoiceDTO> choices;
    
    @JSONField(name = "object")
    private String object;
    
    @JSONField(name = "usage")
    private UsageDTO usage;
    
    @JSONField(name = "created")
    private Long created;
    
    @JSONField(name = "system_fingerprint")
    private String systemFingerprint;
    
    @JSONField(name = "model")
    private String model;
    
    @JSONField(name = "id")
    private String id;
}