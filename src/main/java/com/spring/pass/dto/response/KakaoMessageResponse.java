package com.spring.pass.dto.response;

import lombok.ToString;

import java.util.List;

public record KakaoMessageResponse(
        List<String> successfulReceiverUuids
) {
}
