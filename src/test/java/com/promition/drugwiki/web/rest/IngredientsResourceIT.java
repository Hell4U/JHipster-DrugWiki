package com.promition.drugwiki.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.promition.drugwiki.IntegrationTest;
import com.promition.drugwiki.domain.Ingredients;
import com.promition.drugwiki.repository.IngredientsRepository;
import com.promition.drugwiki.repository.search.IngredientsSearchRepository;
import com.promition.drugwiki.service.criteria.IngredientsCriteria;
import com.promition.drugwiki.service.dto.IngredientsDTO;
import com.promition.drugwiki.service.mapper.IngredientsMapper;
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
 * Integration tests for the {@link IngredientsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
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
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/ingredients";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IngredientsRepository ingredientsRepository;

    @Autowired
    private IngredientsMapper ingredientsMapper;

    /**
     * This repository is mocked in the com.promition.drugwiki.repository.search test package.
     *
     * @see com.promition.drugwiki.repository.search.IngredientsSearchRepositoryMockConfiguration
     */
    @Autowired
    private IngredientsSearchRepository mockIngredientsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIngredientsMockMvc;

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

    @BeforeEach
    public void initTest() {
        ingredients = createEntity(em);
    }

    @Test
    @Transactional
    void createIngredients() throws Exception {
        int databaseSizeBeforeCreate = ingredientsRepository.findAll().size();
        // Create the Ingredients
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(ingredients);
        restIngredientsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeCreate + 1);
        Ingredients testIngredients = ingredientsList.get(ingredientsList.size() - 1);
        assertThat(testIngredients.getIname()).isEqualTo(DEFAULT_INAME);
        assertThat(testIngredients.getSymptoms()).isEqualTo(DEFAULT_SYMPTOMS);
        assertThat(testIngredients.getSideeffects()).isEqualTo(DEFAULT_SIDEEFFECTS);
        assertThat(testIngredients.getCautions()).isEqualTo(DEFAULT_CAUTIONS);

        // Validate the Ingredients in Elasticsearch
        verify(mockIngredientsSearchRepository, times(1)).save(testIngredients);
    }

    @Test
    @Transactional
    void createIngredientsWithExistingId() throws Exception {
        // Create the Ingredients with an existing ID
        ingredients.setId(1L);
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(ingredients);

        int databaseSizeBeforeCreate = ingredientsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIngredientsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeCreate);

        // Validate the Ingredients in Elasticsearch
        verify(mockIngredientsSearchRepository, times(0)).save(ingredients);
    }

    @Test
    @Transactional
    void checkInameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredientsRepository.findAll().size();
        // set the field null
        ingredients.setIname(null);

        // Create the Ingredients, which fails.
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(ingredients);

        restIngredientsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSymptomsIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredientsRepository.findAll().size();
        // set the field null
        ingredients.setSymptoms(null);

        // Create the Ingredients, which fails.
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(ingredients);

        restIngredientsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSideeffectsIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredientsRepository.findAll().size();
        // set the field null
        ingredients.setSideeffects(null);

        // Create the Ingredients, which fails.
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(ingredients);

        restIngredientsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCautionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredientsRepository.findAll().size();
        // set the field null
        ingredients.setCautions(null);

        // Create the Ingredients, which fails.
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(ingredients);

        restIngredientsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIngredients() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList
        restIngredientsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingredients.getId().intValue())))
            .andExpect(jsonPath("$.[*].iname").value(hasItem(DEFAULT_INAME)))
            .andExpect(jsonPath("$.[*].symptoms").value(hasItem(DEFAULT_SYMPTOMS)))
            .andExpect(jsonPath("$.[*].sideeffects").value(hasItem(DEFAULT_SIDEEFFECTS)))
            .andExpect(jsonPath("$.[*].cautions").value(hasItem(DEFAULT_CAUTIONS)));
    }

    @Test
    @Transactional
    void getIngredients() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get the ingredients
        restIngredientsMockMvc
            .perform(get(ENTITY_API_URL_ID, ingredients.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ingredients.getId().intValue()))
            .andExpect(jsonPath("$.iname").value(DEFAULT_INAME))
            .andExpect(jsonPath("$.symptoms").value(DEFAULT_SYMPTOMS))
            .andExpect(jsonPath("$.sideeffects").value(DEFAULT_SIDEEFFECTS))
            .andExpect(jsonPath("$.cautions").value(DEFAULT_CAUTIONS));
    }

    @Test
    @Transactional
    void getIngredientsByIdFiltering() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        Long id = ingredients.getId();

        defaultIngredientsShouldBeFound("id.equals=" + id);
        defaultIngredientsShouldNotBeFound("id.notEquals=" + id);

        defaultIngredientsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIngredientsShouldNotBeFound("id.greaterThan=" + id);

        defaultIngredientsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIngredientsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIngredientsByInameIsEqualToSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where iname equals to DEFAULT_INAME
        defaultIngredientsShouldBeFound("iname.equals=" + DEFAULT_INAME);

        // Get all the ingredientsList where iname equals to UPDATED_INAME
        defaultIngredientsShouldNotBeFound("iname.equals=" + UPDATED_INAME);
    }

    @Test
    @Transactional
    void getAllIngredientsByInameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where iname not equals to DEFAULT_INAME
        defaultIngredientsShouldNotBeFound("iname.notEquals=" + DEFAULT_INAME);

        // Get all the ingredientsList where iname not equals to UPDATED_INAME
        defaultIngredientsShouldBeFound("iname.notEquals=" + UPDATED_INAME);
    }

    @Test
    @Transactional
    void getAllIngredientsByInameIsInShouldWork() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where iname in DEFAULT_INAME or UPDATED_INAME
        defaultIngredientsShouldBeFound("iname.in=" + DEFAULT_INAME + "," + UPDATED_INAME);

        // Get all the ingredientsList where iname equals to UPDATED_INAME
        defaultIngredientsShouldNotBeFound("iname.in=" + UPDATED_INAME);
    }

    @Test
    @Transactional
    void getAllIngredientsByInameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where iname is not null
        defaultIngredientsShouldBeFound("iname.specified=true");

        // Get all the ingredientsList where iname is null
        defaultIngredientsShouldNotBeFound("iname.specified=false");
    }

    @Test
    @Transactional
    void getAllIngredientsByInameContainsSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where iname contains DEFAULT_INAME
        defaultIngredientsShouldBeFound("iname.contains=" + DEFAULT_INAME);

        // Get all the ingredientsList where iname contains UPDATED_INAME
        defaultIngredientsShouldNotBeFound("iname.contains=" + UPDATED_INAME);
    }

    @Test
    @Transactional
    void getAllIngredientsByInameNotContainsSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where iname does not contain DEFAULT_INAME
        defaultIngredientsShouldNotBeFound("iname.doesNotContain=" + DEFAULT_INAME);

        // Get all the ingredientsList where iname does not contain UPDATED_INAME
        defaultIngredientsShouldBeFound("iname.doesNotContain=" + UPDATED_INAME);
    }

    @Test
    @Transactional
    void getAllIngredientsBySymptomsIsEqualToSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where symptoms equals to DEFAULT_SYMPTOMS
        defaultIngredientsShouldBeFound("symptoms.equals=" + DEFAULT_SYMPTOMS);

        // Get all the ingredientsList where symptoms equals to UPDATED_SYMPTOMS
        defaultIngredientsShouldNotBeFound("symptoms.equals=" + UPDATED_SYMPTOMS);
    }

    @Test
    @Transactional
    void getAllIngredientsBySymptomsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where symptoms not equals to DEFAULT_SYMPTOMS
        defaultIngredientsShouldNotBeFound("symptoms.notEquals=" + DEFAULT_SYMPTOMS);

        // Get all the ingredientsList where symptoms not equals to UPDATED_SYMPTOMS
        defaultIngredientsShouldBeFound("symptoms.notEquals=" + UPDATED_SYMPTOMS);
    }

    @Test
    @Transactional
    void getAllIngredientsBySymptomsIsInShouldWork() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where symptoms in DEFAULT_SYMPTOMS or UPDATED_SYMPTOMS
        defaultIngredientsShouldBeFound("symptoms.in=" + DEFAULT_SYMPTOMS + "," + UPDATED_SYMPTOMS);

        // Get all the ingredientsList where symptoms equals to UPDATED_SYMPTOMS
        defaultIngredientsShouldNotBeFound("symptoms.in=" + UPDATED_SYMPTOMS);
    }

    @Test
    @Transactional
    void getAllIngredientsBySymptomsIsNullOrNotNull() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where symptoms is not null
        defaultIngredientsShouldBeFound("symptoms.specified=true");

        // Get all the ingredientsList where symptoms is null
        defaultIngredientsShouldNotBeFound("symptoms.specified=false");
    }

    @Test
    @Transactional
    void getAllIngredientsBySymptomsContainsSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where symptoms contains DEFAULT_SYMPTOMS
        defaultIngredientsShouldBeFound("symptoms.contains=" + DEFAULT_SYMPTOMS);

        // Get all the ingredientsList where symptoms contains UPDATED_SYMPTOMS
        defaultIngredientsShouldNotBeFound("symptoms.contains=" + UPDATED_SYMPTOMS);
    }

    @Test
    @Transactional
    void getAllIngredientsBySymptomsNotContainsSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where symptoms does not contain DEFAULT_SYMPTOMS
        defaultIngredientsShouldNotBeFound("symptoms.doesNotContain=" + DEFAULT_SYMPTOMS);

        // Get all the ingredientsList where symptoms does not contain UPDATED_SYMPTOMS
        defaultIngredientsShouldBeFound("symptoms.doesNotContain=" + UPDATED_SYMPTOMS);
    }

    @Test
    @Transactional
    void getAllIngredientsBySideeffectsIsEqualToSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where sideeffects equals to DEFAULT_SIDEEFFECTS
        defaultIngredientsShouldBeFound("sideeffects.equals=" + DEFAULT_SIDEEFFECTS);

        // Get all the ingredientsList where sideeffects equals to UPDATED_SIDEEFFECTS
        defaultIngredientsShouldNotBeFound("sideeffects.equals=" + UPDATED_SIDEEFFECTS);
    }

    @Test
    @Transactional
    void getAllIngredientsBySideeffectsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where sideeffects not equals to DEFAULT_SIDEEFFECTS
        defaultIngredientsShouldNotBeFound("sideeffects.notEquals=" + DEFAULT_SIDEEFFECTS);

        // Get all the ingredientsList where sideeffects not equals to UPDATED_SIDEEFFECTS
        defaultIngredientsShouldBeFound("sideeffects.notEquals=" + UPDATED_SIDEEFFECTS);
    }

    @Test
    @Transactional
    void getAllIngredientsBySideeffectsIsInShouldWork() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where sideeffects in DEFAULT_SIDEEFFECTS or UPDATED_SIDEEFFECTS
        defaultIngredientsShouldBeFound("sideeffects.in=" + DEFAULT_SIDEEFFECTS + "," + UPDATED_SIDEEFFECTS);

        // Get all the ingredientsList where sideeffects equals to UPDATED_SIDEEFFECTS
        defaultIngredientsShouldNotBeFound("sideeffects.in=" + UPDATED_SIDEEFFECTS);
    }

    @Test
    @Transactional
    void getAllIngredientsBySideeffectsIsNullOrNotNull() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where sideeffects is not null
        defaultIngredientsShouldBeFound("sideeffects.specified=true");

        // Get all the ingredientsList where sideeffects is null
        defaultIngredientsShouldNotBeFound("sideeffects.specified=false");
    }

    @Test
    @Transactional
    void getAllIngredientsBySideeffectsContainsSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where sideeffects contains DEFAULT_SIDEEFFECTS
        defaultIngredientsShouldBeFound("sideeffects.contains=" + DEFAULT_SIDEEFFECTS);

        // Get all the ingredientsList where sideeffects contains UPDATED_SIDEEFFECTS
        defaultIngredientsShouldNotBeFound("sideeffects.contains=" + UPDATED_SIDEEFFECTS);
    }

    @Test
    @Transactional
    void getAllIngredientsBySideeffectsNotContainsSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where sideeffects does not contain DEFAULT_SIDEEFFECTS
        defaultIngredientsShouldNotBeFound("sideeffects.doesNotContain=" + DEFAULT_SIDEEFFECTS);

        // Get all the ingredientsList where sideeffects does not contain UPDATED_SIDEEFFECTS
        defaultIngredientsShouldBeFound("sideeffects.doesNotContain=" + UPDATED_SIDEEFFECTS);
    }

    @Test
    @Transactional
    void getAllIngredientsByCautionsIsEqualToSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where cautions equals to DEFAULT_CAUTIONS
        defaultIngredientsShouldBeFound("cautions.equals=" + DEFAULT_CAUTIONS);

        // Get all the ingredientsList where cautions equals to UPDATED_CAUTIONS
        defaultIngredientsShouldNotBeFound("cautions.equals=" + UPDATED_CAUTIONS);
    }

    @Test
    @Transactional
    void getAllIngredientsByCautionsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where cautions not equals to DEFAULT_CAUTIONS
        defaultIngredientsShouldNotBeFound("cautions.notEquals=" + DEFAULT_CAUTIONS);

        // Get all the ingredientsList where cautions not equals to UPDATED_CAUTIONS
        defaultIngredientsShouldBeFound("cautions.notEquals=" + UPDATED_CAUTIONS);
    }

    @Test
    @Transactional
    void getAllIngredientsByCautionsIsInShouldWork() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where cautions in DEFAULT_CAUTIONS or UPDATED_CAUTIONS
        defaultIngredientsShouldBeFound("cautions.in=" + DEFAULT_CAUTIONS + "," + UPDATED_CAUTIONS);

        // Get all the ingredientsList where cautions equals to UPDATED_CAUTIONS
        defaultIngredientsShouldNotBeFound("cautions.in=" + UPDATED_CAUTIONS);
    }

    @Test
    @Transactional
    void getAllIngredientsByCautionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where cautions is not null
        defaultIngredientsShouldBeFound("cautions.specified=true");

        // Get all the ingredientsList where cautions is null
        defaultIngredientsShouldNotBeFound("cautions.specified=false");
    }

    @Test
    @Transactional
    void getAllIngredientsByCautionsContainsSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where cautions contains DEFAULT_CAUTIONS
        defaultIngredientsShouldBeFound("cautions.contains=" + DEFAULT_CAUTIONS);

        // Get all the ingredientsList where cautions contains UPDATED_CAUTIONS
        defaultIngredientsShouldNotBeFound("cautions.contains=" + UPDATED_CAUTIONS);
    }

    @Test
    @Transactional
    void getAllIngredientsByCautionsNotContainsSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where cautions does not contain DEFAULT_CAUTIONS
        defaultIngredientsShouldNotBeFound("cautions.doesNotContain=" + DEFAULT_CAUTIONS);

        // Get all the ingredientsList where cautions does not contain UPDATED_CAUTIONS
        defaultIngredientsShouldBeFound("cautions.doesNotContain=" + UPDATED_CAUTIONS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIngredientsShouldBeFound(String filter) throws Exception {
        restIngredientsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingredients.getId().intValue())))
            .andExpect(jsonPath("$.[*].iname").value(hasItem(DEFAULT_INAME)))
            .andExpect(jsonPath("$.[*].symptoms").value(hasItem(DEFAULT_SYMPTOMS)))
            .andExpect(jsonPath("$.[*].sideeffects").value(hasItem(DEFAULT_SIDEEFFECTS)))
            .andExpect(jsonPath("$.[*].cautions").value(hasItem(DEFAULT_CAUTIONS)));

        // Check, that the count call also returns 1
        restIngredientsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIngredientsShouldNotBeFound(String filter) throws Exception {
        restIngredientsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIngredientsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIngredients() throws Exception {
        // Get the ingredients
        restIngredientsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIngredients() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();

        // Update the ingredients
        Ingredients updatedIngredients = ingredientsRepository.findById(ingredients.getId()).get();
        // Disconnect from session so that the updates on updatedIngredients are not directly saved in db
        em.detach(updatedIngredients);
        updatedIngredients.iname(UPDATED_INAME).symptoms(UPDATED_SYMPTOMS).sideeffects(UPDATED_SIDEEFFECTS).cautions(UPDATED_CAUTIONS);
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(updatedIngredients);

        restIngredientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ingredientsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingredientsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
        Ingredients testIngredients = ingredientsList.get(ingredientsList.size() - 1);
        assertThat(testIngredients.getIname()).isEqualTo(UPDATED_INAME);
        assertThat(testIngredients.getSymptoms()).isEqualTo(UPDATED_SYMPTOMS);
        assertThat(testIngredients.getSideeffects()).isEqualTo(UPDATED_SIDEEFFECTS);
        assertThat(testIngredients.getCautions()).isEqualTo(UPDATED_CAUTIONS);

        // Validate the Ingredients in Elasticsearch
        verify(mockIngredientsSearchRepository).save(testIngredients);
    }

    @Test
    @Transactional
    void putNonExistingIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();
        ingredients.setId(count.incrementAndGet());

        // Create the Ingredients
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(ingredients);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngredientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ingredientsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingredientsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Ingredients in Elasticsearch
        verify(mockIngredientsSearchRepository, times(0)).save(ingredients);
    }

    @Test
    @Transactional
    void putWithIdMismatchIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();
        ingredients.setId(count.incrementAndGet());

        // Create the Ingredients
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(ingredients);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingredientsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Ingredients in Elasticsearch
        verify(mockIngredientsSearchRepository, times(0)).save(ingredients);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();
        ingredients.setId(count.incrementAndGet());

        // Create the Ingredients
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(ingredients);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Ingredients in Elasticsearch
        verify(mockIngredientsSearchRepository, times(0)).save(ingredients);
    }

    @Test
    @Transactional
    void partialUpdateIngredientsWithPatch() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();

        // Update the ingredients using partial update
        Ingredients partialUpdatedIngredients = new Ingredients();
        partialUpdatedIngredients.setId(ingredients.getId());

        partialUpdatedIngredients.symptoms(UPDATED_SYMPTOMS);

        restIngredientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngredients.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIngredients))
            )
            .andExpect(status().isOk());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
        Ingredients testIngredients = ingredientsList.get(ingredientsList.size() - 1);
        assertThat(testIngredients.getIname()).isEqualTo(DEFAULT_INAME);
        assertThat(testIngredients.getSymptoms()).isEqualTo(UPDATED_SYMPTOMS);
        assertThat(testIngredients.getSideeffects()).isEqualTo(DEFAULT_SIDEEFFECTS);
        assertThat(testIngredients.getCautions()).isEqualTo(DEFAULT_CAUTIONS);
    }

    @Test
    @Transactional
    void fullUpdateIngredientsWithPatch() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();

        // Update the ingredients using partial update
        Ingredients partialUpdatedIngredients = new Ingredients();
        partialUpdatedIngredients.setId(ingredients.getId());

        partialUpdatedIngredients
            .iname(UPDATED_INAME)
            .symptoms(UPDATED_SYMPTOMS)
            .sideeffects(UPDATED_SIDEEFFECTS)
            .cautions(UPDATED_CAUTIONS);

        restIngredientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngredients.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIngredients))
            )
            .andExpect(status().isOk());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
        Ingredients testIngredients = ingredientsList.get(ingredientsList.size() - 1);
        assertThat(testIngredients.getIname()).isEqualTo(UPDATED_INAME);
        assertThat(testIngredients.getSymptoms()).isEqualTo(UPDATED_SYMPTOMS);
        assertThat(testIngredients.getSideeffects()).isEqualTo(UPDATED_SIDEEFFECTS);
        assertThat(testIngredients.getCautions()).isEqualTo(UPDATED_CAUTIONS);
    }

    @Test
    @Transactional
    void patchNonExistingIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();
        ingredients.setId(count.incrementAndGet());

        // Create the Ingredients
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(ingredients);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngredientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ingredientsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ingredientsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Ingredients in Elasticsearch
        verify(mockIngredientsSearchRepository, times(0)).save(ingredients);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();
        ingredients.setId(count.incrementAndGet());

        // Create the Ingredients
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(ingredients);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ingredientsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Ingredients in Elasticsearch
        verify(mockIngredientsSearchRepository, times(0)).save(ingredients);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();
        ingredients.setId(count.incrementAndGet());

        // Create the Ingredients
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(ingredients);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ingredientsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Ingredients in Elasticsearch
        verify(mockIngredientsSearchRepository, times(0)).save(ingredients);
    }

    @Test
    @Transactional
    void deleteIngredients() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        int databaseSizeBeforeDelete = ingredientsRepository.findAll().size();

        // Delete the ingredients
        restIngredientsMockMvc
            .perform(delete(ENTITY_API_URL_ID, ingredients.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Ingredients in Elasticsearch
        verify(mockIngredientsSearchRepository, times(1)).deleteById(ingredients.getId());
    }

    @Test
    @Transactional
    void searchIngredients() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);
        when(mockIngredientsSearchRepository.search(queryStringQuery("id:" + ingredients.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(ingredients), PageRequest.of(0, 1), 1));

        // Search the ingredients
        restIngredientsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + ingredients.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingredients.getId().intValue())))
            .andExpect(jsonPath("$.[*].iname").value(hasItem(DEFAULT_INAME)))
            .andExpect(jsonPath("$.[*].symptoms").value(hasItem(DEFAULT_SYMPTOMS)))
            .andExpect(jsonPath("$.[*].sideeffects").value(hasItem(DEFAULT_SIDEEFFECTS)))
            .andExpect(jsonPath("$.[*].cautions").value(hasItem(DEFAULT_CAUTIONS)));
    }
}
