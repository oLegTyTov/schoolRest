package com.example.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.school.entities.Teacher;
@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Long>{
    void deleteByUsername(String username);
            @Modifying
    @Transactional
    @Query(value = "DELETE FROM teacher_extracurricularcourse WHERE teacher_id = :teacherId", nativeQuery = true)
    void deleteExtracurricularcourseByTeacherId(@Param("teacherId") Long teacherId);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM teacher_school_classes WHERE teacher_id = :teacherId", nativeQuery = true)
    void deleteSchoolClassesByTeacherId(@Param("teacherId") Long teacherId);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM teacher_subjects WHERE teacher_id = :teacherId", nativeQuery = true)
    void deleteSubjectsByTeacherId(@Param("teacherId") Long teacherId);
}
