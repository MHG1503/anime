package com.animewebsite.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
public abstract class AbstractAuditBase implements Serializable {
    @CreatedDate
    @Column(updatable = false)
    @JsonIgnore
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(insertable = false)
    @JsonIgnore
    private LocalDateTime updateDate;

    @CreatedBy
    @Column(updatable = false)
    @JsonIgnore
    private String createBy;

    @LastModifiedDate
    @Column(insertable = false)
    @JsonIgnore
    private String updateBy;
}
