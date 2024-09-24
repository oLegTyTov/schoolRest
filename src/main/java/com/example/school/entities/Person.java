package com.example.school.entities;



import java.util.Set;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;

import java.util.HashSet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.NoArgsConstructor;
import jakarta.persistence.JoinColumn;
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@Entity
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, // Використовуємо ім'я класу для ідентифікації типу
    include = JsonTypeInfo.As.PROPERTY,
    property = "type" // Вказуємо, що "type" буде визначати клас (Teacher/Student)
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Studente.class, name = "student"),
    @JsonSubTypes.Type(value = Teacher.class, name = "teacher"),
    @JsonSubTypes.Type(value = Administrator.class, name = "administrator")
    
})
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private short age;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age &&
                Objects.equals(id, person.id) &&
                Objects.equals(name, person.name) &&
                Objects.equals(surname, person.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, age);
    }
}