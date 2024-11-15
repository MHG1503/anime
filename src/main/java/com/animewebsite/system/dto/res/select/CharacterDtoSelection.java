package com.animewebsite.system.dto.res.select;

import com.animewebsite.system.dto.res.lazy.ImageDtoLazy;
import com.animewebsite.system.model.Image;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterDtoSelection {
    private Long id;

    private String name;

    private ImageDtoLazy image;
}
