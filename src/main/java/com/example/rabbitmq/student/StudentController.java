package com.example.rabbitmq.student;

import com.example.rabbitmq.persistence.CacheService;
import com.example.rabbitmq.persistence.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Integer id) {
        Student student = studentService.getStudentById(id);

        if (student != null) {
            return new ResponseEntity<>(student, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<String> addStudent(@RequestBody Student student) {
        Boolean success = studentService.addStudent(student);

        if (success) {
            return new ResponseEntity<>("Student saved successfully.", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Failed to save student.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
