package mx.com.ghara.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import mx.com.ghara.service.FileSystemStorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/assets")
public class AssetController extends BaseController {

    private final FileSystemStorageService fileSystemStorageService;

    @Autowired
    public AssetController(FileSystemStorageService fileSystemStorageService) {
        this.fileSystemStorageService = fileSystemStorageService;
    }

    @GetMapping("{filename:.+}")
    ResponseEntity<Resource> getArchivo(@PathVariable String filename) throws IOException {
        Resource resource = fileSystemStorageService.loadAsResource(filename);
        String contentType = Files.probeContentType(resource.getFile().toPath());

        log.info("El tipo de contenido para {} es {}", filename, contentType);

        return ResponseEntity
                .ok()
                .header("Content-Type", contentType)
                .body(resource);
    }

    @PostMapping("/upload")
    Map<String, String> subirArchivo(@RequestParam MultipartFile file) {
        String rutaArchivo = fileSystemStorageService.store(file);

        Map<String, String> result = new HashMap();
        result.put("ruta", rutaArchivo);
        result.put("url", buildUrlString(rutaArchivo));

        return result;
    }

}
