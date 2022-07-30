package ma.pfe.bricovite.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ma.pfe.bricovite.domain.Annonce;
import ma.pfe.bricovite.repository.AnnonceRepository;
import ma.pfe.bricovite.service.ActiviteService;
import ma.pfe.bricovite.service.AnnonceService;
import ma.pfe.bricovite.service.UserService;
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
 * REST controller for managing {@link ma.pfe.bricovite.domain.Annonce}.
 */
@RestController
@RequestMapping("/api")
public class AnnonceResource {

    private final Logger log = LoggerFactory.getLogger(AnnonceResource.class);

    private static final String ENTITY_NAME = "annonce";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnnonceService annonceService;

    private final AnnonceRepository annonceRepository;

    private final UserService userService;

    public AnnonceResource(AnnonceService annonceService, AnnonceRepository annonceRepository, UserService userService) {
        this.annonceService = annonceService;
        this.annonceRepository = annonceRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /annonces} : Create a new annonce.
     *
     * @param annonce the annonce to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new annonce, or with status {@code 400 (Bad Request)} if the annonce has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/annonces")
    public ResponseEntity<Annonce> createAnnonce(@Valid @RequestBody Annonce annonce) throws URISyntaxException {
        log.debug("REST request to save Annonce : {}", annonce);
        if (annonce.getId() != null) {
            throw new BadRequestAlertException("A new annonce cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Annonce result = annonceService.save(annonce);
        return ResponseEntity
            .created(new URI("/api/annonces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /annonces/:id} : Updates an existing annonce.
     *
     * @param id the id of the annonce to save.
     * @param annonce the annonce to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annonce,
     * or with status {@code 400 (Bad Request)} if the annonce is not valid,
     * or with status {@code 500 (Internal Server Error)} if the annonce couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/annonces/{id}")
    public ResponseEntity<Annonce> updateAnnonce(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Annonce annonce
    ) throws URISyntaxException {
        log.debug("REST request to update Annonce : {}, {}", id, annonce);
        if (annonce.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, annonce.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!annonceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Annonce result = annonceService.update(annonce);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, annonce.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /annonces/:id} : Partial updates given fields of an existing annonce, field will ignore if it is null
     *
     * @param id the id of the annonce to save.
     * @param annonce the annonce to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annonce,
     * or with status {@code 400 (Bad Request)} if the annonce is not valid,
     * or with status {@code 404 (Not Found)} if the annonce is not found,
     * or with status {@code 500 (Internal Server Error)} if the annonce couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/annonces/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Annonce> partialUpdateAnnonce(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Annonce annonce
    ) throws URISyntaxException {
        log.debug("REST request to partial update Annonce partially : {}, {}", id, annonce);
        if (annonce.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, annonce.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!annonceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Annonce> result = annonceService.partialUpdate(annonce);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, annonce.getId().toString())
        );
    }

    /**
     * {@code GET  /annonces} : get all the annonces.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of annonces in body.
     */
    @GetMapping("/annonces")
    public ResponseEntity<List<Annonce>> getAllAnnonces(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Annonces");
        Page<Annonce> page;
        if (eagerload) {
            page = annonceService.findAllWithEagerRelationships(pageable);
        } else {
            page = annonceService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /annonces/:id} : get the "id" annonce.
     *
     * @param id the id of the annonce to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the annonce, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/annonces/{id}")
    public ResponseEntity<Annonce> getAnnonce(@PathVariable Long id) {
        log.debug("REST request to get Annonce : {}", id);
        Optional<Annonce> annonce = annonceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(annonce);
    }

    /**
     * {@code DELETE  /annonces/:id} : delete the "id" annonce.
     *
     * @param id the id of the annonce to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/annonces/{id}")
    public ResponseEntity<Void> deleteAnnonce(@PathVariable Long id) {
        log.debug("REST request to delete Annonce : {}", id);
        annonceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("annonces/connectedUser")
    public ResponseEntity<List<Annonce>> findByUserId(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Annonces");
        Page<Annonce> page = annonceService.findAllByUserId(pageable, userService.getConnectedUser().getId());

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("annonces/search")
    public ResponseEntity<List<Annonce>> search(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload,
        @RequestParam String provinceId,
        @RequestParam String activiteId,
        @RequestParam String categorieId
    ) {
        log.debug("REST request to get a page of Annonces");
        Page<Annonce> page = annonceService.search(provinceId, activiteId, categorieId, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
