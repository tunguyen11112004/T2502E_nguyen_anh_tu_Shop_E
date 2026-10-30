package org.fp.shop_e.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class OpenApiController {

    @GetMapping(value = "/api/v1/openapi.yaml", produces = "text/yaml;charset=UTF-8")
    public ResponseEntity<String> getOpenApiYaml() throws IOException {
        ClassPathResource resource = new ClassPathResource("openapi/shop-api.yaml");
        String yaml = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/yaml;charset=UTF-8"))
                .body(yaml);
    }
}

