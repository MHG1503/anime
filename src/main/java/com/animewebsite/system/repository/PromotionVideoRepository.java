package com.animewebsite.system.repository;

import com.animewebsite.system.model.PromotionVideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionVideoRepository extends JpaRepository<PromotionVideo,Long> {

    Page<PromotionVideo> findAllByAnimeId(Long id,Pageable pageable);
}
