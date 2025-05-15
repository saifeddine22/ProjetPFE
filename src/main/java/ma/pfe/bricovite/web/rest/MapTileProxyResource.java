package ma.pfe.bricovite.web.rest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class MapTileProxyResource {

    private final Logger log = LoggerFactory.getLogger(MapTileProxyResource.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping(value = "/map-tiles/osm/{z}/{x}/{y}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getOpenStreetMapTile(@PathVariable("z") int z, @PathVariable("x") int x, @PathVariable("y") int y) {
        try {
            String url = String.format("https://a.tile.openstreetmap.org/%d/%d/%d.png", z, x, y);
            ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);

            return ResponseEntity.status(response.getStatusCode()).contentType(MediaType.IMAGE_PNG).body(response.getBody());
        } catch (Exception e) {
            log.error("Error proxying OpenStreetMap tile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
