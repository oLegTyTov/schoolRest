package com.example.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.school.entities.Mark;
import java.util.List;


@Repository
public interface MarkRepository extends JpaRepository<Mark,Long>{
boolean existsByDescriptionAndMark(String description, short mark);
Mark findByMark(short mark);
}
