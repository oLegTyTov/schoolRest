package com.example.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.school.entities.WrapperTestWithStudent;
import com.example.school.entities.SchoolTest;
import java.util.List;
import com.example.school.entities.Studente;


public interface WrapperTestPersonRepository extends JpaRepository<WrapperTestWithStudent,Long>{
WrapperTestWithStudent findByStudenteAndSchoolTest(Studente studente, SchoolTest schoolTest);
}
