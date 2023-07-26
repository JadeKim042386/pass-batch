package com.spring.pass.dto.request;

import java.util.Collections;
import java.util.List;

public record KakaoMessageRequest(
        TemplateObject templateObject,
        List<String> receiverUuids
) {
    public record TemplateObject(
            String objectType,
            String text,
            Link link
    ) {
        public record Link(
                String webUrl
        ) {
        }
    }

    public static KakaoMessageRequest of(String uuid, String text) {
        List<String> receiverUuids = Collections.singletonList(uuid);
        TemplateObject templateObject = new TemplateObject(
                "text",
                text,
                new TemplateObject.Link(null)
        );

        return new KakaoMessageRequest(templateObject, receiverUuids);
    }
}
