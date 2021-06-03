package com.promition.drugwiki.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.promition.drugwiki.IntegrationTest;
import com.promition.drugwiki.domain.Brand;
import com.promition.drugwiki.repository.BrandRepository;
import com.promition.drugwiki.service.EntityManager;
import java.time.Duration;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link BrandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class BrandResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    private static final String ENTITY_API_URL = "/api/brands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BrandRepository brandRepository;

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
        Brand brand = new Brand().name(DEFAULT_NAME).price(DEFAULT_PRICE);
        return brand;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Brand createUpdatedEntity(EntityManager em) {
        Brand brand = new Brand().name(UPDATED_NAME).price(UPDATED_PRICE);
        return brand;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Brand.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
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
        assertThat(testBrand.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBrand.getPrice()).isEqualTo(DEFAULT_PRICE);
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
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = brandRepository.findAll().collectList().block().size();
        // set the field null
        brand.setName(null);

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
    void getAllBrandsAsStream() {
        // Initialize the database
        brandRepository.save(brand).block();

        List<Brand> brandList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Brand.class)
            .getResponseBody()
            .filter(brand::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(brandList).isNotNull();
        assertThat(brandList).hasSize(1);
        Brand testBrand = brandList.get(0);
        assertThat(testBrand.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBrand.getPrice()).isEqualTo(DEFAULT_PRICE);
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
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].price")
            .value(hasItem(DEFAULT_PRICE.doubleValue()));
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
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.price")
            .value(is(DEFAULT_PRICE.doubleValue()));
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
        updatedBrand.name(UPDATED_NAME).price(UPDATED_PRICE);

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
        assertThat(testBrand.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBrand.getPrice()).isEqualTo(UPDATED_PRICE);
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

        partialUpdatedBrand.name(UPDATED_NAME);

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
        assertThat(testBrand.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBrand.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    void fullUpdateBrandWithPatch() throws Exception {
        // Initialize the database
        brandRepository.save(brand).block();

        int databaseSizeBeforeUpdate = brandRepository.findAll().collectList().block().size();

        // Update the brand using partial update
        Brand partialUpdatedBrand = new Brand();
        partialUpdatedBrand.setId(brand.getId());

        partialUpdatedBrand.name(UPDATED_NAME).price(UPDATED_PRICE);

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
        assertThat(testBrand.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBrand.getPrice()).isEqualTo(UPDATED_PRICE);
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
