package dev.aj.config.entity.repository;

import dev.aj.config.entity.SecurityUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityUserRepository extends JpaRepository<SecurityUser, Long> {
    Optional<SecurityUser> findByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<SecurityUser> findByUsernameOrEmail(String username, String email);
}
