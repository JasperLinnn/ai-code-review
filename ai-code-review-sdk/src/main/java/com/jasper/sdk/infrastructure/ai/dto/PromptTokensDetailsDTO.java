package com.jasper.sdk.infrastructure.ai.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class PromptTokensDetailsDTO {
    @JSONField(name = "cached_tokens")
    private Integer cachedTokens;
}