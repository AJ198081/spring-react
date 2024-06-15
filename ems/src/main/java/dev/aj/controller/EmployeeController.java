package dev.aj.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aj.dto.EmployeeDto;
import dev.aj.service.EmployeeService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody @Validated EmployeeDto employeeDto) {
        log.debug("Create request for: %s".formatted(objectMapper.writeValueAsString(employeeDto)));
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(employeeDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PreAuthorize("hasAnyRole('admin', 'superuser')")
    @PostAuthorize("hasRole('superuser')")
    @GetMapping("/all")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @SneakyThrows
    @GetMapping("/email/{email}")
    public ResponseEntity<Boolean> isEmailExist(@PathVariable String email) {
        ResponseEntity<Boolean> responseEntity = ResponseEntity.ok(employeeService.isEmailExist(email));
        log.debug(objectMapper.writeValueAsString("Email %s, response %s".formatted(email, responseEntity)));
        return responseEntity;

    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @RequestBody @Validated EmployeeDto employeeDto) {
        Optional<EmployeeDto> optionalUpdatedEmployee = employeeService.updateEmployee(id, employeeDto);
        if (optionalUpdatedEmployee.isPresent()) {
            return ResponseEntity.ok().body(optionalUpdatedEmployee.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EmployeeDto> deleteEmployee(@PathVariable Long id) {
        if (employeeService.deleteEmployee(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
