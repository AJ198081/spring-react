package dev.aj.mapper;

import dev.aj.dto.EmployeeDto;
import dev.aj.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeMapper {

    @Mapping(target = "fullName", expression = "java(employee.getFirstName() + \" \" + employee.getLastName())")
    EmployeeDto employeeToEmployeeDto(Employee employee);

    @Mapping(target = "firstName", expression = "java(employeeDto.getFullName().split(\" \")[0])")
    @Mapping(target = "lastName", expression = "java(employeeDto.getFullName().split(\" \")[1])")
    Employee employeeDtoToEmployee(EmployeeDto employeeDto);

}
