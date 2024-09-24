package com.example.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.school.entities.Subject;
import java.util.List;


@Repository
public interface SubjectRepository extends JpaRepository<Subject,Long>{
boolean existsByName(String name);
Subject findByName(String name);
}
