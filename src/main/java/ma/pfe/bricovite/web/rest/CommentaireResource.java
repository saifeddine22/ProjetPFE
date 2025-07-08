package ma.pfe.bricovite.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ma.pfe.bricovite.domain.Commentaire;
import ma.pfe.bricovite.repository.CommentaireRepository;
import ma.pfe.bricovite.service.CommentaireService;
import ma.pfe.bricovite.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ma.pfe.bricovite.domain.Commentaire}.
 */
@RestController
@RequestMapping("/api")
public class CommentaireResource {

    private final Logger log = LoggerFactory.getLogger(CommentaireResource.class);

    private static final String ENTITY_NAME = "commentaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommentaireService commentaireService;

    private final CommentaireRepository commentaireRepository;

    public CommentaireResource(CommentaireService commentaireService, CommentaireRepository commentaireRepository) {
        this.commentaireService = commentaireService;
        this.commentaireRepository = commentaireRepository;
    }

    /**
     * {@code POST  /commentaires} : Create a new commentaire.
     *
     * @param commentaire the commentaire to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commentaire, or with status {@code 400 (Bad Request)} if the commentaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/commentaires")
    public ResponseEntity<Commentaire> createCommentaire(@Valid @RequestBody Commentaire commentaire) throws URISyntaxException {
        log.debug("REST request to save Commentaire : {}", commentaire);
        if (commentaire.getId() != null) {
            throw new BadRequestAlertException("A new commentaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Commentaire result = commentaireService.save(commentaire);
        return ResponseEntity
            .created(new URI("/api/commentaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /commentaires/:id} : Updates an existing commentaire.
     *
     * @param id the id of the commentaire to save.
     * @param commentaire the commentaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentaire,
     * or with status {@code 400 (Bad Request)} if the commentaire is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commentaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/commentaires/{id}")
    public ResponseEntity<Commentaire> updateCommentaire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Commentaire commentaire
    ) throws URISyntaxException {
        log.debug("REST request to update Commentaire : {}, {}", id, commentaire);
        if (commentaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commentaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commentaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Commentaire result = commentaireService.update(commentaire);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commentaire.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /commentaires/:id} : Partial updates given fields of an existing commentaire, field will ignore if it is null
     *
     * @param id the id of the commentaire to save.
     * @param commentaire the commentaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentaire,
     * or with status {@code 400 (Bad Request)} if the commentaire is not valid,
     * or with status {@code 404 (Not Found)} if the commentaire is not found,
     * or with status {@code 500 (Internal Server Error)} if the commentaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/commentaires/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Commentaire> partialUpdateCommentaire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Commentaire commentaire
    ) throws URISyntaxException {
        log.debug("REST request to partial update Commentaire partially : {}, {}", id, commentaire);
        if (commentaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commentaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commentaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Commentaire> result = commentaireService.partialUpdate(commentaire);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commentaire.getId().toString())
        );
    }

    /**
     * {@code GET  /commentaires} : get all the commentaires.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commentaires in body.
     */
    @GetMapping("/commentaires")
    public ResponseEntity<List<Commentaire>> getAllCommentaires(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Commentaires");
        Page<Commentaire> page;
        if (eagerload) {
            page = commentaireService.findAllWithEagerRelationships(pageable);
        } else {
            page = commentaireService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /commentaires/:id} : get the "id" commentaire.
     *
     * @param id the id of the commentaire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commentaire, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/commentaires/{id}")
    public ResponseEntity<Commentaire> getCommentaire(@PathVariable Long id) {
        log.debug("REST request to get Commentaire : {}", id);
        Optional<Commentaire> commentaire = commentaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commentaire);
    }

    /**
     * {@code DELETE  /commentaires/:id} : delete the "id" commentaire.
     *
     * @param id the id of the commentaire to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/commentaires/{id}")
    public ResponseEntity<Void> deleteCommentaire(@PathVariable Long id) {
        log.debug("REST request to delete Commentaire : {}", id);
        commentaireService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/commentaires/annonce/{id}")
    public ResponseEntity<List<Commentaire>> findByAnnonceId(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @PathVariable Long id
    ) {
        log.debug("REST request to get a page of Commentaires");
        Page<Commentaire> page = commentaireService.findByAnnonceId(pageable, id);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
