package com.example.school.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;
import java.util.HashSet;
@Data
@NoArgsConstructor
@Entity
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Studente> students = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "schoolclass_teacher",
        joinColumns = @JoinColumn(name = "schoolclass_id"),
        inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private Set<Teacher> teachers = new HashSet<>();
}