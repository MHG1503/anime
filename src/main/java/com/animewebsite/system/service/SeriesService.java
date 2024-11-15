package com.animewebsite.system.service;

import com.animewebsite.system.convert.SeriesMapper;
import com.animewebsite.system.dto.req.SeriesRequest;
import com.animewebsite.system.dto.res.PaginatedResponse;
import com.animewebsite.system.dto.res.detail.SeriesDtoDetail;
import com.animewebsite.system.dto.res.lazy.SeriesDtoLazy;
import com.animewebsite.system.model.Anime;
import com.animewebsite.system.model.Series;
import com.animewebsite.system.repository.AnimeRepository;
import com.animewebsite.system.repository.SeriesRepository;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeriesService {
    private final SeriesRepository seriesRepository;
    private final SeriesMapper seriesMapper;
    private final AnimeRepository animeRepository;

    public Object getAllSeries(int pageNum, int pageSize){

        if(pageNum != 0 && pageSize != 0){
            Pageable pageable = PageRequest.of(pageNum-1,pageSize);
            Page<Series> series = seriesRepository.findAll(pageable);
            return new PaginatedResponse<>(
                    series.getContent().stream().map(seriesMapper::seriesToSeriesDtoLazy).toList(),
                    series.getTotalPages(),
                    series.getNumber()+1,
                    series.getTotalElements()
            );
        }
        return seriesRepository.findAll().stream().map(seriesMapper::seriesToSeriesDtoLazy).toList();
    }

//    public SeriesDtoDetail getByName(String title){
//        return seriesMapper.seriesToSeriesDtoDetail(seriesRepository
//                .findByTitle(title,NamedEntityGraph.fetching("series-anime"))
//                .orElseThrow(()->new RuntimeException("Khong tim thay series")));
//    }

    public SeriesDtoLazy getById(Long id){
        return seriesMapper.seriesToSeriesDtoLazy(seriesRepository
                .findById(id)
                .orElseThrow(()->new RuntimeException("Khong tim thay series")));
    }

    @Transactional
    public SeriesDtoLazy createSeries(SeriesRequest request){
        String titleRequest = request.getTitle();
        Series series = Series
                .builder()
                .title(titleRequest)
                .animeSet(new HashSet<>())
                .build();
        return seriesMapper.seriesToSeriesDtoLazy(seriesRepository.save(series));
    }

    @Transactional
    public SeriesDtoLazy updateSeries(Long id,SeriesRequest request){
        Series series = seriesRepository.findById(id, NamedEntityGraph.fetching("series-anime"))
                .orElseThrow(()->new RuntimeException("Khong tim thay series"));

        String titleRequest = request.getTitle();
        series.setTitle(titleRequest);

        return seriesMapper.seriesToSeriesDtoLazy(seriesRepository.save(series));
    }

    @Transactional
    public void deleteSeries(Long id){
        Series series = seriesRepository
                .findById(id)
                .orElseThrow(()->new RuntimeException("Khong tim thay series"));

        seriesRepository.deleteById(id);
    }

    @Transactional
    public SeriesDtoLazy manageAnimeInSeries(Long seriesId, Long animeId, boolean isAdd){
        Series series = seriesRepository
                .findById(seriesId,NamedEntityGraph.fetching("series-anime"))
                .orElseThrow(()->new RuntimeException("Khong tim thay series"));

        Anime anime = animeRepository
                .findById(animeId)
                .orElseThrow(() -> new RuntimeException("Khong tim thay anime"));

        Optional<Anime> animeOptional = series
                .getAnimeSet()
                .stream()
                .filter((a -> a.getId().equals(animeId)))
                .findFirst();
        if(isAdd){
            if (animeOptional.isEmpty()) {
                if (series.addAnimeToSeries(anime)) {
                    return seriesMapper.seriesToSeriesDtoLazy(seriesRepository.save(series));
                }
                throw new RuntimeException("Them anime vao series that bai!");
            }
            throw new RuntimeException("Anime da ton tai trong series!");
        } else {
            if (animeOptional.isPresent()) {
                if (series.removeAnimeFromSeries(animeOptional.get())) {
                    return seriesMapper.seriesToSeriesDtoLazy(seriesRepository.save(series));
                }
                throw new RuntimeException("xoa anime ra khoi series that bai!");
            }
            throw new RuntimeException("anime khong ton tai trong series");
        }
    }
}
