package dev.aj.mapper;

import dev.aj.dto.EmployeeDto;
import dev.aj.entity.Department;
import dev.aj.entity.Employee;
import dev.aj.repository.DepartmentRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@Slf4j
public abstract class EmployeeMapper {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Mapping(target = "fullName", expression = "java(employee.getFirstName() + \" \" + employee.getLastName())")
    @Mapping(target = "departmentId", expression = "java(employee.getDepartment().getId())")
    public abstract EmployeeDto employeeToEmployeeDto(Employee employee);

    @Mapping(target = "firstName", expression = "java(employeeDto.getFullName().split(\" \")[0])")
    @Mapping(target = "lastName", expression = "java(employeeDto.getFullName().split(\" \")[1])")
    @Mapping(target = "department", qualifiedByName = "createDepartment", source = "employeeDto")
    public abstract Employee employeeDtoToEmployee(EmployeeDto employeeDto);

    @Named("createDepartment")
    public Department createDepartment(EmployeeDto employeeDto) {
        Optional<Department> optionalDepartment = departmentRepository.findById(employeeDto.getDepartmentId());
        log.info("Department %s".formatted(optionalDepartment));

        if (optionalDepartment.isPresent()) {
            return optionalDepartment.get();
        } else {
            Department placeholderDepartment = Department.builder()
                                                         .id(employeeDto.getId())
                                                         .departmentName(employeeDto.getFullName())
                                                         .departmentDescription(employeeDto.getEmail())
                                                         .build();
            return departmentRepository.saveAndFlush(placeholderDepartment);
        }
    }
}
