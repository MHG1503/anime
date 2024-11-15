package com.animewebsite.system.repository;

import com.animewebsite.system.model.AlternativeTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlternativeTitleRepository extends JpaRepository<AlternativeTitle,Long> {
    Optional<AlternativeTitle> findByAlternativeName(String alternativeName);
}
