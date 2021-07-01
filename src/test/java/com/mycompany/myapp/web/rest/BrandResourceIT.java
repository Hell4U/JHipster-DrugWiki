package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Brand;
import com.mycompany.myapp.domain.Company;
import com.mycompany.myapp.domain.Generics;
import com.mycompany.myapp.domain.enumeration.BrandType;
import com.mycompany.myapp.domain.enumeration.TypeUnit;
import com.mycompany.myapp.repository.BrandRepository;
import com.mycompany.myapp.service.EntityManager;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Integration tests for the {@link BrandResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient
@WithMockUser
class BrandResourceIT {

    private static final String DEFAULT_BNAME = "AAAAAAAAAA";
    private static final String UPDATED_BNAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_PACKAGEUNIT = 1D;
    private static final Double UPDATED_PACKAGEUNIT = 2D;

    private static final BrandType DEFAULT_TYPE = BrandType.Tablet;
    private static final BrandType UPDATED_TYPE = BrandType.Injection;

    private static final TypeUnit DEFAULT_TYPEUNIT = TypeUnit.PCS;
    private static final TypeUnit UPDATED_TYPEUNIT = TypeUnit.Miligram;

    private static final String ENTITY_API_URL = "/api/brands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BrandRepository brandRepository;

    @Mock
    private BrandRepository brandRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

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
        company = em.insert(CompanyResourceIT.createEntity(em)).block();
        brand.setCompanyofMedicine(company);
        // Add required entity
        Generics generics;
        generics = em.insert(GenericsResourceIT.createEntity(em)).block();
        brand.getGenerics().add(generics);
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
        company = em.insert(CompanyResourceIT.createUpdatedEntity(em)).block();
        brand.setCompanyofMedicine(company);
        // Add required entity
        Generics generics;
        generics = em.insert(GenericsResourceIT.createUpdatedEntity(em)).block();
        brand.getGenerics().add(generics);
        return brand;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_brand__generics").block();
            em.deleteAll(Brand.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        CompanyResourceIT.deleteEntities(em);
        GenericsResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        brand = createEntity(em);
    }

    @Test
    void createBrand() throws Exception {
        int databaseSizeBeforeCreate = brandRepository.findAll().collectList().block().size();
        // Create the Brand
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(brand))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll().collectList().block();
        assertThat(brandList).hasSize(databaseSizeBeforeCreate + 1);
        Brand testBrand = brandList.get(brandList.size() - 1);
        assertThat(testBrand.getBname()).isEqualTo(DEFAULT_BNAME);
        assertThat(testBrand.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testBrand.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testBrand.getPackageunit()).isEqualTo(DEFAULT_PACKAGEUNIT);
        assertThat(testBrand.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testBrand.getTypeunit()).isEqualTo(DEFAULT_TYPEUNIT);
    }

    @Test
    void createBrandWithExistingId() throws Exception {
        // Create the Brand with an existing ID
        brand.setId(1L);

        int databaseSizeBeforeCreate = brandRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(brand))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll().collectList().block();
        assertThat(brandList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkBnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = brandRepository.findAll().collectList().block().size();
        // set the field null
        brand.setBname(null);

        // Create the Brand, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(brand))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Brand> brandList = brandRepository.findAll().collectList().block();
        assertThat(brandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = brandRepository.findAll().collectList().block().size();
        // set the field null
        brand.setPrice(null);

        // Create the Brand, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(brand))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Brand> brandList = brandRepository.findAll().collectList().block();
        assertThat(brandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = brandRepository.findAll().collectList().block().size();
        // set the field null
        brand.setDate(null);

        // Create the Brand, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(brand))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Brand> brandList = brandRepository.findAll().collectList().block();
        assertThat(brandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPackageunitIsRequired() throws Exception {
        int databaseSizeBeforeTest = brandRepository.findAll().collectList().block().size();
        // set the field null
        brand.setPackageunit(null);

        // Create the Brand, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(brand))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Brand> brandList = brandRepository.findAll().collectList().block();
        assertThat(brandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = brandRepository.findAll().collectList().block().size();
        // set the field null
        brand.setType(null);

        // Create the Brand, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(brand))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Brand> brandList = brandRepository.findAll().collectList().block();
        assertThat(brandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeunitIsRequired() throws Exception {
        int databaseSizeBeforeTest = brandRepository.findAll().collectList().block().size();
        // set the field null
        brand.setTypeunit(null);

        // Create the Brand, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(brand))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Brand> brandList = brandRepository.findAll().collectList().block();
        assertThat(brandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllBrands() {
        // Initialize the database
        brandRepository.save(brand).block();

        // Get all the brandList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(brand.getId().intValue()))
            .jsonPath("$.[*].bname")
            .value(hasItem(DEFAULT_BNAME))
            .jsonPath("$.[*].price")
            .value(hasItem(DEFAULT_PRICE.doubleValue()))
            .jsonPath("$.[*].date")
            .value(hasItem(DEFAULT_DATE.toString()))
            .jsonPath("$.[*].packageunit")
            .value(hasItem(DEFAULT_PACKAGEUNIT.doubleValue()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()))
            .jsonPath("$.[*].typeunit")
            .value(hasItem(DEFAULT_TYPEUNIT.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBrandsWithEagerRelationshipsIsEnabled() {
        when(brandRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(brandRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBrandsWithEagerRelationshipsIsNotEnabled() {
        when(brandRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(brandRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getBrand() {
        // Initialize the database
        brandRepository.save(brand).block();

        // Get the brand
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, brand.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(brand.getId().intValue()))
            .jsonPath("$.bname")
            .value(is(DEFAULT_BNAME))
            .jsonPath("$.price")
            .value(is(DEFAULT_PRICE.doubleValue()))
            .jsonPath("$.date")
            .value(is(DEFAULT_DATE.toString()))
            .jsonPath("$.packageunit")
            .value(is(DEFAULT_PACKAGEUNIT.doubleValue()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE.toString()))
            .jsonPath("$.typeunit")
            .value(is(DEFAULT_TYPEUNIT.toString()));
    }

    @Test
    void getNonExistingBrand() {
        // Get the brand
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewBrand() throws Exception {
        // Initialize the database
        brandRepository.save(brand).block();

        int databaseSizeBeforeUpdate = brandRepository.findAll().collectList().block().size();

        // Update the brand
        Brand updatedBrand = brandRepository.findById(brand.getId()).block();
        updatedBrand
            .bname(UPDATED_BNAME)
            .price(UPDATED_PRICE)
            .date(UPDATED_DATE)
            .packageunit(UPDATED_PACKAGEUNIT)
            .type(UPDATED_TYPE)
            .typeunit(UPDATED_TYPEUNIT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedBrand.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedBrand))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll().collectList().block();
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
    void putNonExistingBrand() throws Exception {
        int databaseSizeBeforeUpdate = brandRepository.findAll().collectList().block().size();
        brand.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, brand.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(brand))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll().collectList().block();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBrand() throws Exception {
        int databaseSizeBeforeUpdate = brandRepository.findAll().collectList().block().size();
        brand.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(brand))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll().collectList().block();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBrand() throws Exception {
        int databaseSizeBeforeUpdate = brandRepository.findAll().collectList().block().size();
        brand.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(brand))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll().collectList().block();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBrandWithPatch() throws Exception {
        // Initialize the database
        brandRepository.save(brand).block();

        int databaseSizeBeforeUpdate = brandRepository.findAll().collectList().block().size();

        // Update the brand using partial update
        Brand partialUpdatedBrand = new Brand();
        partialUpdatedBrand.setId(brand.getId());

        partialUpdatedBrand
            .bname(UPDATED_BNAME)
            .date(UPDATED_DATE)
            .packageunit(UPDATED_PACKAGEUNIT)
            .type(UPDATED_TYPE)
            .typeunit(UPDATED_TYPEUNIT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBrand.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBrand))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll().collectList().block();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);
        Brand testBrand = brandList.get(brandList.size() - 1);
        assertThat(testBrand.getBname()).isEqualTo(UPDATED_BNAME);
        assertThat(testBrand.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testBrand.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBrand.getPackageunit()).isEqualTo(UPDATED_PACKAGEUNIT);
        assertThat(testBrand.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBrand.getTypeunit()).isEqualTo(UPDATED_TYPEUNIT);
    }

    @Test
    void fullUpdateBrandWithPatch() throws Exception {
        // Initialize the database
        brandRepository.save(brand).block();

        int databaseSizeBeforeUpdate = brandRepository.findAll().collectList().block().size();

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

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBrand.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBrand))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll().collectList().block();
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
    void patchNonExistingBrand() throws Exception {
        int databaseSizeBeforeUpdate = brandRepository.findAll().collectList().block().size();
        brand.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, brand.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(brand))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll().collectList().block();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBrand() throws Exception {
        int databaseSizeBeforeUpdate = brandRepository.findAll().collectList().block().size();
        brand.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(brand))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll().collectList().block();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBrand() throws Exception {
        int databaseSizeBeforeUpdate = brandRepository.findAll().collectList().block().size();
        brand.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(brand))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Brand in the database
        List<Brand> brandList = brandRepository.findAll().collectList().block();
        assertThat(brandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBrand() {
        // Initialize the database
        brandRepository.save(brand).block();

        int databaseSizeBeforeDelete = brandRepository.findAll().collectList().block().size();

        // Delete the brand
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, brand.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Brand> brandList = brandRepository.findAll().collectList().block();
        assertThat(brandList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
