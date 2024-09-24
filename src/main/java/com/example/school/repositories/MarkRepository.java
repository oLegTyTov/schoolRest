package com.example.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.school.entities.Mark;

@Repository
public interface MarkRepository extends JpaRepository<Mark,Long>{
boolean existsByDescriptionAndMark(String description, short mark);
}
