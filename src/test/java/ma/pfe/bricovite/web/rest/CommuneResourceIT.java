package ma.pfe.bricovite.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import ma.pfe.bricovite.IntegrationTest;
import ma.pfe.bricovite.domain.Commune;
import ma.pfe.bricovite.domain.Province;
import ma.pfe.bricovite.repository.CommuneRepository;
import ma.pfe.bricovite.service.CommuneService;
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
 * Integration tests for the {@link CommuneResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CommuneResourceIT {

    private static final Double DEFAULT_CODE_REG = 1D;
    private static final Double UPDATED_CODE_REG = 2D;

    private static final Double DEFAULT_CODE_PROV = 1D;
    private static final Double UPDATED_CODE_PROV = 2D;

    private static final String DEFAULT_PROVINCE_FR = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCE_FR = "BBBBBBBBBB";

    private static final String DEFAULT_PROVINCE_AR = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCE_AR = "BBBBBBBBBB";

    private static final String DEFAULT_REGION_FR = "AAAAAAAAAA";
    private static final String UPDATED_REGION_FR = "BBBBBBBBBB";

    private static final String DEFAULT_REGION_AR = "AAAAAAAAAA";
    private static final String UPDATED_REGION_AR = "BBBBBBBBBB";

    private static final String DEFAULT_CERCLE_FR = "AAAAAAAAAA";
    private static final String UPDATED_CERCLE_FR = "BBBBBBBBBB";

    private static final Double DEFAULT_CODE_CERCLE = 1D;
    private static final Double UPDATED_CODE_CERCLE = 2D;

    private static final String DEFAULT_COM_FR = "AAAAAAAAAA";
    private static final String UPDATED_COM_FR = "BBBBBBBBBB";

    private static final Double DEFAULT_CODE_COM = 1D;
    private static final Double UPDATED_CODE_COM = 2D;

    private static final String DEFAULT_CENTRE_FR = "AAAAAAAAAA";
    private static final String UPDATED_CENTRE_FR = "BBBBBBBBBB";

    private static final Double DEFAULT_COD_AC = 1D;
    private static final Double UPDATED_COD_AC = 2D;

    private static final String DEFAULT_COM_AR = "AAAAAAAAAA";
    private static final String UPDATED_COM_AR = "BBBBBBBBBB";

    private static final Double DEFAULT_CC = 1D;
    private static final Double UPDATED_CC = 2D;

    private static final String DEFAULT_CENTRE_AR = "AAAAAAAAAA";
    private static final String UPDATED_CENTRE_AR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_AR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_AR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_GEOMETRY = "AAAAAAAAAA";
    private static final String UPDATED_GEOMETRY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/communes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommuneRepository communeRepository;

    @Mock
    private CommuneRepository communeRepositoryMock;

    @Mock
    private CommuneService communeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommuneMockMvc;

    private Commune commune;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commune createEntity(EntityManager em) {
        Commune commune = new Commune()
            .codeReg(DEFAULT_CODE_REG)
            .codeProv(DEFAULT_CODE_PROV)
            .provinceFr(DEFAULT_PROVINCE_FR)
            .provinceAr(DEFAULT_PROVINCE_AR)
            .regionFr(DEFAULT_REGION_FR)
            .regionAr(DEFAULT_REGION_AR)
            .cercleFr(DEFAULT_CERCLE_FR)
            .codeCercle(DEFAULT_CODE_CERCLE)
            .comFr(DEFAULT_COM_FR)
            .codeCom(DEFAULT_CODE_COM)
            .centreFr(DEFAULT_CENTRE_FR)
            .codAc(DEFAULT_COD_AC)
            .comAr(DEFAULT_COM_AR)
            .cc(DEFAULT_CC)
            .centreAr(DEFAULT_CENTRE_AR)
            .nomAr(DEFAULT_NOM_AR)
            .nomFr(DEFAULT_NOM_FR)
            .geometry(DEFAULT_GEOMETRY);
        // Add required entity
        Province province;
        if (TestUtil.findAll(em, Province.class).isEmpty()) {
            province = ProvinceResourceIT.createEntity(em);
            em.persist(province);
            em.flush();
        } else {
            province = TestUtil.findAll(em, Province.class).get(0);
        }
        commune.setProvince(province);
        return commune;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commune createUpdatedEntity(EntityManager em) {
        Commune commune = new Commune()
            .codeReg(UPDATED_CODE_REG)
            .codeProv(UPDATED_CODE_PROV)
            .provinceFr(UPDATED_PROVINCE_FR)
            .provinceAr(UPDATED_PROVINCE_AR)
            .regionFr(UPDATED_REGION_FR)
            .regionAr(UPDATED_REGION_AR)
            .cercleFr(UPDATED_CERCLE_FR)
            .codeCercle(UPDATED_CODE_CERCLE)
            .comFr(UPDATED_COM_FR)
            .codeCom(UPDATED_CODE_COM)
            .centreFr(UPDATED_CENTRE_FR)
            .codAc(UPDATED_COD_AC)
            .comAr(UPDATED_COM_AR)
            .cc(UPDATED_CC)
            .centreAr(UPDATED_CENTRE_AR)
            .nomAr(UPDATED_NOM_AR)
            .nomFr(UPDATED_NOM_FR)
            .geometry(UPDATED_GEOMETRY);
        // Add required entity
        Province province;
        if (TestUtil.findAll(em, Province.class).isEmpty()) {
            province = ProvinceResourceIT.createUpdatedEntity(em);
            em.persist(province);
            em.flush();
        } else {
            province = TestUtil.findAll(em, Province.class).get(0);
        }
        commune.setProvince(province);
        return commune;
    }

    @BeforeEach
    public void initTest() {
        commune = createEntity(em);
    }

    @Test
    @Transactional
    void createCommune() throws Exception {
        int databaseSizeBeforeCreate = communeRepository.findAll().size();
        // Create the Commune
        restCommuneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commune)))
            .andExpect(status().isCreated());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeCreate + 1);
        Commune testCommune = communeList.get(communeList.size() - 1);
        assertThat(testCommune.getCodeReg()).isEqualTo(DEFAULT_CODE_REG);
        assertThat(testCommune.getCodeProv()).isEqualTo(DEFAULT_CODE_PROV);
        assertThat(testCommune.getProvinceFr()).isEqualTo(DEFAULT_PROVINCE_FR);
        assertThat(testCommune.getProvinceAr()).isEqualTo(DEFAULT_PROVINCE_AR);
        assertThat(testCommune.getRegionFr()).isEqualTo(DEFAULT_REGION_FR);
        assertThat(testCommune.getRegionAr()).isEqualTo(DEFAULT_REGION_AR);
        assertThat(testCommune.getCercleFr()).isEqualTo(DEFAULT_CERCLE_FR);
        assertThat(testCommune.getCodeCercle()).isEqualTo(DEFAULT_CODE_CERCLE);
        assertThat(testCommune.getComFr()).isEqualTo(DEFAULT_COM_FR);
        assertThat(testCommune.getCodeCom()).isEqualTo(DEFAULT_CODE_COM);
        assertThat(testCommune.getCentreFr()).isEqualTo(DEFAULT_CENTRE_FR);
        assertThat(testCommune.getCodAc()).isEqualTo(DEFAULT_COD_AC);
        assertThat(testCommune.getComAr()).isEqualTo(DEFAULT_COM_AR);
        assertThat(testCommune.getCc()).isEqualTo(DEFAULT_CC);
        assertThat(testCommune.getCentreAr()).isEqualTo(DEFAULT_CENTRE_AR);
        assertThat(testCommune.getNomAr()).isEqualTo(DEFAULT_NOM_AR);
        assertThat(testCommune.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testCommune.getGeometry()).isEqualTo(DEFAULT_GEOMETRY);
    }

    @Test
    @Transactional
    void createCommuneWithExistingId() throws Exception {
        // Create the Commune with an existing ID
        commune.setId(1L);

        int databaseSizeBeforeCreate = communeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommuneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commune)))
            .andExpect(status().isBadRequest());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCommunes() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        // Get all the communeList
        restCommuneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commune.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeReg").value(hasItem(DEFAULT_CODE_REG.doubleValue())))
            .andExpect(jsonPath("$.[*].codeProv").value(hasItem(DEFAULT_CODE_PROV.doubleValue())))
            .andExpect(jsonPath("$.[*].provinceFr").value(hasItem(DEFAULT_PROVINCE_FR)))
            .andExpect(jsonPath("$.[*].provinceAr").value(hasItem(DEFAULT_PROVINCE_AR)))
            .andExpect(jsonPath("$.[*].regionFr").value(hasItem(DEFAULT_REGION_FR)))
            .andExpect(jsonPath("$.[*].regionAr").value(hasItem(DEFAULT_REGION_AR)))
            .andExpect(jsonPath("$.[*].cercleFr").value(hasItem(DEFAULT_CERCLE_FR)))
            .andExpect(jsonPath("$.[*].codeCercle").value(hasItem(DEFAULT_CODE_CERCLE.doubleValue())))
            .andExpect(jsonPath("$.[*].comFr").value(hasItem(DEFAULT_COM_FR)))
            .andExpect(jsonPath("$.[*].codeCom").value(hasItem(DEFAULT_CODE_COM.doubleValue())))
            .andExpect(jsonPath("$.[*].centreFr").value(hasItem(DEFAULT_CENTRE_FR)))
            .andExpect(jsonPath("$.[*].codAc").value(hasItem(DEFAULT_COD_AC.doubleValue())))
            .andExpect(jsonPath("$.[*].comAr").value(hasItem(DEFAULT_COM_AR)))
            .andExpect(jsonPath("$.[*].cc").value(hasItem(DEFAULT_CC.doubleValue())))
            .andExpect(jsonPath("$.[*].centreAr").value(hasItem(DEFAULT_CENTRE_AR)))
            .andExpect(jsonPath("$.[*].nomAr").value(hasItem(DEFAULT_NOM_AR)))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].geometry").value(hasItem(DEFAULT_GEOMETRY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommunesWithEagerRelationshipsIsEnabled() throws Exception {
        when(communeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCommuneMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(communeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommunesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(communeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCommuneMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(communeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCommune() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        // Get the commune
        restCommuneMockMvc
            .perform(get(ENTITY_API_URL_ID, commune.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commune.getId().intValue()))
            .andExpect(jsonPath("$.codeReg").value(DEFAULT_CODE_REG.doubleValue()))
            .andExpect(jsonPath("$.codeProv").value(DEFAULT_CODE_PROV.doubleValue()))
            .andExpect(jsonPath("$.provinceFr").value(DEFAULT_PROVINCE_FR))
            .andExpect(jsonPath("$.provinceAr").value(DEFAULT_PROVINCE_AR))
            .andExpect(jsonPath("$.regionFr").value(DEFAULT_REGION_FR))
            .andExpect(jsonPath("$.regionAr").value(DEFAULT_REGION_AR))
            .andExpect(jsonPath("$.cercleFr").value(DEFAULT_CERCLE_FR))
            .andExpect(jsonPath("$.codeCercle").value(DEFAULT_CODE_CERCLE.doubleValue()))
            .andExpect(jsonPath("$.comFr").value(DEFAULT_COM_FR))
            .andExpect(jsonPath("$.codeCom").value(DEFAULT_CODE_COM.doubleValue()))
            .andExpect(jsonPath("$.centreFr").value(DEFAULT_CENTRE_FR))
            .andExpect(jsonPath("$.codAc").value(DEFAULT_COD_AC.doubleValue()))
            .andExpect(jsonPath("$.comAr").value(DEFAULT_COM_AR))
            .andExpect(jsonPath("$.cc").value(DEFAULT_CC.doubleValue()))
            .andExpect(jsonPath("$.centreAr").value(DEFAULT_CENTRE_AR))
            .andExpect(jsonPath("$.nomAr").value(DEFAULT_NOM_AR))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.geometry").value(DEFAULT_GEOMETRY));
    }

    @Test
    @Transactional
    void getNonExistingCommune() throws Exception {
        // Get the commune
        restCommuneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommune() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        int databaseSizeBeforeUpdate = communeRepository.findAll().size();

        // Update the commune
        Commune updatedCommune = communeRepository.findById(commune.getId()).get();
        // Disconnect from session so that the updates on updatedCommune are not directly saved in db
        em.detach(updatedCommune);
        updatedCommune
            .codeReg(UPDATED_CODE_REG)
            .codeProv(UPDATED_CODE_PROV)
            .provinceFr(UPDATED_PROVINCE_FR)
            .provinceAr(UPDATED_PROVINCE_AR)
            .regionFr(UPDATED_REGION_FR)
            .regionAr(UPDATED_REGION_AR)
            .cercleFr(UPDATED_CERCLE_FR)
            .codeCercle(UPDATED_CODE_CERCLE)
            .comFr(UPDATED_COM_FR)
            .codeCom(UPDATED_CODE_COM)
            .centreFr(UPDATED_CENTRE_FR)
            .codAc(UPDATED_COD_AC)
            .comAr(UPDATED_COM_AR)
            .cc(UPDATED_CC)
            .centreAr(UPDATED_CENTRE_AR)
            .nomAr(UPDATED_NOM_AR)
            .nomFr(UPDATED_NOM_FR)
            .geometry(UPDATED_GEOMETRY);

        restCommuneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommune.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCommune))
            )
            .andExpect(status().isOk());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
        Commune testCommune = communeList.get(communeList.size() - 1);
        assertThat(testCommune.getCodeReg()).isEqualTo(UPDATED_CODE_REG);
        assertThat(testCommune.getCodeProv()).isEqualTo(UPDATED_CODE_PROV);
        assertThat(testCommune.getProvinceFr()).isEqualTo(UPDATED_PROVINCE_FR);
        assertThat(testCommune.getProvinceAr()).isEqualTo(UPDATED_PROVINCE_AR);
        assertThat(testCommune.getRegionFr()).isEqualTo(UPDATED_REGION_FR);
        assertThat(testCommune.getRegionAr()).isEqualTo(UPDATED_REGION_AR);
        assertThat(testCommune.getCercleFr()).isEqualTo(UPDATED_CERCLE_FR);
        assertThat(testCommune.getCodeCercle()).isEqualTo(UPDATED_CODE_CERCLE);
        assertThat(testCommune.getComFr()).isEqualTo(UPDATED_COM_FR);
        assertThat(testCommune.getCodeCom()).isEqualTo(UPDATED_CODE_COM);
        assertThat(testCommune.getCentreFr()).isEqualTo(UPDATED_CENTRE_FR);
        assertThat(testCommune.getCodAc()).isEqualTo(UPDATED_COD_AC);
        assertThat(testCommune.getComAr()).isEqualTo(UPDATED_COM_AR);
        assertThat(testCommune.getCc()).isEqualTo(UPDATED_CC);
        assertThat(testCommune.getCentreAr()).isEqualTo(UPDATED_CENTRE_AR);
        assertThat(testCommune.getNomAr()).isEqualTo(UPDATED_NOM_AR);
        assertThat(testCommune.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testCommune.getGeometry()).isEqualTo(UPDATED_GEOMETRY);
    }

    @Test
    @Transactional
    void putNonExistingCommune() throws Exception {
        int databaseSizeBeforeUpdate = communeRepository.findAll().size();
        commune.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommuneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commune.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commune))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommune() throws Exception {
        int databaseSizeBeforeUpdate = communeRepository.findAll().size();
        commune.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommuneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commune))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommune() throws Exception {
        int databaseSizeBeforeUpdate = communeRepository.findAll().size();
        commune.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommuneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commune)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommuneWithPatch() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        int databaseSizeBeforeUpdate = communeRepository.findAll().size();

        // Update the commune using partial update
        Commune partialUpdatedCommune = new Commune();
        partialUpdatedCommune.setId(commune.getId());

        partialUpdatedCommune
            .codeProv(UPDATED_CODE_PROV)
            .provinceFr(UPDATED_PROVINCE_FR)
            .provinceAr(UPDATED_PROVINCE_AR)
            .regionFr(UPDATED_REGION_FR)
            .regionAr(UPDATED_REGION_AR)
            .codeCercle(UPDATED_CODE_CERCLE)
            .comAr(UPDATED_COM_AR)
            .cc(UPDATED_CC)
            .centreAr(UPDATED_CENTRE_AR)
            .nomFr(UPDATED_NOM_FR)
            .geometry(UPDATED_GEOMETRY);

        restCommuneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommune.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommune))
            )
            .andExpect(status().isOk());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
        Commune testCommune = communeList.get(communeList.size() - 1);
        assertThat(testCommune.getCodeReg()).isEqualTo(DEFAULT_CODE_REG);
        assertThat(testCommune.getCodeProv()).isEqualTo(UPDATED_CODE_PROV);
        assertThat(testCommune.getProvinceFr()).isEqualTo(UPDATED_PROVINCE_FR);
        assertThat(testCommune.getProvinceAr()).isEqualTo(UPDATED_PROVINCE_AR);
        assertThat(testCommune.getRegionFr()).isEqualTo(UPDATED_REGION_FR);
        assertThat(testCommune.getRegionAr()).isEqualTo(UPDATED_REGION_AR);
        assertThat(testCommune.getCercleFr()).isEqualTo(DEFAULT_CERCLE_FR);
        assertThat(testCommune.getCodeCercle()).isEqualTo(UPDATED_CODE_CERCLE);
        assertThat(testCommune.getComFr()).isEqualTo(DEFAULT_COM_FR);
        assertThat(testCommune.getCodeCom()).isEqualTo(DEFAULT_CODE_COM);
        assertThat(testCommune.getCentreFr()).isEqualTo(DEFAULT_CENTRE_FR);
        assertThat(testCommune.getCodAc()).isEqualTo(DEFAULT_COD_AC);
        assertThat(testCommune.getComAr()).isEqualTo(UPDATED_COM_AR);
        assertThat(testCommune.getCc()).isEqualTo(UPDATED_CC);
        assertThat(testCommune.getCentreAr()).isEqualTo(UPDATED_CENTRE_AR);
        assertThat(testCommune.getNomAr()).isEqualTo(DEFAULT_NOM_AR);
        assertThat(testCommune.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testCommune.getGeometry()).isEqualTo(UPDATED_GEOMETRY);
    }

    @Test
    @Transactional
    void fullUpdateCommuneWithPatch() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        int databaseSizeBeforeUpdate = communeRepository.findAll().size();

        // Update the commune using partial update
        Commune partialUpdatedCommune = new Commune();
        partialUpdatedCommune.setId(commune.getId());

        partialUpdatedCommune
            .codeReg(UPDATED_CODE_REG)
            .codeProv(UPDATED_CODE_PROV)
            .provinceFr(UPDATED_PROVINCE_FR)
            .provinceAr(UPDATED_PROVINCE_AR)
            .regionFr(UPDATED_REGION_FR)
            .regionAr(UPDATED_REGION_AR)
            .cercleFr(UPDATED_CERCLE_FR)
            .codeCercle(UPDATED_CODE_CERCLE)
            .comFr(UPDATED_COM_FR)
            .codeCom(UPDATED_CODE_COM)
            .centreFr(UPDATED_CENTRE_FR)
            .codAc(UPDATED_COD_AC)
            .comAr(UPDATED_COM_AR)
            .cc(UPDATED_CC)
            .centreAr(UPDATED_CENTRE_AR)
            .nomAr(UPDATED_NOM_AR)
            .nomFr(UPDATED_NOM_FR)
            .geometry(UPDATED_GEOMETRY);

        restCommuneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommune.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommune))
            )
            .andExpect(status().isOk());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
        Commune testCommune = communeList.get(communeList.size() - 1);
        assertThat(testCommune.getCodeReg()).isEqualTo(UPDATED_CODE_REG);
        assertThat(testCommune.getCodeProv()).isEqualTo(UPDATED_CODE_PROV);
        assertThat(testCommune.getProvinceFr()).isEqualTo(UPDATED_PROVINCE_FR);
        assertThat(testCommune.getProvinceAr()).isEqualTo(UPDATED_PROVINCE_AR);
        assertThat(testCommune.getRegionFr()).isEqualTo(UPDATED_REGION_FR);
        assertThat(testCommune.getRegionAr()).isEqualTo(UPDATED_REGION_AR);
        assertThat(testCommune.getCercleFr()).isEqualTo(UPDATED_CERCLE_FR);
        assertThat(testCommune.getCodeCercle()).isEqualTo(UPDATED_CODE_CERCLE);
        assertThat(testCommune.getComFr()).isEqualTo(UPDATED_COM_FR);
        assertThat(testCommune.getCodeCom()).isEqualTo(UPDATED_CODE_COM);
        assertThat(testCommune.getCentreFr()).isEqualTo(UPDATED_CENTRE_FR);
        assertThat(testCommune.getCodAc()).isEqualTo(UPDATED_COD_AC);
        assertThat(testCommune.getComAr()).isEqualTo(UPDATED_COM_AR);
        assertThat(testCommune.getCc()).isEqualTo(UPDATED_CC);
        assertThat(testCommune.getCentreAr()).isEqualTo(UPDATED_CENTRE_AR);
        assertThat(testCommune.getNomAr()).isEqualTo(UPDATED_NOM_AR);
        assertThat(testCommune.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testCommune.getGeometry()).isEqualTo(UPDATED_GEOMETRY);
    }

    @Test
    @Transactional
    void patchNonExistingCommune() throws Exception {
        int databaseSizeBeforeUpdate = communeRepository.findAll().size();
        commune.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommuneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commune.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commune))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommune() throws Exception {
        int databaseSizeBeforeUpdate = communeRepository.findAll().size();
        commune.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommuneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commune))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommune() throws Exception {
        int databaseSizeBeforeUpdate = communeRepository.findAll().size();
        commune.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommuneMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(commune)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommune() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        int databaseSizeBeforeDelete = communeRepository.findAll().size();

        // Delete the commune
        restCommuneMockMvc
            .perform(delete(ENTITY_API_URL_ID, commune.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
