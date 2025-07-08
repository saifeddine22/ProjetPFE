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
import ma.pfe.bricovite.domain.Commentaire;
import ma.pfe.bricovite.domain.User;
import ma.pfe.bricovite.repository.CommentaireRepository;
import ma.pfe.bricovite.service.CommentaireService;
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
 * Integration tests for the {@link CommentaireResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CommentaireResourceIT {

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_COMMENTAIRE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_COMMENTAIRE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/commentaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommentaireRepository commentaireRepository;

    @Mock
    private CommentaireRepository commentaireRepositoryMock;

    @Mock
    private CommentaireService commentaireServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommentaireMockMvc;

    private Commentaire commentaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commentaire createEntity(EntityManager em) {
        Commentaire commentaire = new Commentaire().details(DEFAULT_DETAILS).dateCommentaire(DEFAULT_DATE_COMMENTAIRE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        commentaire.setUser(user);
        return commentaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commentaire createUpdatedEntity(EntityManager em) {
        Commentaire commentaire = new Commentaire().details(UPDATED_DETAILS).dateCommentaire(UPDATED_DATE_COMMENTAIRE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        commentaire.setUser(user);
        return commentaire;
    }

    @BeforeEach
    public void initTest() {
        commentaire = createEntity(em);
    }

    @Test
    @Transactional
    void createCommentaire() throws Exception {
        int databaseSizeBeforeCreate = commentaireRepository.findAll().size();
        // Create the Commentaire
        restCommentaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentaire)))
            .andExpect(status().isCreated());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeCreate + 1);
        Commentaire testCommentaire = commentaireList.get(commentaireList.size() - 1);
        assertThat(testCommentaire.getDetails()).isEqualTo(DEFAULT_DETAILS);
        assertThat(testCommentaire.getDateCommentaire()).isEqualTo(DEFAULT_DATE_COMMENTAIRE);
    }

    @Test
    @Transactional
    void createCommentaireWithExistingId() throws Exception {
        // Create the Commentaire with an existing ID
        commentaire.setId(1L);

        int databaseSizeBeforeCreate = commentaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentaire)))
            .andExpect(status().isBadRequest());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCommentaires() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList
        restCommentaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].dateCommentaire").value(hasItem(DEFAULT_DATE_COMMENTAIRE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommentairesWithEagerRelationshipsIsEnabled() throws Exception {
        when(commentaireServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCommentaireMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(commentaireServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommentairesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(commentaireServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCommentaireMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(commentaireServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCommentaire() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get the commentaire
        restCommentaireMockMvc
            .perform(get(ENTITY_API_URL_ID, commentaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commentaire.getId().intValue()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS))
            .andExpect(jsonPath("$.dateCommentaire").value(DEFAULT_DATE_COMMENTAIRE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCommentaire() throws Exception {
        // Get the commentaire
        restCommentaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommentaire() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();

        // Update the commentaire
        Commentaire updatedCommentaire = commentaireRepository.findById(commentaire.getId()).get();
        // Disconnect from session so that the updates on updatedCommentaire are not directly saved in db
        em.detach(updatedCommentaire);
        updatedCommentaire.details(UPDATED_DETAILS).dateCommentaire(UPDATED_DATE_COMMENTAIRE);

        restCommentaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommentaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCommentaire))
            )
            .andExpect(status().isOk());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeUpdate);
        Commentaire testCommentaire = commentaireList.get(commentaireList.size() - 1);
        assertThat(testCommentaire.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testCommentaire.getDateCommentaire()).isEqualTo(UPDATED_DATE_COMMENTAIRE);
    }

    @Test
    @Transactional
    void putNonExistingCommentaire() throws Exception {
        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();
        commentaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommentaire() throws Exception {
        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();
        commentaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommentaire() throws Exception {
        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();
        commentaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentaireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentaire)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommentaireWithPatch() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();

        // Update the commentaire using partial update
        Commentaire partialUpdatedCommentaire = new Commentaire();
        partialUpdatedCommentaire.setId(commentaire.getId());

        partialUpdatedCommentaire.dateCommentaire(UPDATED_DATE_COMMENTAIRE);

        restCommentaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommentaire))
            )
            .andExpect(status().isOk());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeUpdate);
        Commentaire testCommentaire = commentaireList.get(commentaireList.size() - 1);
        assertThat(testCommentaire.getDetails()).isEqualTo(DEFAULT_DETAILS);
        assertThat(testCommentaire.getDateCommentaire()).isEqualTo(UPDATED_DATE_COMMENTAIRE);
    }

    @Test
    @Transactional
    void fullUpdateCommentaireWithPatch() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();

        // Update the commentaire using partial update
        Commentaire partialUpdatedCommentaire = new Commentaire();
        partialUpdatedCommentaire.setId(commentaire.getId());

        partialUpdatedCommentaire.details(UPDATED_DETAILS).dateCommentaire(UPDATED_DATE_COMMENTAIRE);

        restCommentaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommentaire))
            )
            .andExpect(status().isOk());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeUpdate);
        Commentaire testCommentaire = commentaireList.get(commentaireList.size() - 1);
        assertThat(testCommentaire.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testCommentaire.getDateCommentaire()).isEqualTo(UPDATED_DATE_COMMENTAIRE);
    }

    @Test
    @Transactional
    void patchNonExistingCommentaire() throws Exception {
        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();
        commentaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commentaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommentaire() throws Exception {
        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();
        commentaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommentaire() throws Exception {
        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();
        commentaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentaireMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(commentaire))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commentaire in the database
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommentaire() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        int databaseSizeBeforeDelete = commentaireRepository.findAll().size();

        // Delete the commentaire
        restCommentaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, commentaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Commentaire> commentaireList = commentaireRepository.findAll();
        assertThat(commentaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
