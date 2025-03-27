package com.jasper.sdk.infrastructure.ai.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class UsageDTO {
    @JSONField(name = "prompt_tokens")
    private Integer promptTokens;

    @JSONField(name = "completion_tokens")
    private Integer completionTokens;

    @JSONField(name = "total_tokens")
    private Integer totalTokens;

    @JSONField(name = "prompt_tokens_details")
    private PromptTokensDetailsDTO promptTokensDetails;
}