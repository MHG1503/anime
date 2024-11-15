package com.animewebsite.system.convert;

import com.animewebsite.system.dto.res.detail.AnimeDtoDetail;
import com.animewebsite.system.dto.res.lazy.AnimeDtoLazy;
import com.animewebsite.system.dto.res.lazy.GenreDtoLazy;
import com.animewebsite.system.dto.res.lazy.ProducerDtoLazy;
import com.animewebsite.system.dto.res.lazy.StudioDtoLazy;
import com.animewebsite.system.model.Anime;
import com.animewebsite.system.model.Genre;
import com.animewebsite.system.model.Producer;
import com.animewebsite.system.model.Studio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring",
        uses = {ImageMapper.class,
                AlternativeTitleMapper.class})
public interface AnimeMapper{

    @Mapping(source = "image",target = "imageDtoLazy")
    AnimeDtoLazy animeToAnimeDtoLazy(Anime anime);

    Set<AnimeDtoLazy> animeSetToAnimeDtoLazySet(Set<Anime> animeSet);

    @Mapping(source = "image",target = "imageDtoLazy")
    @Mapping(source = "alternativeTitles",target = "alternativeTitleDtoLazies")
    @Mapping(source = "studios",target = "studioDtoLazies",qualifiedByName = "toStudioDtoLazy")
    @Mapping(source = "producers",target = "producerDtoLazies", qualifiedByName = "toProducerDtoLazy")
    @Mapping(source = "genres",target = "genreDtoLazies", qualifiedByName = "toGenreDtoLazy")
    AnimeDtoDetail animeToAnimeDtoDetail(Anime anime);

    @Named("toStudioDtoLazy")
    default Set<StudioDtoLazy> toStudioDtoLazy(Set<Studio> studios) {
        Set<StudioDtoLazy> studioDtoLazies = new HashSet<>();
        for(var studio : studios){
            StudioDtoLazy studioDtoLazy = new StudioDtoLazy();
            studioDtoLazy.setId(studio.getId());
            studioDtoLazy.setName(studio.getName());
            studioDtoLazy.setImage(ImageMapper.INSTANCE.imageToImageDtoLazy(studio.getImage()));
            studioDtoLazies.add(studioDtoLazy);
        }
        return studioDtoLazies;
    }

    @Named("toProducerDtoLazy")
    default Set<ProducerDtoLazy> toProducerDtoLazy(Set<Producer> producers) {
        Set<ProducerDtoLazy> producerDtoLazies = new HashSet<>();
        for(var producer : producers){
            ProducerDtoLazy producerDtoLazy = new ProducerDtoLazy();
            producerDtoLazy.setId(producer.getId());
            producerDtoLazy.setName(producer.getName());
            producerDtoLazy.setImage(ImageMapper.INSTANCE.imageToImageDtoLazy(producer.getImage()));
            producerDtoLazies.add(producerDtoLazy);
        }
        return producerDtoLazies;
    }

    @Named("toGenreDtoLazy")
    default Set<GenreDtoLazy> toGenreDtoLazy(Set<Genre> genres){
        Set<GenreDtoLazy> genreDtoLazies = new HashSet<>();
        for(var genre : genres){
            GenreDtoLazy genreDtoLazy = new GenreDtoLazy();
            genreDtoLazy.setId(genre.getId());
            genreDtoLazy.setName(genre.getName());
            genreDtoLazy.setDescription(genre.getDescription());
            genreDtoLazies.add(genreDtoLazy);
        }
        return genreDtoLazies;
    }
}
