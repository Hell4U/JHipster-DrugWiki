package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Brand;
import com.mycompany.myapp.repository.BrandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * SearchResource controller
 */
@RestController
@RequestMapping("/api/search")
public class SearchResource {

    private final Logger log = LoggerFactory.getLogger(SearchResource.class);

    private final BrandRepository brandRepository;

    public SearchResource(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }


    /**
     * GET search
     */
    @GetMapping("/brand/")
    public ResponseEntity<List<Brand>> search(String name) {
        return ResponseEntity.created(URI.create("/api/search/")).body(brandRepository.findAllByBnameContaining(name));
    }
}
