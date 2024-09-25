package com.example.school.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.school.entities.Mark;
import com.example.school.entities.Subject;
import com.example.school.repositories.MarkRepository;

@Service
public class MarkService {
@Autowired
    private MarkRepository markRepository;
    public boolean addMark(Mark mark) {
        if (markRepository.existsByDescriptionAndMark(mark.getDescription(), mark.getMark())) {
            return false;
        } else {
            markRepository.save(mark);
            return true;
        }
    }
    public Mark getMarkByName(short mark)
    {
    return markRepository.findByMark(mark);
    }
}
