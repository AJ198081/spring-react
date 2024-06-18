package dev.aj.config.security.mapper;

import dev.aj.config.security.dto.UserRegisterationDetails;
import dev.aj.config.security.entity.Role;
import dev.aj.config.security.entity.SecurityUser;
import dev.aj.config.security.repository.RoleRepository;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserRegistrationMapper {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Mapping(target = "roles", expression = "java(securityUser.getRoles().stream().map(dev.aj.config.security.entity.Role::getName).toList())")
    public abstract UserRegisterationDetails toDto(final SecurityUser securityUser);

    @Mapping(target = "roles", source = "userRegisterationDetails", qualifiedByName = "stringToSet")
    @Mapping(target = "password", source = "userRegisterationDetails", qualifiedByName = "encodePassword")
    public abstract SecurityUser toEntity(final UserRegisterationDetails userRegisterationDetails);

    @Named("stringToSet")
    public Set<Role> stringToSet(final UserRegisterationDetails userRegisterationDetails) {
        return userRegisterationDetails.getRoles().stream()
                                       .map(this::roleBuilder)
                                       .collect(Collectors.toSet());
    }

    private Role roleBuilder(String role) {
        return roleRepository.findByName(role).map(existingRole -> {
                                 existingRole.setSecurityUsers(new HashSet<>());
                                 return existingRole;
                             })
                             .orElseGet(() -> Role.builder()
                                                  .name(role)
                                                  .securityUsers(new HashSet<>())
                                                  .build());
    }

    @Named("encodePassword")
    public String encodePassword(final UserRegisterationDetails userRegisterationDetails) {
        return encoder.encode(userRegisterationDetails.getPassword());
    }
}
