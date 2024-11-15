package com.animewebsite.system.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    USER("USER", "Người dùng"),
    MANAGER("MANAGER", "Quản lí"),
    ADMIN("ADMIN", "Quản trị viên");
    private final String name;
    private final String description;
     RoleType(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
