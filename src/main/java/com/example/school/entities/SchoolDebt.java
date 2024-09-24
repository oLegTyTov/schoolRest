package com.example.school.entities;

import java.util.Objects;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class SchoolDebt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "wrapper_test_with_student_id")
    private WrapperlTestWithStudent wrapperlTestWithStudent;
        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchoolDebt that = (SchoolDebt) o;
        return Objects.equals(id, that.id);
    }
    @ManyToOne
private Studente studente;
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}