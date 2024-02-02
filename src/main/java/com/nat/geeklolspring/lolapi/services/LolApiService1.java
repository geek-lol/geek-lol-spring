package com.nat.geeklolspring.lolapi.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class LolApiService1 {

    Mono<String> mono = WebClient.create()
            .get()
            .uri("")
            .retrieve()
            .bodyToMono(String.class);


}
