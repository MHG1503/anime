package com.animewebsite.system.service;

import com.animewebsite.system.convert.GenreMapper;
import com.animewebsite.system.dto.req.GenreRequest;
import com.animewebsite.system.dto.res.detail.GenreDtoDetail;
import com.animewebsite.system.dto.res.lazy.GenreDtoLazy;
import com.animewebsite.system.model.Genre;
import com.animewebsite.system.repository.GenreRepository;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public GenreDtoDetail getGenreById(Long id){
        return genreMapper.genreToGenreDtoDetail(genreRepository
                .findById(id, NamedEntityGraph.fetching("genre-anime"))
                .orElseThrow(()->new RuntimeException("Khong tim thay the loai (genre)")));
    }

    public Set<GenreDtoLazy> getAllGenres(){
        return new HashSet<>(genreRepository.findAll().stream().map(genreMapper::genreToGenreDtoLazy).sorted(Comparator.comparing(GenreDtoLazy::getId)).toList());
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public GenreDtoLazy createGenre(GenreRequest genreRequest){
        String genreNameRequest = genreRequest.getName();

        Optional<Genre> genreOptional = genreRepository.findByName(genreNameRequest);
        if(genreOptional.isPresent()){
            throw new RuntimeException(" The loai (genre) da ton tai!");
        }

        String genreDescriptionRequest = genreRequest.getDescription();

        Genre newGenre = Genre
                .builder()
                .name(genreNameRequest)
                .description(genreDescriptionRequest)
                .build();

        return genreMapper.genreToGenreDtoLazy(genreRepository.save(newGenre));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public GenreDtoLazy updateGenre(Long id,GenreRequest genreRequest){
        Genre existGenre = genreRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Khong tim tha the loai (gerne)"));

        String genreNameRequest = genreRequest.getName();
        String genreDescriptionRequest = genreRequest.getDescription();
        existGenre.setName(genreNameRequest);
        existGenre.setDescription(genreDescriptionRequest);

        return genreMapper.genreToGenreDtoLazy(genreRepository.save(existGenre));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Genre deleteGenre(Long id){
        Genre genre = genreRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Khong tim tha the loai (gerne)"));
        genreRepository.delete(genre);
        return genre;
    }
}
