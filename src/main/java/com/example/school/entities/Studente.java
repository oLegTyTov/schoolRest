package com.example.school.entities;
import lombok.Data;

import java.util.Set;

import java.util.HashSet;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@Entity
public class Studente extends Person {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_class_id")
    private SchoolClass schoolClass;

    @OneToMany(mappedBy = "studente", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<SchoolDebt> schoolDebts = new HashSet<>();

    @OneToMany(mappedBy = "studente", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<WrapperTestWithStudent> wrapperlTestWithStudents = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "student_extracurricularcourse",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "extracurricularcourse_id")
    )
    private Set<Extracurricularcourse> extracurricularcourses = new HashSet<>();
    
}