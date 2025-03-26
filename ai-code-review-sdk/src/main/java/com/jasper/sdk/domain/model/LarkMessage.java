package com.jasper.sdk.domain.model;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LarkMessage {
    @JSONField(name = "msg_type")
    private String msgType;
    
    @JSONField(name = "content")
    private Content content;

    @Data
    @Builder
    public static class Content {
        @JSONField(name = "text")
        private String text;
    }
}