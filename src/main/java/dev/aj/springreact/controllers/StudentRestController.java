package dev.aj.springreact.controllers;

import dev.aj.springreact.domain.model.Student;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class StudentRestController {

    public static final Student DEFAULT_STUDENT_AJ = Student.builder()
                                                            .name("AJ")
                                                            .age(44)
                                                            .grade("Masters")
                                                            .build();

    @GetMapping(value = "/default-student", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Student> getDefaultStudent() {
        return ResponseEntity.ok().body(DEFAULT_STUDENT_AJ);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok()
                             .body(List.of(
                                     Student.builder()
                                            .name("Zoravar")
                                            .age(1)
                                            .grade("Toddler")
                                            .build(),
                                     Student.builder()
                                            .name("Reet")
                                            .age(9)
                                            .grade("4th")
                                            .build(),
                                     Student.builder()
                                            .name("Dippi")
                                            .age(42)
                                            .grade("Nurse")
                                            .build(),
                                     DEFAULT_STUDENT_AJ
                             ));
    }

    @GetMapping(value = "/student/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable(value = "id") int id) {
        return ResponseEntity.ofNullable(Student.builder()
                                                .name("Student " + id)
                                                .age(id + 40)
                                                .grade("Graduate")
                                                .build());
    }

    @GetMapping(value = "/student/{name}/{age}/{grade}")
    public ResponseEntity<Student> getStudentByDetails(@PathVariable(value = "name") String name,
                                                       @PathVariable(value = "age") int age,
                                                       @PathVariable(value = "grade") String grade) {
        return ResponseEntity.ok(Student.builder()
                                        .name(name)
                                        .age(age)
                                        .grade(grade)
                                        .build());
    }
}
