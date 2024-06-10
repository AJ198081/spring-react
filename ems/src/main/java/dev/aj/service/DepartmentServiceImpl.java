package dev.aj.service;

import dev.aj.dto.DepartmentDto;
import dev.aj.exception.ResourceNotFoundException;
import dev.aj.mapper.DepartmentMapper;
import dev.aj.repository.DepartmentRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    private static Supplier<ResourceNotFoundException> supplyResourceNotFoundException(Long id) {
        return () -> new ResourceNotFoundException("Unable to find department with ID: %s".formatted(id));
    }

    @Override
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        return departmentMapper.toDto(
                departmentRepository.save(
                        departmentMapper.toEntity(departmentDto)));
    }

    @Override
    public DepartmentDto getDepartment(Long id) {
        return departmentRepository.findById(id)
                                   .map(departmentMapper::toDto)
                                   .orElseThrow(supplyResourceNotFoundException(id));
    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll()
                                   .stream()
                                   .map(departmentMapper::toDto)
                                   .toList();
    }

    @Override
    public DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto) {

        var department = departmentRepository.findById(id)
                                             .orElseThrow(supplyResourceNotFoundException(id));

        Optional.ofNullable(departmentDto.getDepartmentName())
                .filter(not(String::isBlank))
                .ifPresent(department::setDepartmentName);

        Optional.ofNullable(departmentDto.getDepartmentDescription())
                .filter(not(String::isBlank))
                .ifPresent(department::setDepartmentDescription);

        return Optional.of(departmentRepository.saveAndFlush(department))
                       .map(departmentMapper::toDto)
                       .orElseThrow();
    }
}
