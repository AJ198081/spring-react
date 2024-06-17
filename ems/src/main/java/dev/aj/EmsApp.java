package dev.aj;

import dev.aj.config.security.SecurityConfig;
import dev.aj.config.security.entity.Role;
import dev.aj.config.security.entity.SecurityUser;
import dev.aj.config.security.repository.RoleRepository;
import dev.aj.config.security.repository.SecurityUserRepository;
import dev.aj.entity.Department;
import dev.aj.entity.Employee;
import dev.aj.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class })
@RequiredArgsConstructor
public class EmsApp {

    private final SecurityUserRepository securityUserRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(EmsApp.class, args);
    }

    @Bean(name = "encoder")
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    public void createUsersInDB() {
        if (securityUserRepository.count() == 0) {
            persistLoginUsersAndRoles();
        }
        if (employeeRepository.count() == 0) {
            persistEmployeesAndDepartments();
        }
    }

    private void persistEmployeesAndDepartments() {
        Employee employeePW = Employee.builder()
                                      .firstName("P")
                                      .lastName("W")
                                      .email("pw@gmail.com")
                                      .department(Department.builder()
                                                            .departmentName("Engineering")
                                                            .departmentDescription("Engineering and Technical Services")
                                                            .build())
                                      .build();

        Employee employeeBW = Employee.builder()
                                      .firstName("B")
                                      .lastName("W")
                                      .email("bw@gmail.com")
                                      .department(Department.builder()
                                                            .departmentName("Business")
                                                            .departmentDescription("Business and Admin Services")
                                                            .build())
                                      .build();

        employeeRepository.saveAllAndFlush(List.of(employeePW, employeeBW));
    }

    private void persistLoginUsersAndRoles() {
        List<SecurityConfig.SecurityUserRecord> securityUserRecords = (List<SecurityConfig.SecurityUserRecord>) applicationContext.getBean("securityUserRecords");
        PasswordEncoder encoder = applicationContext.getBean("encoder", PasswordEncoder.class);

        Set<SecurityUser> securityUsers = getMappedSecurityUser(securityUserRecords, encoder);

        securityUserRepository.saveAllAndFlush(securityUsers);
    }

    public Set<SecurityUser> getMappedSecurityUser(List<SecurityConfig.SecurityUserRecord> securityUserRecords, PasswordEncoder encoder) {
        return securityUserRecords.stream()
                                  .map(securityUserRecord -> {

                                      SecurityUser securityUser = SecurityUser.builder()
                                                                              .name(securityUserRecord.username())
                                                                              .username(securityUserRecord.username())
                                                                              .password(encoder.encode(securityUserRecord.password()))
                                                                              .email(securityUserRecord.email())
                                                                              .roles(new HashSet<>())
                                                                              .build();

                                      Role role = roleRepository.findByName(securityUserRecord.role())
                                                                .orElseGet(() -> Role.builder()
                                                                                     .name(securityUserRecord.role())
                                                                                     .securityUsers(new HashSet<>())
                                                                                     .build());
                                      role.addSecurityUsers(securityUser);

                                      return securityUser;
                                  }).collect(Collectors.toSet());
    }
}