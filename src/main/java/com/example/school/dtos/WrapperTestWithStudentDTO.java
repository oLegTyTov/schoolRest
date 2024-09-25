package com.example.school.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WrapperTestWithStudentDTO {
private String studentUsername;
private String schoolTestName;
private short mark;
}