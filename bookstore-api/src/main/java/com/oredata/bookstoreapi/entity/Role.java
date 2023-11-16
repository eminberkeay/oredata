package com.oredata.bookstoreapi.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserRole name;

    public Role() {
    }

    public Role(UserRole name) {
        this.name = name;
    }
}
