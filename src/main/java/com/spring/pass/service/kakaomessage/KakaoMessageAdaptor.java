package com.spring.pass.service.kakaomessage;

import com.spring.pass.dto.properties.KakaoProperties;
import com.spring.pass.dto.request.KakaoMessageRequest;
import com.spring.pass.dto.response.KakaoMessageResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KakaoMessageAdaptor {
    private final WebClient webClient;

    public KakaoMessageAdaptor(KakaoProperties config) {
        webClient = WebClient.builder()
                .baseUrl(config.host())
                .defaultHeaders(h -> {
                    h.setBearerAuth(config.token());
                    h.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                })
                .build();
    }

    public boolean sendKakaoMessage(String uuid, String text) {
        KakaoMessageResponse response = webClient.post()
                .uri("/v1/api/talk/friends/message/default/send")
                .body(BodyInserters.fromValue(KakaoMessageRequest.of(uuid, text)))
                .retrieve()
                .bodyToMono(KakaoMessageResponse.class)
                .block();

        if (response == null || response.successfulReceiverUuids() == null) {
            return false;
        }
        return response.successfulReceiverUuids().size() > 0;
    }
}
