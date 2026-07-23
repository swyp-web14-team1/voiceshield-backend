package com.swyp.voiceshield.category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @Column(name = "category_id")
    private String id;

    @Column(name = "category_name", nullable = false, unique = true)
    private String name;

    protected Category() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
