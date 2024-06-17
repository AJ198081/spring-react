package dev.aj.config.security.mapper;

import dev.aj.config.security.dto.LoginUserDetails;
import dev.aj.config.security.entity.Role;
import dev.aj.config.security.entity.SecurityUser;
import dev.aj.config.security.entity.repository.RoleRepository;
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
public abstract class LoginUserMapper {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Mapping(target = "roles", expression = "java(securityUser.getRoles().stream().map(dev.aj.config.security.entity.Role::getName).toList())")
    public abstract LoginUserDetails toDto(final SecurityUser securityUser);

    @Mapping(target = "roles", source = "loginUserDetails", qualifiedByName = "stringToSet")
    @Mapping(target = "password", source = "loginUserDetails", qualifiedByName = "encodePassword")
    public abstract SecurityUser toEntity(final LoginUserDetails loginUserDetails);

    @Named("stringToSet")
    public Set<Role> stringToSet(final LoginUserDetails loginUserDetails) {
        return loginUserDetails.getRoles().stream()
                               .map(this::roleBuilder)
                               .collect(Collectors.toSet());
    }

    private Role roleBuilder(String role) {
       return roleRepository.findByName(role).orElseGet(() -> Role.builder()
                                                            .name(role)
                                                            .securityUsers(new HashSet<>())
                                                            .build());
    }

    @Named("encodePassword")
    public String encodePassword(final LoginUserDetails loginUserDetails) {
        return encoder.encode(loginUserDetails.getPassword());
    }
}
