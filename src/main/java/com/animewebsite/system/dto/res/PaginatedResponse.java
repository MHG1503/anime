package com.animewebsite.system.dto.res;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PaginatedResponse <T> implements Serializable {
    private List<T> content;
    private int totalPages;
    private int currentPage;
    private long totalElements;
}
