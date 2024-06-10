package dev.aj.controller;

import dev.aj.dto.DepartmentDto;
import dev.aj.service.DepartmentService;
import java.net.URI;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
                                                     .pathSegment("departments", "byId", "{id}")
                                                     .buildAndExpand(Map.of("id", savedDepartmentDto.getId()))
                                                     .toUri();

        return ResponseEntity.created(uriLocation)
                             .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                             .body(savedDepartmentDto);
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<DepartmentDto> getDepartment(@PathVariable("id") Long id) {
        DepartmentDto departmentDto = departmentService.getDepartment(id);
        return ResponseEntity.ok()
                             .body(departmentDto);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        List<DepartmentDto> departmentDtos = departmentService.getAllDepartments();
        return ResponseEntity.ok(departmentDtos);
    }

    @PatchMapping(path = "/update/{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable Long id, @RequestBody @Validated DepartmentDto departmentDto) {
        DepartmentDto updatedDepartmentDto = departmentService.updateDepartment(id, departmentDto);
        return ResponseEntity.ok().body(updatedDepartmentDto);
    }
}
