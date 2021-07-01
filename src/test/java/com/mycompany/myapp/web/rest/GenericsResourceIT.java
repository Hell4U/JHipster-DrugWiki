package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Generics;
import com.mycompany.myapp.domain.Ingredients;
import com.mycompany.myapp.domain.enumeration.DosageUnit;
import com.mycompany.myapp.repository.GenericsRepository;
import com.mycompany.myapp.service.EntityManager;
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
 * Integration tests for the {@link GenericsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class GenericsResourceIT {

    private static final Double DEFAULT_DOSAGE = 1D;
    private static final Double UPDATED_DOSAGE = 2D;

    private static final DosageUnit DEFAULT_DOSAGEUNIT = DosageUnit.Microgram;
    private static final DosageUnit UPDATED_DOSAGEUNIT = DosageUnit.Miligram;

    private static final String ENTITY_API_URL = "/api/generics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GenericsRepository genericsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Generics generics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Generics createEntity(EntityManager em) {
        Generics generics = new Generics().dosage(DEFAULT_DOSAGE).dosageunit(DEFAULT_DOSAGEUNIT);
        // Add required entity
        Ingredients ingredients;
        ingredients = em.insert(IngredientsResourceIT.createEntity(em)).block();
        generics.setIngredientName(ingredients);
        return generics;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Generics createUpdatedEntity(EntityManager em) {
        Generics generics = new Generics().dosage(UPDATED_DOSAGE).dosageunit(UPDATED_DOSAGEUNIT);
        // Add required entity
        Ingredients ingredients;
        ingredients = em.insert(IngredientsResourceIT.createUpdatedEntity(em)).block();
        generics.setIngredientName(ingredients);
        return generics;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Generics.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        IngredientsResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        generics = createEntity(em);
    }

    @Test
    void createGenerics() throws Exception {
        int databaseSizeBeforeCreate = genericsRepository.findAll().collectList().block().size();
        // Create the Generics
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(generics))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll().collectList().block();
        assertThat(genericsList).hasSize(databaseSizeBeforeCreate + 1);
        Generics testGenerics = genericsList.get(genericsList.size() - 1);
        assertThat(testGenerics.getDosage()).isEqualTo(DEFAULT_DOSAGE);
        assertThat(testGenerics.getDosageunit()).isEqualTo(DEFAULT_DOSAGEUNIT);
    }

    @Test
    void createGenericsWithExistingId() throws Exception {
        // Create the Generics with an existing ID
        generics.setId(1L);

        int databaseSizeBeforeCreate = genericsRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(generics))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll().collectList().block();
        assertThat(genericsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkDosageIsRequired() throws Exception {
        int databaseSizeBeforeTest = genericsRepository.findAll().collectList().block().size();
        // set the field null
        generics.setDosage(null);

        // Create the Generics, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(generics))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Generics> genericsList = genericsRepository.findAll().collectList().block();
        assertThat(genericsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDosageunitIsRequired() throws Exception {
        int databaseSizeBeforeTest = genericsRepository.findAll().collectList().block().size();
        // set the field null
        generics.setDosageunit(null);

        // Create the Generics, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(generics))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Generics> genericsList = genericsRepository.findAll().collectList().block();
        assertThat(genericsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllGenerics() {
        // Initialize the database
        genericsRepository.save(generics).block();

        // Get all the genericsList
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
            .value(hasItem(generics.getId().intValue()))
            .jsonPath("$.[*].dosage")
            .value(hasItem(DEFAULT_DOSAGE.doubleValue()))
            .jsonPath("$.[*].dosageunit")
            .value(hasItem(DEFAULT_DOSAGEUNIT.toString()));
    }

    @Test
    void getGenerics() {
        // Initialize the database
        genericsRepository.save(generics).block();

        // Get the generics
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, generics.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(generics.getId().intValue()))
            .jsonPath("$.dosage")
            .value(is(DEFAULT_DOSAGE.doubleValue()))
            .jsonPath("$.dosageunit")
            .value(is(DEFAULT_DOSAGEUNIT.toString()));
    }

    @Test
    void getNonExistingGenerics() {
        // Get the generics
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewGenerics() throws Exception {
        // Initialize the database
        genericsRepository.save(generics).block();

        int databaseSizeBeforeUpdate = genericsRepository.findAll().collectList().block().size();

        // Update the generics
        Generics updatedGenerics = genericsRepository.findById(generics.getId()).block();
        updatedGenerics.dosage(UPDATED_DOSAGE).dosageunit(UPDATED_DOSAGEUNIT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedGenerics.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedGenerics))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll().collectList().block();
        assertThat(genericsList).hasSize(databaseSizeBeforeUpdate);
        Generics testGenerics = genericsList.get(genericsList.size() - 1);
        assertThat(testGenerics.getDosage()).isEqualTo(UPDATED_DOSAGE);
        assertThat(testGenerics.getDosageunit()).isEqualTo(UPDATED_DOSAGEUNIT);
    }

    @Test
    void putNonExistingGenerics() throws Exception {
        int databaseSizeBeforeUpdate = genericsRepository.findAll().collectList().block().size();
        generics.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, generics.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(generics))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll().collectList().block();
        assertThat(genericsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchGenerics() throws Exception {
        int databaseSizeBeforeUpdate = genericsRepository.findAll().collectList().block().size();
        generics.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(generics))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll().collectList().block();
        assertThat(genericsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamGenerics() throws Exception {
        int databaseSizeBeforeUpdate = genericsRepository.findAll().collectList().block().size();
        generics.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(generics))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll().collectList().block();
        assertThat(genericsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateGenericsWithPatch() throws Exception {
        // Initialize the database
        genericsRepository.save(generics).block();

        int databaseSizeBeforeUpdate = genericsRepository.findAll().collectList().block().size();

        // Update the generics using partial update
        Generics partialUpdatedGenerics = new Generics();
        partialUpdatedGenerics.setId(generics.getId());

        partialUpdatedGenerics.dosageunit(UPDATED_DOSAGEUNIT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGenerics.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGenerics))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll().collectList().block();
        assertThat(genericsList).hasSize(databaseSizeBeforeUpdate);
        Generics testGenerics = genericsList.get(genericsList.size() - 1);
        assertThat(testGenerics.getDosage()).isEqualTo(DEFAULT_DOSAGE);
        assertThat(testGenerics.getDosageunit()).isEqualTo(UPDATED_DOSAGEUNIT);
    }

    @Test
    void fullUpdateGenericsWithPatch() throws Exception {
        // Initialize the database
        genericsRepository.save(generics).block();

        int databaseSizeBeforeUpdate = genericsRepository.findAll().collectList().block().size();

        // Update the generics using partial update
        Generics partialUpdatedGenerics = new Generics();
        partialUpdatedGenerics.setId(generics.getId());

        partialUpdatedGenerics.dosage(UPDATED_DOSAGE).dosageunit(UPDATED_DOSAGEUNIT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGenerics.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGenerics))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll().collectList().block();
        assertThat(genericsList).hasSize(databaseSizeBeforeUpdate);
        Generics testGenerics = genericsList.get(genericsList.size() - 1);
        assertThat(testGenerics.getDosage()).isEqualTo(UPDATED_DOSAGE);
        assertThat(testGenerics.getDosageunit()).isEqualTo(UPDATED_DOSAGEUNIT);
    }

    @Test
    void patchNonExistingGenerics() throws Exception {
        int databaseSizeBeforeUpdate = genericsRepository.findAll().collectList().block().size();
        generics.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, generics.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(generics))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll().collectList().block();
        assertThat(genericsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchGenerics() throws Exception {
        int databaseSizeBeforeUpdate = genericsRepository.findAll().collectList().block().size();
        generics.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(generics))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll().collectList().block();
        assertThat(genericsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamGenerics() throws Exception {
        int databaseSizeBeforeUpdate = genericsRepository.findAll().collectList().block().size();
        generics.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(generics))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll().collectList().block();
        assertThat(genericsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteGenerics() {
        // Initialize the database
        genericsRepository.save(generics).block();

        int databaseSizeBeforeDelete = genericsRepository.findAll().collectList().block().size();

        // Delete the generics
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, generics.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Generics> genericsList = genericsRepository.findAll().collectList().block();
        assertThat(genericsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
