package com.example.demosqltest.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Entity
@Table(schema = "public")
public class HomeEntity implements Persistable<Long> {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    Long id;
    @Getter
    @Setter
    String name;


    @Transient
    @Setter
    private Boolean isNew;

    @Override
    public boolean isNew() {
        return isNew == null ? true : isNew;
    }

    @PrePersist
    @PostLoad
    void markNotNew() {
        if (isNew == null) {
            this.isNew = false;
        }
    }
}
