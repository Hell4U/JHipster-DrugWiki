package com.promition.drugwiki.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.promition.drugwiki.IntegrationTest;
import com.promition.drugwiki.domain.Brand;
import com.promition.drugwiki.domain.Company;
import com.promition.drugwiki.domain.Generics;
import com.promition.drugwiki.domain.enumeration.BrandType;
import com.promition.drugwiki.domain.enumeration.TypeUnit;
import com.promition.drugwiki.repository.BrandRepository;
import com.promition.drugwiki.repository.search.BrandSearchRepository;
import com.promition.drugwiki.service.BrandService;
import com.promition.drugwiki.service.criteria.BrandCriteria;
import com.promition.drugwiki.service.dto.BrandDTO;
import com.promition.drugwiki.service.mapper.BrandMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BrandResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BrandResourceIT {

    private static final String DEFAULT_BNAME = "AAAAAAAAAA";
    private static final String UPDATED_BNAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;
    private static final Double SMALLER_PRICE = 1D - 1D;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_PACKAGEUNIT = 1D;
    private static final Double UPDATED_PACKAGEUNIT = 2D;
    private static final Double SMALLER_PACKAGEUNIT = 1D - 1D;

    private static final BrandType DEFAULT_TYPE = BrandType.Tablet;
    private static final BrandType UPDATED_TYPE = BrandType.Injection;

    private static final TypeUnit DEFAULT_TYPEUNIT = TypeUnit.PCS;
    private static final TypeUnit UPDATED_TYPEUNIT = TypeUnit.Miligram;

    private static final String ENTITY_API_URL = "/api/brands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/brands";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BrandRepository brandRepository;

    @Mock
    private BrandRepository brandRepositoryMock;

    @Autowired
    private BrandMapper brandMapper;

    @Mock
    private BrandService brandServiceMock;

    /**
     * This repository is mocked in the com.promition.drugwiki.repository.search test package.
     *
     * @see com.promition.drugwiki.repository.search.BrandSearchRepositoryMockConfiguration
     */
    @Autowired
    private BrandSearchRepository mockBrandSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBrandMockMvc;

    private Brand brand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Brand createEntity(EntityManager em) {
        Brand brand = new Brand()
            .bname(DEFAULT_BNAME)
            .price(DEFAULT_PRICE)
            .date(DEFAULT_DATE)
            .packageunit(DEFAULT_PACKAGEUNIT)
            .type(DEFAULT_TYPE)
            .typeunit(DEFAULT_TYPEUNIT);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        brand.setCompanyofMedicine(company);
        // Add required entity
        Generics generics;
        if (TestUtil.findAll(em, Generics.class).isEmpty()) {
            generics = GenericsResourceIT.createEntity(em);
            em.persist(generics);
            em.flush();
        } else {
            generics = TestUtil.findAll(em, Generics.class).get(0);
        }
        brand.getGenericsuseds().add(generics);
        return brand;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Brand createUpdatedEntity(EntityManager em) {
        Brand brand = new Brand()
            .bname(UPDATED_BNAME)
            .price(UPDATED_PRICE)
            .date(UPDATED_DATE)
            .packageunit(UPDATED_PACKAGEUNIT)
            .type(UPDATED_TYPE)
            .typeunit(UPDATED_TYPEUNIT);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createUpdatedEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        brand.setCompanyofMedicine(company);
        // Add required entity
        Generics generics;
        if (TestUtil.findAll(em, Generics.class).isEmpty()) {
            generics = GenericsResourceIT.createUpdatedEntity(em);
            em.persist(generics);
            em.flush();
        } else {
            generics = TestUtil.findAll(em, Generics.class).get(0);
        }
        brand.getGenericsuseds().add(generics);
        return brand;
    }

    @BeforeEach
    public void initTest() {
        brand = createEntity(em);
    }

    @Test
    @Transactional
    void createBrand() throws Exception {
        int databaseSizeBeforeCreate = brandRepository.findAll().size();
        // Create the Brand
        BrandDTO brandDTO = brandMapper.toDto(brand);
        restBrandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(brandDTO)))
            .andExpect(status().isCreated());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeCreate + 1);
        Brand testBrand = brandList.get(brandList.size() - 1);
        assertThat(testBrand.getBname()).isEqualTo(DEFAULT_BNAME);
        assertThat(testBrand.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testBrand.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testBrand.getPackageunit()).isEqualTo(DEFAULT_PACKAGEUNIT);
        assertThat(testBrand.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testBrand.getTypeunit()).isEqualTo(DEFAULT_TYPEUNIT);

        // Validate the Brand in Elasticsearch
        verify(mockBrandSearchRepository, times(1)).save(testBrand);
    }

    @Test
    @Transactional
    void createBrandWithExistingId() throws Exception {
        // Create the Brand with an existing ID
        brand.setId(1L);
        BrandDTO brandDTO = brandMapper.toDto(brand);

        int databaseSizeBeforeCreate = brandRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBrandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(brandDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeCreate);

        // Validate the Brand in Elasticsearch
        verify(mockBrandSearchRepository, times(0)).save(brand);
    }

    @Test
    @Transactional
    void checkBnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = brandRepository.findAll().size();
        // set the field null
        brand.setBname(null);

        // Create the Brand, which fails.
        BrandDTO brandDTO = brandMapper.toDto(brand);

        restBrandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(brandDTO)))
            .andExpect(status().isBadRequest());

        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = brandRepository.findAll().size();
        // set the field null
        brand.setPrice(null);

        // Create the Brand, which fails.
        BrandDTO brandDTO = brandMapper.toDto(brand);

        restBrandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(brandDTO)))
            .andExpect(status().isBadRequest());

        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = brandRepository.findAll().size();
        // set the field null
        brand.setDate(null);

        // Create the Brand, which fails.
        BrandDTO brandDTO = brandMapper.toDto(brand);

        restBrandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(brandDTO)))
            .andExpect(status().isBadRequest());

        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPackageunitIsRequired() throws Exception {
        int databaseSizeBeforeTest = brandRepository.findAll().size();
        // set the field null
        brand.setPackageunit(null);

        // Create the Brand, which fails.
        BrandDTO brandDTO = brandMapper.toDto(brand);

        restBrandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(brandDTO)))
            .andExpect(status().isBadRequest());

        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = brandRepository.findAll().size();
        // set the field null
        brand.setType(null);

        // Create the Brand, which fails.
        BrandDTO brandDTO = brandMapper.toDto(brand);

        restBrandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(brandDTO)))
            .andExpect(status().isBadRequest());

        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeunitIsRequired() throws Exception {
        int databaseSizeBeforeTest = brandRepository.findAll().size();
        // set the field null
        brand.setTypeunit(null);

        // Create the Brand, which fails.
        BrandDTO brandDTO = brandMapper.toDto(brand);

        restBrandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(brandDTO)))
            .andExpect(status().isBadRequest());

        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBrands() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList
        restBrandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(brand.getId().intValue())))
            .andExpect(jsonPath("$.[*].bname").value(hasItem(DEFAULT_BNAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].packageunit").value(hasItem(DEFAULT_PACKAGEUNIT.doubleValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].typeunit").value(hasItem(DEFAULT_TYPEUNIT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBrandsWithEagerRelationshipsIsEnabled() throws Exception {
        when(brandServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBrandMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(brandServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBrandsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(brandServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBrandMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(brandServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getBrand() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get the brand
        restBrandMockMvc
            .perform(get(ENTITY_API_URL_ID, brand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(brand.getId().intValue()))
            .andExpect(jsonPath("$.bname").value(DEFAULT_BNAME))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.packageunit").value(DEFAULT_PACKAGEUNIT.doubleValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.typeunit").value(DEFAULT_TYPEUNIT.toString()));
    }

    @Test
    @Transactional
    void getBrandsByIdFiltering() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        Long id = brand.getId();

        defaultBrandShouldBeFound("id.equals=" + id);
        defaultBrandShouldNotBeFound("id.notEquals=" + id);

        defaultBrandShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBrandShouldNotBeFound("id.greaterThan=" + id);

        defaultBrandShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBrandShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBrandsByBnameIsEqualToSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where bname equals to DEFAULT_BNAME
        defaultBrandShouldBeFound("bname.equals=" + DEFAULT_BNAME);

        // Get all the brandList where bname equals to UPDATED_BNAME
        defaultBrandShouldNotBeFound("bname.equals=" + UPDATED_BNAME);
    }

    @Test
    @Transactional
    void getAllBrandsByBnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where bname not equals to DEFAULT_BNAME
        defaultBrandShouldNotBeFound("bname.notEquals=" + DEFAULT_BNAME);

        // Get all the brandList where bname not equals to UPDATED_BNAME
        defaultBrandShouldBeFound("bname.notEquals=" + UPDATED_BNAME);
    }

    @Test
    @Transactional
    void getAllBrandsByBnameIsInShouldWork() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where bname in DEFAULT_BNAME or UPDATED_BNAME
        defaultBrandShouldBeFound("bname.in=" + DEFAULT_BNAME + "," + UPDATED_BNAME);

        // Get all the brandList where bname equals to UPDATED_BNAME
        defaultBrandShouldNotBeFound("bname.in=" + UPDATED_BNAME);
    }

    @Test
    @Transactional
    void getAllBrandsByBnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where bname is not null
        defaultBrandShouldBeFound("bname.specified=true");

        // Get all the brandList where bname is null
        defaultBrandShouldNotBeFound("bname.specified=false");
    }

    @Test
    @Transactional
    void getAllBrandsByBnameContainsSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where bname contains DEFAULT_BNAME
        defaultBrandShouldBeFound("bname.contains=" + DEFAULT_BNAME);

        // Get all the brandList where bname contains UPDATED_BNAME
        defaultBrandShouldNotBeFound("bname.contains=" + UPDATED_BNAME);
    }

    @Test
    @Transactional
    void getAllBrandsByBnameNotContainsSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where bname does not contain DEFAULT_BNAME
        defaultBrandShouldNotBeFound("bname.doesNotContain=" + DEFAULT_BNAME);

        // Get all the brandList where bname does not contain UPDATED_BNAME
        defaultBrandShouldBeFound("bname.doesNotContain=" + UPDATED_BNAME);
    }

    @Test
    @Transactional
    void getAllBrandsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where price equals to DEFAULT_PRICE
        defaultBrandShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the brandList where price equals to UPDATED_PRICE
        defaultBrandShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBrandsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where price not equals to DEFAULT_PRICE
        defaultBrandShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the brandList where price not equals to UPDATED_PRICE
        defaultBrandShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBrandsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultBrandShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the brandList where price equals to UPDATED_PRICE
        defaultBrandShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBrandsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where price is not null
        defaultBrandShouldBeFound("price.specified=true");

        // Get all the brandList where price is null
        defaultBrandShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllBrandsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where price is greater than or equal to DEFAULT_PRICE
        defaultBrandShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the brandList where price is greater than or equal to UPDATED_PRICE
        defaultBrandShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBrandsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where price is less than or equal to DEFAULT_PRICE
        defaultBrandShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the brandList where price is less than or equal to SMALLER_PRICE
        defaultBrandShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllBrandsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where price is less than DEFAULT_PRICE
        defaultBrandShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the brandList where price is less than UPDATED_PRICE
        defaultBrandShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBrandsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where price is greater than DEFAULT_PRICE
        defaultBrandShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the brandList where price is greater than SMALLER_PRICE
        defaultBrandShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllBrandsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where date equals to DEFAULT_DATE
        defaultBrandShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the brandList where date equals to UPDATED_DATE
        defaultBrandShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBrandsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where date not equals to DEFAULT_DATE
        defaultBrandShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the brandList where date not equals to UPDATED_DATE
        defaultBrandShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBrandsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where date in DEFAULT_DATE or UPDATED_DATE
        defaultBrandShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the brandList where date equals to UPDATED_DATE
        defaultBrandShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBrandsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where date is not null
        defaultBrandShouldBeFound("date.specified=true");

        // Get all the brandList where date is null
        defaultBrandShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllBrandsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where date is greater than or equal to DEFAULT_DATE
        defaultBrandShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the brandList where date is greater than or equal to UPDATED_DATE
        defaultBrandShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBrandsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where date is less than or equal to DEFAULT_DATE
        defaultBrandShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the brandList where date is less than or equal to SMALLER_DATE
        defaultBrandShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllBrandsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where date is less than DEFAULT_DATE
        defaultBrandShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the brandList where date is less than UPDATED_DATE
        defaultBrandShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBrandsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where date is greater than DEFAULT_DATE
        defaultBrandShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the brandList where date is greater than SMALLER_DATE
        defaultBrandShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllBrandsByPackageunitIsEqualToSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where packageunit equals to DEFAULT_PACKAGEUNIT
        defaultBrandShouldBeFound("packageunit.equals=" + DEFAULT_PACKAGEUNIT);

        // Get all the brandList where packageunit equals to UPDATED_PACKAGEUNIT
        defaultBrandShouldNotBeFound("packageunit.equals=" + UPDATED_PACKAGEUNIT);
    }

    @Test
    @Transactional
    void getAllBrandsByPackageunitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where packageunit not equals to DEFAULT_PACKAGEUNIT
        defaultBrandShouldNotBeFound("packageunit.notEquals=" + DEFAULT_PACKAGEUNIT);

        // Get all the brandList where packageunit not equals to UPDATED_PACKAGEUNIT
        defaultBrandShouldBeFound("packageunit.notEquals=" + UPDATED_PACKAGEUNIT);
    }

    @Test
    @Transactional
    void getAllBrandsByPackageunitIsInShouldWork() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where packageunit in DEFAULT_PACKAGEUNIT or UPDATED_PACKAGEUNIT
        defaultBrandShouldBeFound("packageunit.in=" + DEFAULT_PACKAGEUNIT + "," + UPDATED_PACKAGEUNIT);

        // Get all the brandList where packageunit equals to UPDATED_PACKAGEUNIT
        defaultBrandShouldNotBeFound("packageunit.in=" + UPDATED_PACKAGEUNIT);
    }

    @Test
    @Transactional
    void getAllBrandsByPackageunitIsNullOrNotNull() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where packageunit is not null
        defaultBrandShouldBeFound("packageunit.specified=true");

        // Get all the brandList where packageunit is null
        defaultBrandShouldNotBeFound("packageunit.specified=false");
    }

    @Test
    @Transactional
    void getAllBrandsByPackageunitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where packageunit is greater than or equal to DEFAULT_PACKAGEUNIT
        defaultBrandShouldBeFound("packageunit.greaterThanOrEqual=" + DEFAULT_PACKAGEUNIT);

        // Get all the brandList where packageunit is greater than or equal to UPDATED_PACKAGEUNIT
        defaultBrandShouldNotBeFound("packageunit.greaterThanOrEqual=" + UPDATED_PACKAGEUNIT);
    }

    @Test
    @Transactional
    void getAllBrandsByPackageunitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where packageunit is less than or equal to DEFAULT_PACKAGEUNIT
        defaultBrandShouldBeFound("packageunit.lessThanOrEqual=" + DEFAULT_PACKAGEUNIT);

        // Get all the brandList where packageunit is less than or equal to SMALLER_PACKAGEUNIT
        defaultBrandShouldNotBeFound("packageunit.lessThanOrEqual=" + SMALLER_PACKAGEUNIT);
    }

    @Test
    @Transactional
    void getAllBrandsByPackageunitIsLessThanSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where packageunit is less than DEFAULT_PACKAGEUNIT
        defaultBrandShouldNotBeFound("packageunit.lessThan=" + DEFAULT_PACKAGEUNIT);

        // Get all the brandList where packageunit is less than UPDATED_PACKAGEUNIT
        defaultBrandShouldBeFound("packageunit.lessThan=" + UPDATED_PACKAGEUNIT);
    }

    @Test
    @Transactional
    void getAllBrandsByPackageunitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where packageunit is greater than DEFAULT_PACKAGEUNIT
        defaultBrandShouldNotBeFound("packageunit.greaterThan=" + DEFAULT_PACKAGEUNIT);

        // Get all the brandList where packageunit is greater than SMALLER_PACKAGEUNIT
        defaultBrandShouldBeFound("packageunit.greaterThan=" + SMALLER_PACKAGEUNIT);
    }

    @Test
    @Transactional
    void getAllBrandsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where type equals to DEFAULT_TYPE
        defaultBrandShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the brandList where type equals to UPDATED_TYPE
        defaultBrandShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBrandsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where type not equals to DEFAULT_TYPE
        defaultBrandShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the brandList where type not equals to UPDATED_TYPE
        defaultBrandShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBrandsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultBrandShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the brandList where type equals to UPDATED_TYPE
        defaultBrandShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBrandsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where type is not null
        defaultBrandShouldBeFound("type.specified=true");

        // Get all the brandList where type is null
        defaultBrandShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllBrandsByTypeunitIsEqualToSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where typeunit equals to DEFAULT_TYPEUNIT
        defaultBrandShouldBeFound("typeunit.equals=" + DEFAULT_TYPEUNIT);

        // Get all the brandList where typeunit equals to UPDATED_TYPEUNIT
        defaultBrandShouldNotBeFound("typeunit.equals=" + UPDATED_TYPEUNIT);
    }

    @Test
    @Transactional
    void getAllBrandsByTypeunitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where typeunit not equals to DEFAULT_TYPEUNIT
        defaultBrandShouldNotBeFound("typeunit.notEquals=" + DEFAULT_TYPEUNIT);

        // Get all the brandList where typeunit not equals to UPDATED_TYPEUNIT
        defaultBrandShouldBeFound("typeunit.notEquals=" + UPDATED_TYPEUNIT);
    }

    @Test
    @Transactional
    void getAllBrandsByTypeunitIsInShouldWork() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where typeunit in DEFAULT_TYPEUNIT or UPDATED_TYPEUNIT
        defaultBrandShouldBeFound("typeunit.in=" + DEFAULT_TYPEUNIT + "," + UPDATED_TYPEUNIT);

        // Get all the brandList where typeunit equals to UPDATED_TYPEUNIT
        defaultBrandShouldNotBeFound("typeunit.in=" + UPDATED_TYPEUNIT);
    }

    @Test
    @Transactional
    void getAllBrandsByTypeunitIsNullOrNotNull() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        // Get all the brandList where typeunit is not null
        defaultBrandShouldBeFound("typeunit.specified=true");

        // Get all the brandList where typeunit is null
        defaultBrandShouldNotBeFound("typeunit.specified=false");
    }

    @Test
    @Transactional
    void getAllBrandsByCompanyofMedicineIsEqualToSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);
        Company companyofMedicine = CompanyResourceIT.createEntity(em);
        em.persist(companyofMedicine);
        em.flush();
        brand.setCompanyofMedicine(companyofMedicine);
        brandRepository.saveAndFlush(brand);
        Long companyofMedicineId = companyofMedicine.getId();

        // Get all the brandList where companyofMedicine equals to companyofMedicineId
        defaultBrandShouldBeFound("companyofMedicineId.equals=" + companyofMedicineId);

        // Get all the brandList where companyofMedicine equals to (companyofMedicineId + 1)
        defaultBrandShouldNotBeFound("companyofMedicineId.equals=" + (companyofMedicineId + 1));
    }

    @Test
    @Transactional
    void getAllBrandsByGenericsusedIsEqualToSomething() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);
        Generics genericsused = GenericsResourceIT.createEntity(em);
        em.persist(genericsused);
        em.flush();
        brand.addGenericsused(genericsused);
        brandRepository.saveAndFlush(brand);
        Long genericsusedId = genericsused.getId();

        // Get all the brandList where genericsused equals to genericsusedId
        defaultBrandShouldBeFound("genericsusedId.equals=" + genericsusedId);

        // Get all the brandList where genericsused equals to (genericsusedId + 1)
        defaultBrandShouldNotBeFound("genericsusedId.equals=" + (genericsusedId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBrandShouldBeFound(String filter) throws Exception {
        restBrandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(brand.getId().intValue())))
            .andExpect(jsonPath("$.[*].bname").value(hasItem(DEFAULT_BNAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].packageunit").value(hasItem(DEFAULT_PACKAGEUNIT.doubleValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].typeunit").value(hasItem(DEFAULT_TYPEUNIT.toString())));

        // Check, that the count call also returns 1
        restBrandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBrandShouldNotBeFound(String filter) throws Exception {
        restBrandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBrandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBrand() throws Exception {
        // Get the brand
        restBrandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBrand() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        int databaseSizeBeforeUpdate = brandRepository.findAll().size();

        // Update the brand
        Brand updatedBrand = brandRepository.findById(brand.getId()).get();
        // Disconnect from session so that the updates on updatedBrand are not directly saved in db
        em.detach(updatedBrand);
        updatedBrand
            .bname(UPDATED_BNAME)
            .price(UPDATED_PRICE)
            .date(UPDATED_DATE)
            .packageunit(UPDATED_PACKAGEUNIT)
            .type(UPDATED_TYPE)
            .typeunit(UPDATED_TYPEUNIT);
        BrandDTO brandDTO = brandMapper.toDto(updatedBrand);

        restBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, brandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(brandDTO))
            )
            .andExpect(status().isOk());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);
        Brand testBrand = brandList.get(brandList.size() - 1);
        assertThat(testBrand.getBname()).isEqualTo(UPDATED_BNAME);
        assertThat(testBrand.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testBrand.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBrand.getPackageunit()).isEqualTo(UPDATED_PACKAGEUNIT);
        assertThat(testBrand.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBrand.getTypeunit()).isEqualTo(UPDATED_TYPEUNIT);

        // Validate the Brand in Elasticsearch
        verify(mockBrandSearchRepository).save(testBrand);
    }

    @Test
    @Transactional
    void putNonExistingBrand() throws Exception {
        int databaseSizeBeforeUpdate = brandRepository.findAll().size();
        brand.setId(count.incrementAndGet());

        // Create the Brand
        BrandDTO brandDTO = brandMapper.toDto(brand);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, brandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(brandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Brand in Elasticsearch
        verify(mockBrandSearchRepository, times(0)).save(brand);
    }

    @Test
    @Transactional
    void putWithIdMismatchBrand() throws Exception {
        int databaseSizeBeforeUpdate = brandRepository.findAll().size();
        brand.setId(count.incrementAndGet());

        // Create the Brand
        BrandDTO brandDTO = brandMapper.toDto(brand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(brandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Brand in Elasticsearch
        verify(mockBrandSearchRepository, times(0)).save(brand);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBrand() throws Exception {
        int databaseSizeBeforeUpdate = brandRepository.findAll().size();
        brand.setId(count.incrementAndGet());

        // Create the Brand
        BrandDTO brandDTO = brandMapper.toDto(brand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBrandMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(brandDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Brand in Elasticsearch
        verify(mockBrandSearchRepository, times(0)).save(brand);
    }

    @Test
    @Transactional
    void partialUpdateBrandWithPatch() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        int databaseSizeBeforeUpdate = brandRepository.findAll().size();

        // Update the brand using partial update
        Brand partialUpdatedBrand = new Brand();
        partialUpdatedBrand.setId(brand.getId());

        partialUpdatedBrand.price(UPDATED_PRICE).packageunit(UPDATED_PACKAGEUNIT).type(UPDATED_TYPE);

        restBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBrand.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBrand))
            )
            .andExpect(status().isOk());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);
        Brand testBrand = brandList.get(brandList.size() - 1);
        assertThat(testBrand.getBname()).isEqualTo(DEFAULT_BNAME);
        assertThat(testBrand.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testBrand.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testBrand.getPackageunit()).isEqualTo(UPDATED_PACKAGEUNIT);
        assertThat(testBrand.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBrand.getTypeunit()).isEqualTo(DEFAULT_TYPEUNIT);
    }

    @Test
    @Transactional
    void fullUpdateBrandWithPatch() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        int databaseSizeBeforeUpdate = brandRepository.findAll().size();

        // Update the brand using partial update
        Brand partialUpdatedBrand = new Brand();
        partialUpdatedBrand.setId(brand.getId());

        partialUpdatedBrand
            .bname(UPDATED_BNAME)
            .price(UPDATED_PRICE)
            .date(UPDATED_DATE)
            .packageunit(UPDATED_PACKAGEUNIT)
            .type(UPDATED_TYPE)
            .typeunit(UPDATED_TYPEUNIT);

        restBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBrand.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBrand))
            )
            .andExpect(status().isOk());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);
        Brand testBrand = brandList.get(brandList.size() - 1);
        assertThat(testBrand.getBname()).isEqualTo(UPDATED_BNAME);
        assertThat(testBrand.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testBrand.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBrand.getPackageunit()).isEqualTo(UPDATED_PACKAGEUNIT);
        assertThat(testBrand.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBrand.getTypeunit()).isEqualTo(UPDATED_TYPEUNIT);
    }

    @Test
    @Transactional
    void patchNonExistingBrand() throws Exception {
        int databaseSizeBeforeUpdate = brandRepository.findAll().size();
        brand.setId(count.incrementAndGet());

        // Create the Brand
        BrandDTO brandDTO = brandMapper.toDto(brand);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, brandDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(brandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Brand in Elasticsearch
        verify(mockBrandSearchRepository, times(0)).save(brand);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBrand() throws Exception {
        int databaseSizeBeforeUpdate = brandRepository.findAll().size();
        brand.setId(count.incrementAndGet());

        // Create the Brand
        BrandDTO brandDTO = brandMapper.toDto(brand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(brandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Brand in Elasticsearch
        verify(mockBrandSearchRepository, times(0)).save(brand);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBrand() throws Exception {
        int databaseSizeBeforeUpdate = brandRepository.findAll().size();
        brand.setId(count.incrementAndGet());

        // Create the Brand
        BrandDTO brandDTO = brandMapper.toDto(brand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBrandMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(brandDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Brand in Elasticsearch
        verify(mockBrandSearchRepository, times(0)).save(brand);
    }

    @Test
    @Transactional
    void deleteBrand() throws Exception {
        // Initialize the database
        brandRepository.saveAndFlush(brand);

        int databaseSizeBeforeDelete = brandRepository.findAll().size();

        // Delete the brand
        restBrandMockMvc
            .perform(delete(ENTITY_API_URL_ID, brand.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Brand> brandList = brandRepository.findAll();
        assertThat(brandList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Brand in Elasticsearch
        verify(mockBrandSearchRepository, times(1)).deleteById(brand.getId());
    }

    @Test
    @Transactional
    void searchBrand() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        brandRepository.saveAndFlush(brand);
        when(mockBrandSearchRepository.search(queryStringQuery("id:" + brand.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(brand), PageRequest.of(0, 1), 1));

        // Search the brand
        restBrandMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + brand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(brand.getId().intValue())))
            .andExpect(jsonPath("$.[*].bname").value(hasItem(DEFAULT_BNAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].packageunit").value(hasItem(DEFAULT_PACKAGEUNIT.doubleValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].typeunit").value(hasItem(DEFAULT_TYPEUNIT.toString())));
    }
}
