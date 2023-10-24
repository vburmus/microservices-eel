package com.epam.esm.tag.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "tag")
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String imageUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Tag tag = (Tag) o;
        return id != null && Objects.equals(id, tag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,name,imageUrl);
    }
}