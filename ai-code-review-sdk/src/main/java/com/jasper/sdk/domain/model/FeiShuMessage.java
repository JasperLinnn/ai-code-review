package com.jasper.sdk.domain.model;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FeiShuMessage {
    @JSONField(name = "msg_type")
    private String msgType;

    @JSONField(name = "content")
    private Content content;

    @JSONField(name = "card")
    private Card card;

    @Data
    @Builder
    public static class Content {
        @JSONField(name = "text")
        private String text;
    }

    @Data
    @Builder
    public static class Card {
        @JSONField(name = "header")
        private CardHeader header;

        @JSONField(name = "elements")
        private List<CardElement> elements;
    }

    @Data
    @Builder
    public static class CardHeader {
        @JSONField(name = "title")
        private CardHeaderTitle title;
        @JSONField(name = "template")
        private String template;
    }

    @Data
    @Builder
    public static class CardHeaderTitle {
        @JSONField(name = "content")
        private String content;

        @JSONField(name = "tag")
        private String tag;
    }

    @Data
    @Builder
    public static class CardElement {
        @JSONField(name = "tag")
        private String tag;

        @JSONField(name = "text")
        private CardElementText text;
    }

    @Data
    @Builder
    public static class CardElementText {
        @JSONField(name = "content")
        private String content;

        @JSONField(name = "tag")
        private String tag;
    }
}