package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Brand;
import com.mycompany.myapp.repository.BrandRepository;
import com.mycompany.myapp.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * SearchResource controller
 */
@RestController
@RequestMapping("/api/search")
public class SearchResource {

    private final Logger log = LoggerFactory.getLogger(SearchResource.class);

    @Autowired
    private SearchService searchService;

    /**
     * GET search
     */
    @GetMapping("/brand")
    public Flux<Brand> search(String name) {
        return searchService.searchBrand(name);
    }
}
