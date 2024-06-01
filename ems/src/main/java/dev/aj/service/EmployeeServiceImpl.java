package dev.aj.service;

import dev.aj.dto.EmployeeDto;
import dev.aj.entity.Employee;
import dev.aj.exception.FailedToPersistEntityException;
import dev.aj.exception.ResourceNotFoundException;
import dev.aj.mapper.EmployeeMapper;
import dev.aj.repository.EmployeeRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {

        return Optional.ofNullable(employeeDto)
                       .map(employeeMapper::employeeDtoToEmployee)
                       .map(this::saveEmployeeOrThrow)
                       .map(employeeMapper::employeeToEmployeeDto)
                       .orElseThrow();

    }

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                                 .map(employeeMapper::employeeToEmployeeDto)
                                 .orElseThrow(() -> new ResourceNotFoundException("Employee with id %d not found".formatted(id)));
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll()
                                 .stream()
                                 .map(employeeMapper::employeeToEmployeeDto)
                                 .toList();
    }

    @Override
    public Optional<EmployeeDto> updateEmployee(Long employeeId, EmployeeDto updatedEmployee) {

        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            String fullName = updatedEmployee.getFullName();
            if (fullName != null && !fullName.isEmpty()) {
                String[] firstLastName = fullName.split(" ");
                if (firstLastName.length == 2) {
                    employee.setFirstName(firstLastName[0]);
                    employee.setLastName(firstLastName[1]);
                }
            }
            if (updatedEmployee.getEmail() != null) {
                employee.setEmail(updatedEmployee.getEmail());
            }
            Employee savedEmployee = employeeRepository.save(employee);
            return Optional.ofNullable(employeeMapper.employeeToEmployeeDto(savedEmployee));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteEmployee(Long employeeId) {
        if (employeeRepository.findById(employeeId).isPresent()) {
            employeeRepository.deleteById(employeeId);
            return true;
        }
        return false;
    }

    private Employee saveEmployeeOrThrow(Employee employee) {
        try {
            return employeeRepository.save(employee);
        } catch (ConstraintViolationException e) {
            throw new FailedToPersistEntityException(
                    "Failed to save entity, Constraint %s violated, %s".formatted(e.getConstraintName(), e.getMessage()));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}

