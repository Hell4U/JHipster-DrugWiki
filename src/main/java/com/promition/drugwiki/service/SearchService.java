package com.promition.drugwiki.service;

import com.promition.drugwiki.domain.Brand;
import com.promition.drugwiki.domain.Company;
import com.promition.drugwiki.domain.Generics;
import com.promition.drugwiki.domain.Ingredients;
import com.promition.drugwiki.repository.BrandRepository;
import com.promition.drugwiki.repository.CompanyRepository;
import com.promition.drugwiki.repository.GenericsRepository;
import com.promition.drugwiki.repository.IngredientsRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SearchService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private GenericsRepository genericsRepository;

    @Autowired
    private IngredientsRepository ingredientsRepository;

    private final Logger log = LoggerFactory.getLogger(SearchService.class);

    public List<Brand> searchByBrandName(String name) {
        return brandRepository.findAllByBnameContains(name);
    }

    public List<Company> searchByCompanyName(String name) {
        return companyRepository.findAllByCnameContains(name);
    }

    public List<Generics> searchByGenericName(String name) {
        return genericsRepository.findAllByGnameContains(name);
    }

    public List<Ingredients> searchByIngredientName(String name) {
        return ingredientsRepository.findAllByInameContains(name);
    }
}
