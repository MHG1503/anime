package com.animewebsite.system.dto.req;

import com.animewebsite.system.model.Aired;
import com.animewebsite.system.model.enums.Season;
import com.animewebsite.system.model.enums.Status;
import com.animewebsite.system.model.enums.Type;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UpdateAnimeRequest {
    @NotBlank
    private String name; // chinh la title luon;
    private Set<AlternativeTitleRequest> alternativeTitles; // Ten dong nghia
    @NotBlank
    private String description;
    private Long seriesId;
    private Status status;
    private Type type;
    private Aired aired; // ngay phat song
    @NotNull
    private Double duration; // thoi luong moi tap
    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    private Double malScore; // diem dua tren trang web MyAnimeList
    private Season season;
    @NotNull
    private Integer year;
    @NotNull
    private Integer episodes;
    private Set<Long> genresIds;
    private Set<Long> producersIds;
    private Set<Long> studiosIds;
}
