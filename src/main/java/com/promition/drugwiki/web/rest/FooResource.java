package com.promition.drugwiki.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * FooResource controller
 */
@RestController
@RequestMapping("/api/foo")
public class FooResource {

    private final Logger log = LoggerFactory.getLogger(FooResource.class);

    /**
     * GET foos
     */
    @GetMapping("/foos")
    public Mono<String> foos() {
        return Mono.just("foos");
    }

    /**
     * GET boos
     */
    @GetMapping("/boos")
    public Mono<String> boos() {
        return Mono.just("boos");
    }
}
