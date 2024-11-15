package com.animewebsite.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users = new ArrayList<>();

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
        this.users = new ArrayList<>();
    }
}
