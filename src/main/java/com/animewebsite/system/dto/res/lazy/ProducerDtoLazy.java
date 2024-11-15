package com.animewebsite.system.dto.res.lazy;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProducerDtoLazy {
    private Long id;

    private String name;

    private LocalDate established;

    private String introduce;

    private ImageDtoLazy image;

}
