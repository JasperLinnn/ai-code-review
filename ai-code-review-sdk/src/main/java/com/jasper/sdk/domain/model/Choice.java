package com.jasper.sdk.domain.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class Choice {
    @JSONField(name = "message")
    private Message message;
    
    @JSONField(name = "finish_reason")
    private String finishReason;
    
    @JSONField(name = "index")
    private Integer index;
    
    @JSONField(name = "logprobs")
    private Object logprobs; // 根据实际数据结构调整类型
}