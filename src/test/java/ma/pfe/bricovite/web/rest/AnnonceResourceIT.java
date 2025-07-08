package ma.pfe.bricovite.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import ma.pfe.bricovite.IntegrationTest;
import ma.pfe.bricovite.domain.Activite;
import ma.pfe.bricovite.domain.Annonce;
import ma.pfe.bricovite.domain.User;
import ma.pfe.bricovite.repository.AnnonceRepository;
import ma.pfe.bricovite.service.AnnonceService;
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
 * Integration tests for the {@link AnnonceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AnnonceResourceIT {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final Instant DEFAULT_DATE_ANNONCE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ANNONCE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final String ENTITY_API_URL = "/api/annonces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AnnonceRepository annonceRepository;

    @Mock
    private AnnonceRepository annonceRepositoryMock;

    @Mock
    private AnnonceService annonceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnnonceMockMvc;

    private Annonce annonce;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Annonce createEntity(EntityManager em) {
        Annonce annonce = new Annonce()
            .titre(DEFAULT_TITRE)
            .description(DEFAULT_DESCRIPTION)
            .adresse(DEFAULT_ADRESSE)
            .status(DEFAULT_STATUS)
            .dateAnnonce(DEFAULT_DATE_ANNONCE)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        annonce.setUser(user);
        // Add required entity
        Activite activite;
        if (TestUtil.findAll(em, Activite.class).isEmpty()) {
            activite = ActiviteResourceIT.createEntity(em);
            em.persist(activite);
            em.flush();
        } else {
            activite = TestUtil.findAll(em, Activite.class).get(0);
        }
        annonce.setActivite(activite);
        return annonce;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Annonce createUpdatedEntity(EntityManager em) {
        Annonce annonce = new Annonce()
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .adresse(UPDATED_ADRESSE)
            .status(UPDATED_STATUS)
            .dateAnnonce(UPDATED_DATE_ANNONCE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        annonce.setUser(user);
        // Add required entity
        Activite activite;
        if (TestUtil.findAll(em, Activite.class).isEmpty()) {
            activite = ActiviteResourceIT.createUpdatedEntity(em);
            em.persist(activite);
            em.flush();
        } else {
            activite = TestUtil.findAll(em, Activite.class).get(0);
        }
        annonce.setActivite(activite);
        return annonce;
    }

    @BeforeEach
    public void initTest() {
        annonce = createEntity(em);
    }

    @Test
    @Transactional
    void createAnnonce() throws Exception {
        int databaseSizeBeforeCreate = annonceRepository.findAll().size();
        // Create the Annonce
        restAnnonceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annonce)))
            .andExpect(status().isCreated());

        // Validate the Annonce in the database
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeCreate + 1);
        Annonce testAnnonce = annonceList.get(annonceList.size() - 1);
        assertThat(testAnnonce.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testAnnonce.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAnnonce.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testAnnonce.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAnnonce.getDateAnnonce()).isEqualTo(DEFAULT_DATE_ANNONCE);
        assertThat(testAnnonce.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testAnnonce.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void createAnnonceWithExistingId() throws Exception {
        // Create the Annonce with an existing ID
        annonce.setId(1L);

        int databaseSizeBeforeCreate = annonceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnnonceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annonce)))
            .andExpect(status().isBadRequest());

        // Validate the Annonce in the database
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitreIsRequired() throws Exception {
        int databaseSizeBeforeTest = annonceRepository.findAll().size();
        // set the field null
        annonce.setTitre(null);

        // Create the Annonce, which fails.

        restAnnonceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annonce)))
            .andExpect(status().isBadRequest());

        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = annonceRepository.findAll().size();
        // set the field null
        annonce.setDescription(null);

        // Create the Annonce, which fails.

        restAnnonceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annonce)))
            .andExpect(status().isBadRequest());

        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdresseIsRequired() throws Exception {
        int databaseSizeBeforeTest = annonceRepository.findAll().size();
        // set the field null
        annonce.setAdresse(null);

        // Create the Annonce, which fails.

        restAnnonceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annonce)))
            .andExpect(status().isBadRequest());

        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAnnonces() throws Exception {
        // Initialize the database
        annonceRepository.saveAndFlush(annonce);

        // Get all the annonceList
        restAnnonceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annonce.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].dateAnnonce").value(hasItem(DEFAULT_DATE_ANNONCE.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAnnoncesWithEagerRelationshipsIsEnabled() throws Exception {
        when(annonceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAnnonceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(annonceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAnnoncesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(annonceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAnnonceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(annonceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAnnonce() throws Exception {
        // Initialize the database
        annonceRepository.saveAndFlush(annonce);

        // Get the annonce
        restAnnonceMockMvc
            .perform(get(ENTITY_API_URL_ID, annonce.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(annonce.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.dateAnnonce").value(DEFAULT_DATE_ANNONCE.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingAnnonce() throws Exception {
        // Get the annonce
        restAnnonceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAnnonce() throws Exception {
        // Initialize the database
        annonceRepository.saveAndFlush(annonce);

        int databaseSizeBeforeUpdate = annonceRepository.findAll().size();

        // Update the annonce
        Annonce updatedAnnonce = annonceRepository.findById(annonce.getId()).get();
        // Disconnect from session so that the updates on updatedAnnonce are not directly saved in db
        em.detach(updatedAnnonce);
        updatedAnnonce
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .adresse(UPDATED_ADRESSE)
            .status(UPDATED_STATUS)
            .dateAnnonce(UPDATED_DATE_ANNONCE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);

        restAnnonceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAnnonce.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAnnonce))
            )
            .andExpect(status().isOk());

        // Validate the Annonce in the database
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeUpdate);
        Annonce testAnnonce = annonceList.get(annonceList.size() - 1);
        assertThat(testAnnonce.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testAnnonce.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAnnonce.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testAnnonce.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAnnonce.getDateAnnonce()).isEqualTo(UPDATED_DATE_ANNONCE);
        assertThat(testAnnonce.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testAnnonce.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void putNonExistingAnnonce() throws Exception {
        int databaseSizeBeforeUpdate = annonceRepository.findAll().size();
        annonce.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnonceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, annonce.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(annonce))
            )
            .andExpect(status().isBadRequest());

        // Validate the Annonce in the database
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnnonce() throws Exception {
        int databaseSizeBeforeUpdate = annonceRepository.findAll().size();
        annonce.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnonceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(annonce))
            )
            .andExpect(status().isBadRequest());

        // Validate the Annonce in the database
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnnonce() throws Exception {
        int databaseSizeBeforeUpdate = annonceRepository.findAll().size();
        annonce.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnonceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annonce)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Annonce in the database
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAnnonceWithPatch() throws Exception {
        // Initialize the database
        annonceRepository.saveAndFlush(annonce);

        int databaseSizeBeforeUpdate = annonceRepository.findAll().size();

        // Update the annonce using partial update
        Annonce partialUpdatedAnnonce = new Annonce();
        partialUpdatedAnnonce.setId(annonce.getId());

        partialUpdatedAnnonce
            .titre(UPDATED_TITRE)
            .adresse(UPDATED_ADRESSE)
            .status(UPDATED_STATUS)
            .dateAnnonce(UPDATED_DATE_ANNONCE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);

        restAnnonceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnonce.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnnonce))
            )
            .andExpect(status().isOk());

        // Validate the Annonce in the database
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeUpdate);
        Annonce testAnnonce = annonceList.get(annonceList.size() - 1);
        assertThat(testAnnonce.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testAnnonce.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAnnonce.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testAnnonce.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAnnonce.getDateAnnonce()).isEqualTo(UPDATED_DATE_ANNONCE);
        assertThat(testAnnonce.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testAnnonce.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void fullUpdateAnnonceWithPatch() throws Exception {
        // Initialize the database
        annonceRepository.saveAndFlush(annonce);

        int databaseSizeBeforeUpdate = annonceRepository.findAll().size();

        // Update the annonce using partial update
        Annonce partialUpdatedAnnonce = new Annonce();
        partialUpdatedAnnonce.setId(annonce.getId());

        partialUpdatedAnnonce
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .adresse(UPDATED_ADRESSE)
            .status(UPDATED_STATUS)
            .dateAnnonce(UPDATED_DATE_ANNONCE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);

        restAnnonceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnonce.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnnonce))
            )
            .andExpect(status().isOk());

        // Validate the Annonce in the database
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeUpdate);
        Annonce testAnnonce = annonceList.get(annonceList.size() - 1);
        assertThat(testAnnonce.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testAnnonce.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAnnonce.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testAnnonce.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAnnonce.getDateAnnonce()).isEqualTo(UPDATED_DATE_ANNONCE);
        assertThat(testAnnonce.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testAnnonce.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void patchNonExistingAnnonce() throws Exception {
        int databaseSizeBeforeUpdate = annonceRepository.findAll().size();
        annonce.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnonceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, annonce.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(annonce))
            )
            .andExpect(status().isBadRequest());

        // Validate the Annonce in the database
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnnonce() throws Exception {
        int databaseSizeBeforeUpdate = annonceRepository.findAll().size();
        annonce.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnonceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(annonce))
            )
            .andExpect(status().isBadRequest());

        // Validate the Annonce in the database
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnnonce() throws Exception {
        int databaseSizeBeforeUpdate = annonceRepository.findAll().size();
        annonce.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnonceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(annonce)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Annonce in the database
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAnnonce() throws Exception {
        // Initialize the database
        annonceRepository.saveAndFlush(annonce);

        int databaseSizeBeforeDelete = annonceRepository.findAll().size();

        // Delete the annonce
        restAnnonceMockMvc
            .perform(delete(ENTITY_API_URL_ID, annonce.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
