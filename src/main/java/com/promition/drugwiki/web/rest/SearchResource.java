package com.promition.drugwiki.web.rest;

import com.promition.drugwiki.domain.Brand;
import com.promition.drugwiki.domain.Company;
import com.promition.drugwiki.domain.Generics;
import com.promition.drugwiki.domain.Ingredients;
import com.promition.drugwiki.service.SearchService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SearchResource controller
 */
@RestController
@RequestMapping("/api/search")
public class SearchResource {

    @Autowired
    private SearchService searchService;

    private final Logger log = LoggerFactory.getLogger(SearchResource.class);

    /**
     * GET searchBrand
     */
    @GetMapping("/search-Brand")
    public List<Brand> searchBrand(String name) {
        return searchService.searchByBrandName(name);
    }

    /**
     * GET searchCompany
     */
    @GetMapping("/search-Company")
    public List<Company> searchCompany(String name) {
        return searchService.searchByCompanyName(name);
    }

    /**
     * GET searchIngredient
     */
    @GetMapping("/search-Ingredient")
    public List<Ingredients> searchIngredient(String name) {
        return searchService.searchByIngredientName(name);
    }

    @GetMapping("/search-Generic")
    public List<Generics> searchGeneric(String name) {
        return searchService.searchByGenericName(name);
    }
}
