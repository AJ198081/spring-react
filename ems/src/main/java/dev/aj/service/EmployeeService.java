package dev.aj.service;

import dev.aj.dto.EmployeeDto;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {

   EmployeeDto createEmployee(EmployeeDto employeeDto);

    EmployeeDto getEmployeeById(Long id);

    List<EmployeeDto> getAllEmployees();

    Optional<EmployeeDto> updateEmployee(Long employeeId, EmployeeDto updatedEmployee);

    boolean deleteEmployee(Long employeeId);

    boolean isEmailExist(String email);
}
