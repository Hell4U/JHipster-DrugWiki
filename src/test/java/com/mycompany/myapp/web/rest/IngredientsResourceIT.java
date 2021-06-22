package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Ingredients;
import com.mycompany.myapp.repository.IngredientsRepository;
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
 * Integration tests for the {@link IngredientsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class IngredientsResourceIT {

    private static final String DEFAULT_INAME = "AAAAAAAAAA";
    private static final String UPDATED_INAME = "BBBBBBBBBB";

    private static final String DEFAULT_SYMPTOMS = "AAAAAAAAAA";
    private static final String UPDATED_SYMPTOMS = "BBBBBBBBBB";

    private static final String DEFAULT_SIDEEFFECTS = "AAAAAAAAAA";
    private static final String UPDATED_SIDEEFFECTS = "BBBBBBBBBB";

    private static final String DEFAULT_CAUTIONS = "AAAAAAAAAA";
    private static final String UPDATED_CAUTIONS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ingredients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IngredientsRepository ingredientsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Ingredients ingredients;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ingredients createEntity(EntityManager em) {
        Ingredients ingredients = new Ingredients()
            .iname(DEFAULT_INAME)
            .symptoms(DEFAULT_SYMPTOMS)
            .sideeffects(DEFAULT_SIDEEFFECTS)
            .cautions(DEFAULT_CAUTIONS);
        return ingredients;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ingredients createUpdatedEntity(EntityManager em) {
        Ingredients ingredients = new Ingredients()
            .iname(UPDATED_INAME)
            .symptoms(UPDATED_SYMPTOMS)
            .sideeffects(UPDATED_SIDEEFFECTS)
            .cautions(UPDATED_CAUTIONS);
        return ingredients;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Ingredients.class).block();
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
        ingredients = createEntity(em);
    }

    @Test
    void createIngredients() throws Exception {
        int databaseSizeBeforeCreate = ingredientsRepository.findAll().collectList().block().size();
        // Create the Ingredients
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingredients))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll().collectList().block();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeCreate + 1);
        Ingredients testIngredients = ingredientsList.get(ingredientsList.size() - 1);
        assertThat(testIngredients.getIname()).isEqualTo(DEFAULT_INAME);
        assertThat(testIngredients.getSymptoms()).isEqualTo(DEFAULT_SYMPTOMS);
        assertThat(testIngredients.getSideeffects()).isEqualTo(DEFAULT_SIDEEFFECTS);
        assertThat(testIngredients.getCautions()).isEqualTo(DEFAULT_CAUTIONS);
    }

    @Test
    void createIngredientsWithExistingId() throws Exception {
        // Create the Ingredients with an existing ID
        ingredients.setId(1L);

        int databaseSizeBeforeCreate = ingredientsRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingredients))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll().collectList().block();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkInameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredientsRepository.findAll().collectList().block().size();
        // set the field null
        ingredients.setIname(null);

        // Create the Ingredients, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingredients))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Ingredients> ingredientsList = ingredientsRepository.findAll().collectList().block();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSymptomsIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredientsRepository.findAll().collectList().block().size();
        // set the field null
        ingredients.setSymptoms(null);

        // Create the Ingredients, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingredients))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Ingredients> ingredientsList = ingredientsRepository.findAll().collectList().block();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSideeffectsIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredientsRepository.findAll().collectList().block().size();
        // set the field null
        ingredients.setSideeffects(null);

        // Create the Ingredients, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingredients))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Ingredients> ingredientsList = ingredientsRepository.findAll().collectList().block();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCautionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredientsRepository.findAll().collectList().block().size();
        // set the field null
        ingredients.setCautions(null);

        // Create the Ingredients, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingredients))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Ingredients> ingredientsList = ingredientsRepository.findAll().collectList().block();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllIngredients() {
        // Initialize the database
        ingredientsRepository.save(ingredients).block();

        // Get all the ingredientsList
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
            .value(hasItem(ingredients.getId().intValue()))
            .jsonPath("$.[*].iname")
            .value(hasItem(DEFAULT_INAME))
            .jsonPath("$.[*].symptoms")
            .value(hasItem(DEFAULT_SYMPTOMS))
            .jsonPath("$.[*].sideeffects")
            .value(hasItem(DEFAULT_SIDEEFFECTS))
            .jsonPath("$.[*].cautions")
            .value(hasItem(DEFAULT_CAUTIONS));
    }

    @Test
    void getIngredients() {
        // Initialize the database
        ingredientsRepository.save(ingredients).block();

        // Get the ingredients
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, ingredients.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(ingredients.getId().intValue()))
            .jsonPath("$.iname")
            .value(is(DEFAULT_INAME))
            .jsonPath("$.symptoms")
            .value(is(DEFAULT_SYMPTOMS))
            .jsonPath("$.sideeffects")
            .value(is(DEFAULT_SIDEEFFECTS))
            .jsonPath("$.cautions")
            .value(is(DEFAULT_CAUTIONS));
    }

    @Test
    void getNonExistingIngredients() {
        // Get the ingredients
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewIngredients() throws Exception {
        // Initialize the database
        ingredientsRepository.save(ingredients).block();

        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().collectList().block().size();

        // Update the ingredients
        Ingredients updatedIngredients = ingredientsRepository.findById(ingredients.getId()).block();
        updatedIngredients.iname(UPDATED_INAME).symptoms(UPDATED_SYMPTOMS).sideeffects(UPDATED_SIDEEFFECTS).cautions(UPDATED_CAUTIONS);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedIngredients.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedIngredients))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll().collectList().block();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
        Ingredients testIngredients = ingredientsList.get(ingredientsList.size() - 1);
        assertThat(testIngredients.getIname()).isEqualTo(UPDATED_INAME);
        assertThat(testIngredients.getSymptoms()).isEqualTo(UPDATED_SYMPTOMS);
        assertThat(testIngredients.getSideeffects()).isEqualTo(UPDATED_SIDEEFFECTS);
        assertThat(testIngredients.getCautions()).isEqualTo(UPDATED_CAUTIONS);
    }

    @Test
    void putNonExistingIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().collectList().block().size();
        ingredients.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ingredients.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingredients))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll().collectList().block();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().collectList().block().size();
        ingredients.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingredients))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll().collectList().block();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().collectList().block().size();
        ingredients.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingredients))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll().collectList().block();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIngredientsWithPatch() throws Exception {
        // Initialize the database
        ingredientsRepository.save(ingredients).block();

        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().collectList().block().size();

        // Update the ingredients using partial update
        Ingredients partialUpdatedIngredients = new Ingredients();
        partialUpdatedIngredients.setId(ingredients.getId());

        partialUpdatedIngredients.cautions(UPDATED_CAUTIONS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIngredients.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIngredients))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll().collectList().block();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
        Ingredients testIngredients = ingredientsList.get(ingredientsList.size() - 1);
        assertThat(testIngredients.getIname()).isEqualTo(DEFAULT_INAME);
        assertThat(testIngredients.getSymptoms()).isEqualTo(DEFAULT_SYMPTOMS);
        assertThat(testIngredients.getSideeffects()).isEqualTo(DEFAULT_SIDEEFFECTS);
        assertThat(testIngredients.getCautions()).isEqualTo(UPDATED_CAUTIONS);
    }

    @Test
    void fullUpdateIngredientsWithPatch() throws Exception {
        // Initialize the database
        ingredientsRepository.save(ingredients).block();

        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().collectList().block().size();

        // Update the ingredients using partial update
        Ingredients partialUpdatedIngredients = new Ingredients();
        partialUpdatedIngredients.setId(ingredients.getId());

        partialUpdatedIngredients
            .iname(UPDATED_INAME)
            .symptoms(UPDATED_SYMPTOMS)
            .sideeffects(UPDATED_SIDEEFFECTS)
            .cautions(UPDATED_CAUTIONS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIngredients.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIngredients))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll().collectList().block();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
        Ingredients testIngredients = ingredientsList.get(ingredientsList.size() - 1);
        assertThat(testIngredients.getIname()).isEqualTo(UPDATED_INAME);
        assertThat(testIngredients.getSymptoms()).isEqualTo(UPDATED_SYMPTOMS);
        assertThat(testIngredients.getSideeffects()).isEqualTo(UPDATED_SIDEEFFECTS);
        assertThat(testIngredients.getCautions()).isEqualTo(UPDATED_CAUTIONS);
    }

    @Test
    void patchNonExistingIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().collectList().block().size();
        ingredients.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, ingredients.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingredients))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll().collectList().block();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().collectList().block().size();
        ingredients.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingredients))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll().collectList().block();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().collectList().block().size();
        ingredients.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingredients))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll().collectList().block();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIngredients() {
        // Initialize the database
        ingredientsRepository.save(ingredients).block();

        int databaseSizeBeforeDelete = ingredientsRepository.findAll().collectList().block().size();

        // Delete the ingredients
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, ingredients.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Ingredients> ingredientsList = ingredientsRepository.findAll().collectList().block();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
