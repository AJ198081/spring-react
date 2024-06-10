package dev.aj.service;

import dev.aj.dto.DepartmentDto;
import dev.aj.entity.Department;
import dev.aj.exception.ResourceNotFoundException;
import dev.aj.mapper.DepartmentMapper;
import dev.aj.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        Department department = departmentMapper.toEntity(departmentDto);
        return departmentMapper.toDto(departmentRepository.save(department));
    }

    @Override
    public DepartmentDto getDepartment(Long id) {
        return departmentRepository.findById(id)
                                   .map(departmentMapper::toDto)
                                   .orElseThrow(() -> new ResourceNotFoundException("Unable to find department with ID: %s".formatted(id)));
    }
}
