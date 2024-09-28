package com.example.school.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

import java.util.Objects;

import java.util.HashSet;
@Entity
@NoArgsConstructor
@Data
public class Extracurricularcourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "extracurricularcourses", fetch = FetchType.LAZY)
    private Set<Studente> students = new HashSet<>();

    @ManyToMany(mappedBy = "extracurricularcourses", fetch = FetchType.LAZY)
    private Set<Teacher> teachers = new HashSet<>();
        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Extracurricularcourse that = (Extracurricularcourse) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}