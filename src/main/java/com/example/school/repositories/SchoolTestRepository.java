package com.example.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.school.entities.SchoolTest;

@Repository
public interface SchoolTestRepository extends JpaRepository<SchoolTest,Long>{
boolean existsByTextDocumentOfTest(String textDocumentOfTest);
}
