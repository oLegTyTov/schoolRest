package com.example.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.school.entities.Studente;

@Repository
public interface StudentRepository extends JpaRepository<Studente,Long>{

}
