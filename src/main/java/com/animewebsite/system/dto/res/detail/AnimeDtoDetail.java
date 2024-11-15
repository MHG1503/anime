package com.animewebsite.system.dto.res.detail;

import com.animewebsite.system.dto.res.lazy.*;
import com.animewebsite.system.model.*;
import com.animewebsite.system.model.enums.Season;
import com.animewebsite.system.model.enums.Status;
import com.animewebsite.system.model.enums.Type;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class AnimeDtoDetail {
    private Long id;

    private String name;

    private List<AlternativeTitleDtoLazy> alternativeTitleDtoLazies;

    private String description;

    private Status status;

    private Type type;

    private Aired aired; // ngay phat song

    private Double duration; // thoi luong moi tap

    private Double malScore; // diem dua tren trang web MyAnimeList

    private Season season;

    private int year;

    private int episodes;

    private ImageDtoLazy imageDtoLazy;

//    private List<PromotionVideo> promo;

    private Set<GenreDtoLazy> genreDtoLazies;

    private Set<ProducerDtoLazy> producerDtoLazies;

    private Set<StudioDtoLazy> studioDtoLazies;
}
