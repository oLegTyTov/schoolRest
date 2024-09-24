package com.example.school.entities;
import lombok.Data;

import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@Entity
public class Teacher extends Person {
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "teacher_extracurricularcourse",
        joinColumns = @JoinColumn(name = "teacher_id"),
        inverseJoinColumns = @JoinColumn(name = "extracurricularcourse_id")
    )
    private Set<Extracurricularcourse> extracurricularcourses = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "teacher_schoolclass",
        joinColumns = @JoinColumn(name = "teacher_id"),
        inverseJoinColumns = @JoinColumn(name = "schoolclass_id")
    )
    private Set<SchoolClass> schoolClasses = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "teacher_subject",
        joinColumns = @JoinColumn(name = "teacher_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects = new HashSet<>();
}