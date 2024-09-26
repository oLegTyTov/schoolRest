package com.example.school.entities;

import java.util.Set;

import java.util.Objects;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
public class WrapperTestWithStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "mark_id")
    private Mark mark;
    @OneToOne
    private SchoolDebt schoolDebt;
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "school_test_id") 
    private SchoolTest schoolTest;
    @ManyToOne
    @JoinColumn(name = "studente_id")
    private Studente studente;
        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WrapperTestWithStudent that = (WrapperTestWithStudent) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}