package com.jasper.sdk.domain.model;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class PromptTokensDetails {
    @JSONField(name = "cached_tokens")
    private Integer cachedTokens;
}