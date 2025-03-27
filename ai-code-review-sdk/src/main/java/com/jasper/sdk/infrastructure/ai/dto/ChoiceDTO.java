package com.jasper.sdk.infrastructure.ai.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class ChoiceDTO {
    @JSONField(name = "message")
    private MessageDTO message;
    
    @JSONField(name = "finish_reason")
    private String finishReason;
    
    @JSONField(name = "index")
    private Integer index;
    
    @JSONField(name = "logprobs")
    private Object logprobs; // 根据实际数据结构调整类型
}