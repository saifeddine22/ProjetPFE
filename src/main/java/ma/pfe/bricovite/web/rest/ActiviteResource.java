package ma.pfe.bricovite.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ma.pfe.bricovite.domain.Activite;
import ma.pfe.bricovite.repository.ActiviteRepository;
import ma.pfe.bricovite.service.ActiviteService;
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
 * REST controller for managing {@link ma.pfe.bricovite.domain.Activite}.
 */
@RestController
@RequestMapping("/api")
public class ActiviteResource {

    private final Logger log = LoggerFactory.getLogger(ActiviteResource.class);

    private static final String ENTITY_NAME = "activite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActiviteService activiteService;

    private final ActiviteRepository activiteRepository;

    public ActiviteResource(ActiviteService activiteService, ActiviteRepository activiteRepository) {
        this.activiteService = activiteService;
        this.activiteRepository = activiteRepository;
    }

    /**
     * {@code POST  /activites} : Create a new activite.
     *
     * @param activite the activite to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activite, or with status {@code 400 (Bad Request)} if the activite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/activites")
    public ResponseEntity<Activite> createActivite(@Valid @RequestBody Activite activite) throws URISyntaxException {
        log.debug("REST request to save Activite : {}", activite);
        if (activite.getId() != null) {
            throw new BadRequestAlertException("A new activite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Activite result = activiteService.save(activite);
        return ResponseEntity
            .created(new URI("/api/activites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /activites/:id} : Updates an existing activite.
     *
     * @param id the id of the activite to save.
     * @param activite the activite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activite,
     * or with status {@code 400 (Bad Request)} if the activite is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/activites/{id}")
    public ResponseEntity<Activite> updateActivite(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Activite activite
    ) throws URISyntaxException {
        log.debug("REST request to update Activite : {}, {}", id, activite);
        if (activite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Activite result = activiteService.update(activite);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activite.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /activites/:id} : Partial updates given fields of an existing activite, field will ignore if it is null
     *
     * @param id the id of the activite to save.
     * @param activite the activite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activite,
     * or with status {@code 400 (Bad Request)} if the activite is not valid,
     * or with status {@code 404 (Not Found)} if the activite is not found,
     * or with status {@code 500 (Internal Server Error)} if the activite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/activites/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Activite> partialUpdateActivite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Activite activite
    ) throws URISyntaxException {
        log.debug("REST request to partial update Activite partially : {}, {}", id, activite);
        if (activite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Activite> result = activiteService.partialUpdate(activite);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activite.getId().toString())
        );
    }

    /**
     * {@code GET  /activites} : get all the activites.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activites in body.
     */
    @GetMapping("/activites")
    public ResponseEntity<List<Activite>> getAllActivites(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Activites");
        Page<Activite> page;
        if (eagerload) {
            page = activiteService.findAllWithEagerRelationships(pageable);
        } else {
            page = activiteService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /activites/:id} : get the "id" activite.
     *
     * @param id the id of the activite to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activite, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/activites/{id}")
    public ResponseEntity<Activite> getActivite(@PathVariable Long id) {
        log.debug("REST request to get Activite : {}", id);
        Optional<Activite> activite = activiteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(activite);
    }

    /**
     * {@code DELETE  /activites/:id} : delete the "id" activite.
     *
     * @param id the id of the activite to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/activites/{id}")
    public ResponseEntity<Void> deleteActivite(@PathVariable Long id) {
        log.debug("REST request to delete Activite : {}", id);
        activiteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
