package com.animewebsite.system.service;

import com.animewebsite.system.model.Trailer;
import com.animewebsite.system.repository.TrailerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrailerService {
    private final TrailerRepository trailerRepository;

    public Trailer createTrailer(Trailer trailer){
        return trailerRepository.save(trailer);
    }
}
