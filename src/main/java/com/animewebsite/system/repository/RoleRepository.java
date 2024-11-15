package com.animewebsite.system.repository;

import com.animewebsite.system.model.Role;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    @EntityGraph(attributePaths = {"users"})
    Optional<Role> findByName(String name);
}
