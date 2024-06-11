package dev.aj.service;

import dev.aj.dto.DepartmentDto;
import java.util.List;

public interface DepartmentService {
    DepartmentDto createDepartment(DepartmentDto departmentDto);
    DepartmentDto getDepartment(Long id);
    List<DepartmentDto> getAllDepartments();
    DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto);
    DepartmentDto replaceDepartment(Long id, DepartmentDto departmentDto);
    void deleteDepartment(Long id);
}
