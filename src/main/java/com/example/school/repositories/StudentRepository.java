package com.example.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.school.entities.Studente;

@Repository
public interface StudentRepository extends JpaRepository<Studente,Long>{
        @Modifying
    @Transactional
    @Query(value = "DELETE FROM student_extracurricularcourse WHERE student_id = :studentId", nativeQuery = true)
    void deleteStudentCoursesByStudentId(@Param("studentId") Long studentId);
}
