package dev.aj.springreact.controllers;

import dev.aj.springreact.domain.model.Student;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class StudentRestController {

    private static final Student DEFAULT_STUDENT_AJ = Student.builder()
                                                            .name("AJ")
                                                            .age(44)
                                                            .grade("Masters")
                                                            .build();

    private static final Map<String, Student> STUDENT_MAP = new HashMap<>();

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

    @GetMapping(value = "/student/query")
    public ResponseEntity<Student> getStudentByQuery(@RequestParam(value = "name") String name,
                                                     @RequestParam(value = "age") int age,
                                                     @RequestParam(value = "grade") String grade) {
        return ResponseEntity.ok(Student.builder()
                                        .name(name)
                                        .age(age)
                                        .grade(grade)
                                        .build());
    }

    @GetMapping(value = "/student/name/{name}")
    public ResponseEntity<Student> getStudentByName(@PathVariable String name) {
        return ResponseEntity.ofNullable(STUDENT_MAP.get(name));
    }

    @PostMapping(value = "/student/create")
    public HttpStatus createStudent(@RequestBody Student newStudent) {
        STUDENT_MAP.put(newStudent.getName(), newStudent);
        return HttpStatus.CREATED;
    }

    @PutMapping("/student/update/{name}")
    public HttpStatus updateStudent(@PathVariable String name, @RequestBody Student student) {
        STUDENT_MAP.put(name, student);
        return HttpStatus.ACCEPTED;
    }

    @DeleteMapping("/student/delete/{name}")
    public HttpStatus deleteStudent(@PathVariable String name) {
        STUDENT_MAP.remove(name);
        return HttpStatus.ACCEPTED;
    }

}
