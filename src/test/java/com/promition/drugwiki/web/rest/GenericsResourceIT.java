package com.promition.drugwiki.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.promition.drugwiki.IntegrationTest;
import com.promition.drugwiki.domain.Brand;
import com.promition.drugwiki.domain.Generics;
import com.promition.drugwiki.domain.Ingredients;
import com.promition.drugwiki.domain.enumeration.DosageUnit;
import com.promition.drugwiki.repository.GenericsRepository;
import com.promition.drugwiki.service.criteria.GenericsCriteria;
import com.promition.drugwiki.service.dto.GenericsDTO;
import com.promition.drugwiki.service.mapper.GenericsMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GenericsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GenericsResourceIT {

    private static final String DEFAULT_GNAME = "AAAAAAAAAA";
    private static final String UPDATED_GNAME = "BBBBBBBBBB";

    private static final Double DEFAULT_DOSAGE = 1D;
    private static final Double UPDATED_DOSAGE = 2D;
    private static final Double SMALLER_DOSAGE = 1D - 1D;

    private static final DosageUnit DEFAULT_DOSAGEUNIT = DosageUnit.Microgram;
    private static final DosageUnit UPDATED_DOSAGEUNIT = DosageUnit.Miligram;

    private static final String ENTITY_API_URL = "/api/generics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GenericsRepository genericsRepository;

    @Autowired
    private GenericsMapper genericsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGenericsMockMvc;

    private Generics generics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Generics createEntity(EntityManager em) {
        Generics generics = new Generics().gname(DEFAULT_GNAME).dosage(DEFAULT_DOSAGE).dosageunit(DEFAULT_DOSAGEUNIT);
        // Add required entity
        Ingredients ingredients;
        if (TestUtil.findAll(em, Ingredients.class).isEmpty()) {
            ingredients = IngredientsResourceIT.createEntity(em);
            em.persist(ingredients);
            em.flush();
        } else {
            ingredients = TestUtil.findAll(em, Ingredients.class).get(0);
        }
        generics.setIngredientsused(ingredients);
        return generics;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Generics createUpdatedEntity(EntityManager em) {
        Generics generics = new Generics().gname(UPDATED_GNAME).dosage(UPDATED_DOSAGE).dosageunit(UPDATED_DOSAGEUNIT);
        // Add required entity
        Ingredients ingredients;
        if (TestUtil.findAll(em, Ingredients.class).isEmpty()) {
            ingredients = IngredientsResourceIT.createUpdatedEntity(em);
            em.persist(ingredients);
            em.flush();
        } else {
            ingredients = TestUtil.findAll(em, Ingredients.class).get(0);
        }
        generics.setIngredientsused(ingredients);
        return generics;
    }

    @BeforeEach
    public void initTest() {
        generics = createEntity(em);
    }

    @Test
    @Transactional
    void createGenerics() throws Exception {
        int databaseSizeBeforeCreate = genericsRepository.findAll().size();
        // Create the Generics
        GenericsDTO genericsDTO = genericsMapper.toDto(generics);
        restGenericsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genericsDTO)))
            .andExpect(status().isCreated());

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll();
        assertThat(genericsList).hasSize(databaseSizeBeforeCreate + 1);
        Generics testGenerics = genericsList.get(genericsList.size() - 1);
        assertThat(testGenerics.getGname()).isEqualTo(DEFAULT_GNAME);
        assertThat(testGenerics.getDosage()).isEqualTo(DEFAULT_DOSAGE);
        assertThat(testGenerics.getDosageunit()).isEqualTo(DEFAULT_DOSAGEUNIT);
    }

    @Test
    @Transactional
    void createGenericsWithExistingId() throws Exception {
        // Create the Generics with an existing ID
        generics.setId(1L);
        GenericsDTO genericsDTO = genericsMapper.toDto(generics);

        int databaseSizeBeforeCreate = genericsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGenericsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genericsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll();
        assertThat(genericsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = genericsRepository.findAll().size();
        // set the field null
        generics.setGname(null);

        // Create the Generics, which fails.
        GenericsDTO genericsDTO = genericsMapper.toDto(generics);

        restGenericsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genericsDTO)))
            .andExpect(status().isBadRequest());

        List<Generics> genericsList = genericsRepository.findAll();
        assertThat(genericsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDosageIsRequired() throws Exception {
        int databaseSizeBeforeTest = genericsRepository.findAll().size();
        // set the field null
        generics.setDosage(null);

        // Create the Generics, which fails.
        GenericsDTO genericsDTO = genericsMapper.toDto(generics);

        restGenericsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genericsDTO)))
            .andExpect(status().isBadRequest());

        List<Generics> genericsList = genericsRepository.findAll();
        assertThat(genericsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDosageunitIsRequired() throws Exception {
        int databaseSizeBeforeTest = genericsRepository.findAll().size();
        // set the field null
        generics.setDosageunit(null);

        // Create the Generics, which fails.
        GenericsDTO genericsDTO = genericsMapper.toDto(generics);

        restGenericsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genericsDTO)))
            .andExpect(status().isBadRequest());

        List<Generics> genericsList = genericsRepository.findAll();
        assertThat(genericsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGenerics() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        // Get all the genericsList
        restGenericsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(generics.getId().intValue())))
            .andExpect(jsonPath("$.[*].gname").value(hasItem(DEFAULT_GNAME)))
            .andExpect(jsonPath("$.[*].dosage").value(hasItem(DEFAULT_DOSAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].dosageunit").value(hasItem(DEFAULT_DOSAGEUNIT.toString())));
    }

    @Test
    @Transactional
    void getGenerics() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        // Get the generics
        restGenericsMockMvc
            .perform(get(ENTITY_API_URL_ID, generics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(generics.getId().intValue()))
            .andExpect(jsonPath("$.gname").value(DEFAULT_GNAME))
            .andExpect(jsonPath("$.dosage").value(DEFAULT_DOSAGE.doubleValue()))
            .andExpect(jsonPath("$.dosageunit").value(DEFAULT_DOSAGEUNIT.toString()));
    }

    @Test
    @Transactional
    void getGenericsByIdFiltering() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        Long id = generics.getId();

        defaultGenericsShouldBeFound("id.equals=" + id);
        defaultGenericsShouldNotBeFound("id.notEquals=" + id);

        defaultGenericsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGenericsShouldNotBeFound("id.greaterThan=" + id);

        defaultGenericsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGenericsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGenericsByGnameIsEqualToSomething() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        // Get all the genericsList where gname equals to DEFAULT_GNAME
        defaultGenericsShouldBeFound("gname.equals=" + DEFAULT_GNAME);

        // Get all the genericsList where gname equals to UPDATED_GNAME
        defaultGenericsShouldNotBeFound("gname.equals=" + UPDATED_GNAME);
    }

    @Test
    @Transactional
    void getAllGenericsByGnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        // Get all the genericsList where gname not equals to DEFAULT_GNAME
        defaultGenericsShouldNotBeFound("gname.notEquals=" + DEFAULT_GNAME);

        // Get all the genericsList where gname not equals to UPDATED_GNAME
        defaultGenericsShouldBeFound("gname.notEquals=" + UPDATED_GNAME);
    }

    @Test
    @Transactional
    void getAllGenericsByGnameIsInShouldWork() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        // Get all the genericsList where gname in DEFAULT_GNAME or UPDATED_GNAME
        defaultGenericsShouldBeFound("gname.in=" + DEFAULT_GNAME + "," + UPDATED_GNAME);

        // Get all the genericsList where gname equals to UPDATED_GNAME
        defaultGenericsShouldNotBeFound("gname.in=" + UPDATED_GNAME);
    }

    @Test
    @Transactional
    void getAllGenericsByGnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        // Get all the genericsList where gname is not null
        defaultGenericsShouldBeFound("gname.specified=true");

        // Get all the genericsList where gname is null
        defaultGenericsShouldNotBeFound("gname.specified=false");
    }

    @Test
    @Transactional
    void getAllGenericsByGnameContainsSomething() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        // Get all the genericsList where gname contains DEFAULT_GNAME
        defaultGenericsShouldBeFound("gname.contains=" + DEFAULT_GNAME);

        // Get all the genericsList where gname contains UPDATED_GNAME
        defaultGenericsShouldNotBeFound("gname.contains=" + UPDATED_GNAME);
    }

    @Test
    @Transactional
    void getAllGenericsByGnameNotContainsSomething() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        // Get all the genericsList where gname does not contain DEFAULT_GNAME
        defaultGenericsShouldNotBeFound("gname.doesNotContain=" + DEFAULT_GNAME);

        // Get all the genericsList where gname does not contain UPDATED_GNAME
        defaultGenericsShouldBeFound("gname.doesNotContain=" + UPDATED_GNAME);
    }

    @Test
    @Transactional
    void getAllGenericsByDosageIsEqualToSomething() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        // Get all the genericsList where dosage equals to DEFAULT_DOSAGE
        defaultGenericsShouldBeFound("dosage.equals=" + DEFAULT_DOSAGE);

        // Get all the genericsList where dosage equals to UPDATED_DOSAGE
        defaultGenericsShouldNotBeFound("dosage.equals=" + UPDATED_DOSAGE);
    }

    @Test
    @Transactional
    void getAllGenericsByDosageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        // Get all the genericsList where dosage not equals to DEFAULT_DOSAGE
        defaultGenericsShouldNotBeFound("dosage.notEquals=" + DEFAULT_DOSAGE);

        // Get all the genericsList where dosage not equals to UPDATED_DOSAGE
        defaultGenericsShouldBeFound("dosage.notEquals=" + UPDATED_DOSAGE);
    }

    @Test
    @Transactional
    void getAllGenericsByDosageIsInShouldWork() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        // Get all the genericsList where dosage in DEFAULT_DOSAGE or UPDATED_DOSAGE
        defaultGenericsShouldBeFound("dosage.in=" + DEFAULT_DOSAGE + "," + UPDATED_DOSAGE);

        // Get all the genericsList where dosage equals to UPDATED_DOSAGE
        defaultGenericsShouldNotBeFound("dosage.in=" + UPDATED_DOSAGE);
    }

    @Test
    @Transactional
    void getAllGenericsByDosageIsNullOrNotNull() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        // Get all the genericsList where dosage is not null
        defaultGenericsShouldBeFound("dosage.specified=true");

        // Get all the genericsList where dosage is null
        defaultGenericsShouldNotBeFound("dosage.specified=false");
    }

    @Test
    @Transactional
    void getAllGenericsByDosageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        // Get all the genericsList where dosage is greater than or equal to DEFAULT_DOSAGE
        defaultGenericsShouldBeFound("dosage.greaterThanOrEqual=" + DEFAULT_DOSAGE);

        // Get all the genericsList where dosage is greater than or equal to UPDATED_DOSAGE
        defaultGenericsShouldNotBeFound("dosage.greaterThanOrEqual=" + UPDATED_DOSAGE);
    }

    @Test
    @Transactional
    void getAllGenericsByDosageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        // Get all the genericsList where dosage is less than or equal to DEFAULT_DOSAGE
        defaultGenericsShouldBeFound("dosage.lessThanOrEqual=" + DEFAULT_DOSAGE);

        // Get all the genericsList where dosage is less than or equal to SMALLER_DOSAGE
        defaultGenericsShouldNotBeFound("dosage.lessThanOrEqual=" + SMALLER_DOSAGE);
    }

    @Test
    @Transactional
    void getAllGenericsByDosageIsLessThanSomething() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        // Get all the genericsList where dosage is less than DEFAULT_DOSAGE
        defaultGenericsShouldNotBeFound("dosage.lessThan=" + DEFAULT_DOSAGE);

        // Get all the genericsList where dosage is less than UPDATED_DOSAGE
        defaultGenericsShouldBeFound("dosage.lessThan=" + UPDATED_DOSAGE);
    }

    @Test
    @Transactional
    void getAllGenericsByDosageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        // Get all the genericsList where dosage is greater than DEFAULT_DOSAGE
        defaultGenericsShouldNotBeFound("dosage.greaterThan=" + DEFAULT_DOSAGE);

        // Get all the genericsList where dosage is greater than SMALLER_DOSAGE
        defaultGenericsShouldBeFound("dosage.greaterThan=" + SMALLER_DOSAGE);
    }

    @Test
    @Transactional
    void getAllGenericsByDosageunitIsEqualToSomething() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        // Get all the genericsList where dosageunit equals to DEFAULT_DOSAGEUNIT
        defaultGenericsShouldBeFound("dosageunit.equals=" + DEFAULT_DOSAGEUNIT);

        // Get all the genericsList where dosageunit equals to UPDATED_DOSAGEUNIT
        defaultGenericsShouldNotBeFound("dosageunit.equals=" + UPDATED_DOSAGEUNIT);
    }

    @Test
    @Transactional
    void getAllGenericsByDosageunitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        // Get all the genericsList where dosageunit not equals to DEFAULT_DOSAGEUNIT
        defaultGenericsShouldNotBeFound("dosageunit.notEquals=" + DEFAULT_DOSAGEUNIT);

        // Get all the genericsList where dosageunit not equals to UPDATED_DOSAGEUNIT
        defaultGenericsShouldBeFound("dosageunit.notEquals=" + UPDATED_DOSAGEUNIT);
    }

    @Test
    @Transactional
    void getAllGenericsByDosageunitIsInShouldWork() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        // Get all the genericsList where dosageunit in DEFAULT_DOSAGEUNIT or UPDATED_DOSAGEUNIT
        defaultGenericsShouldBeFound("dosageunit.in=" + DEFAULT_DOSAGEUNIT + "," + UPDATED_DOSAGEUNIT);

        // Get all the genericsList where dosageunit equals to UPDATED_DOSAGEUNIT
        defaultGenericsShouldNotBeFound("dosageunit.in=" + UPDATED_DOSAGEUNIT);
    }

    @Test
    @Transactional
    void getAllGenericsByDosageunitIsNullOrNotNull() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        // Get all the genericsList where dosageunit is not null
        defaultGenericsShouldBeFound("dosageunit.specified=true");

        // Get all the genericsList where dosageunit is null
        defaultGenericsShouldNotBeFound("dosageunit.specified=false");
    }

    @Test
    @Transactional
    void getAllGenericsByIngredientsusedIsEqualToSomething() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);
        Ingredients ingredientsused = IngredientsResourceIT.createEntity(em);
        em.persist(ingredientsused);
        em.flush();
        generics.setIngredientsused(ingredientsused);
        genericsRepository.saveAndFlush(generics);
        Long ingredientsusedId = ingredientsused.getId();

        // Get all the genericsList where ingredientsused equals to ingredientsusedId
        defaultGenericsShouldBeFound("ingredientsusedId.equals=" + ingredientsusedId);

        // Get all the genericsList where ingredientsused equals to (ingredientsusedId + 1)
        defaultGenericsShouldNotBeFound("ingredientsusedId.equals=" + (ingredientsusedId + 1));
    }

    @Test
    @Transactional
    void getAllGenericsByIdIsEqualToSomething() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);
        Brand id = BrandResourceIT.createEntity(em);
        em.persist(id);
        em.flush();
        generics.addId(id);
        genericsRepository.saveAndFlush(generics);
        Long idId = id.getId();

        // Get all the genericsList where id equals to idId
        defaultGenericsShouldBeFound("idId.equals=" + idId);

        // Get all the genericsList where id equals to (idId + 1)
        defaultGenericsShouldNotBeFound("idId.equals=" + (idId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGenericsShouldBeFound(String filter) throws Exception {
        restGenericsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(generics.getId().intValue())))
            .andExpect(jsonPath("$.[*].gname").value(hasItem(DEFAULT_GNAME)))
            .andExpect(jsonPath("$.[*].dosage").value(hasItem(DEFAULT_DOSAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].dosageunit").value(hasItem(DEFAULT_DOSAGEUNIT.toString())));

        // Check, that the count call also returns 1
        restGenericsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGenericsShouldNotBeFound(String filter) throws Exception {
        restGenericsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGenericsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGenerics() throws Exception {
        // Get the generics
        restGenericsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGenerics() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        int databaseSizeBeforeUpdate = genericsRepository.findAll().size();

        // Update the generics
        Generics updatedGenerics = genericsRepository.findById(generics.getId()).get();
        // Disconnect from session so that the updates on updatedGenerics are not directly saved in db
        em.detach(updatedGenerics);
        updatedGenerics.gname(UPDATED_GNAME).dosage(UPDATED_DOSAGE).dosageunit(UPDATED_DOSAGEUNIT);
        GenericsDTO genericsDTO = genericsMapper.toDto(updatedGenerics);

        restGenericsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, genericsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genericsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll();
        assertThat(genericsList).hasSize(databaseSizeBeforeUpdate);
        Generics testGenerics = genericsList.get(genericsList.size() - 1);
        assertThat(testGenerics.getGname()).isEqualTo(UPDATED_GNAME);
        assertThat(testGenerics.getDosage()).isEqualTo(UPDATED_DOSAGE);
        assertThat(testGenerics.getDosageunit()).isEqualTo(UPDATED_DOSAGEUNIT);
    }

    @Test
    @Transactional
    void putNonExistingGenerics() throws Exception {
        int databaseSizeBeforeUpdate = genericsRepository.findAll().size();
        generics.setId(count.incrementAndGet());

        // Create the Generics
        GenericsDTO genericsDTO = genericsMapper.toDto(generics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenericsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, genericsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genericsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll();
        assertThat(genericsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGenerics() throws Exception {
        int databaseSizeBeforeUpdate = genericsRepository.findAll().size();
        generics.setId(count.incrementAndGet());

        // Create the Generics
        GenericsDTO genericsDTO = genericsMapper.toDto(generics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenericsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genericsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll();
        assertThat(genericsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGenerics() throws Exception {
        int databaseSizeBeforeUpdate = genericsRepository.findAll().size();
        generics.setId(count.incrementAndGet());

        // Create the Generics
        GenericsDTO genericsDTO = genericsMapper.toDto(generics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenericsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genericsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll();
        assertThat(genericsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGenericsWithPatch() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        int databaseSizeBeforeUpdate = genericsRepository.findAll().size();

        // Update the generics using partial update
        Generics partialUpdatedGenerics = new Generics();
        partialUpdatedGenerics.setId(generics.getId());

        partialUpdatedGenerics.gname(UPDATED_GNAME).dosage(UPDATED_DOSAGE).dosageunit(UPDATED_DOSAGEUNIT);

        restGenericsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenerics.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenerics))
            )
            .andExpect(status().isOk());

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll();
        assertThat(genericsList).hasSize(databaseSizeBeforeUpdate);
        Generics testGenerics = genericsList.get(genericsList.size() - 1);
        assertThat(testGenerics.getGname()).isEqualTo(UPDATED_GNAME);
        assertThat(testGenerics.getDosage()).isEqualTo(UPDATED_DOSAGE);
        assertThat(testGenerics.getDosageunit()).isEqualTo(UPDATED_DOSAGEUNIT);
    }

    @Test
    @Transactional
    void fullUpdateGenericsWithPatch() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        int databaseSizeBeforeUpdate = genericsRepository.findAll().size();

        // Update the generics using partial update
        Generics partialUpdatedGenerics = new Generics();
        partialUpdatedGenerics.setId(generics.getId());

        partialUpdatedGenerics.gname(UPDATED_GNAME).dosage(UPDATED_DOSAGE).dosageunit(UPDATED_DOSAGEUNIT);

        restGenericsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenerics.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenerics))
            )
            .andExpect(status().isOk());

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll();
        assertThat(genericsList).hasSize(databaseSizeBeforeUpdate);
        Generics testGenerics = genericsList.get(genericsList.size() - 1);
        assertThat(testGenerics.getGname()).isEqualTo(UPDATED_GNAME);
        assertThat(testGenerics.getDosage()).isEqualTo(UPDATED_DOSAGE);
        assertThat(testGenerics.getDosageunit()).isEqualTo(UPDATED_DOSAGEUNIT);
    }

    @Test
    @Transactional
    void patchNonExistingGenerics() throws Exception {
        int databaseSizeBeforeUpdate = genericsRepository.findAll().size();
        generics.setId(count.incrementAndGet());

        // Create the Generics
        GenericsDTO genericsDTO = genericsMapper.toDto(generics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenericsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, genericsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genericsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll();
        assertThat(genericsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGenerics() throws Exception {
        int databaseSizeBeforeUpdate = genericsRepository.findAll().size();
        generics.setId(count.incrementAndGet());

        // Create the Generics
        GenericsDTO genericsDTO = genericsMapper.toDto(generics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenericsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genericsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll();
        assertThat(genericsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGenerics() throws Exception {
        int databaseSizeBeforeUpdate = genericsRepository.findAll().size();
        generics.setId(count.incrementAndGet());

        // Create the Generics
        GenericsDTO genericsDTO = genericsMapper.toDto(generics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenericsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(genericsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Generics in the database
        List<Generics> genericsList = genericsRepository.findAll();
        assertThat(genericsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGenerics() throws Exception {
        // Initialize the database
        genericsRepository.saveAndFlush(generics);

        int databaseSizeBeforeDelete = genericsRepository.findAll().size();

        // Delete the generics
        restGenericsMockMvc
            .perform(delete(ENTITY_API_URL_ID, generics.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Generics> genericsList = genericsRepository.findAll();
        assertThat(genericsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
