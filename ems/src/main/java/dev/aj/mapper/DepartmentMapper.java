package dev.aj.mapper;

import dev.aj.dto.DepartmentDto;
import dev.aj.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DepartmentMapper {

    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    DepartmentDto toDto(Department department);
    Department toEntity(DepartmentDto departmentDto);
}
