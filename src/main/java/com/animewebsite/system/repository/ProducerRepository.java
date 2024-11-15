package com.animewebsite.system.repository;

import com.animewebsite.system.model.Producer;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProducerRepository extends JpaRepository<Producer,Long> {
    Optional<Producer> findByName(String name);

    Optional<Producer> findById(Long id, EntityGraph entityGraph);

    Page<Producer> findAll(Pageable pageable,EntityGraph entityGraph);
}
