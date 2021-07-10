package com.promition.drugwiki.web.rest;

import com.promition.drugwiki.domain.Brand;
import com.promition.drugwiki.domain.Company;
import com.promition.drugwiki.domain.Generics;
import com.promition.drugwiki.domain.Ingredients;
import com.promition.drugwiki.service.SearchService;
import com.promition.drugwiki.service.criteria.BrandCriteria;
import com.promition.drugwiki.service.dto.BrandDTO;
import com.promition.drugwiki.service.criteria.CompanyCriteria;
import com.promition.drugwiki.service.dto.CompanyDTO;
import com.promition.drugwiki.service.criteria.GenericsCriteria;
import com.promition.drugwiki.service.dto.GenericsDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

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

    @GetMapping("/Brand")
    public ResponseEntity<List<Brand>> pagableBrandSearch(String name, BrandCriteria criteria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Brand> pages = searchService.pageableBrandSearch(name, criteria, pageable);
        pages.getSort();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pages);
        return ResponseEntity.ok().headers(headers).body(pages.getContent());
    }
    @GetMapping("/Company")
    public ResponseEntity<List<Company>> pagableCompanySearch(String name, CompanyCriteria criteria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Company> pages = searchService.pageableCompanySearch(name, criteria, pageable);
        pages.getSort();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pages);
        return ResponseEntity.ok().headers(headers).body(pages.getContent());
    }
    @GetMapping("/Generics")
    public ResponseEntity<List<Generics>> pagableGenericsSearch(String name, GenericsCriteria criteria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Generics> pages = searchService.pageableGenericsSearch(name, criteria, pageable);
        pages.getSort();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pages);
        return ResponseEntity.ok().headers(headers).body(pages.getContent());
    }
}
