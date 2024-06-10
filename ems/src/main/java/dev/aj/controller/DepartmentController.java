package dev.aj.controller;

import dev.aj.dto.DepartmentDto;
import dev.aj.service.DepartmentService;
import java.net.URI;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(path = "/departments", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentDto> createDepartment(@RequestBody @Validated DepartmentDto departmentDto) {
        var savedDepartmentDto = departmentService.createDepartment(departmentDto);

        URI uriLocation = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                     .path("departments")
                                                     .path("/{id}")
                                                     .buildAndExpand(Map.of("id", savedDepartmentDto.getId()))
                                                     .toUri();

        return ResponseEntity.created(uriLocation)
                             .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                             .body(savedDepartmentDto);
    }

}
