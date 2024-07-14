package com.example.rabbitmq.student;

import com.example.rabbitmq.persistence.CacheService;
import com.example.rabbitmq.persistence.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class StudentService {

    private final CacheService cacheService;
    private final StorageService storageService;

    public StudentService(CacheService cacheService, StorageService storageService) {
        this.cacheService = cacheService;
        this.storageService = storageService;
    }

    public Student getStudentById(Integer id) {
        Student student = cacheService.get(id.toString());

        if (student == null) {
            System.out.println("Cache miss!");

            student = storageService.findStudentById(id).orElse(null);

            if (student != null) {
                cacheService.set(student.getId().toString(), student, 6000L);
                return student;
            }
        } else {
            System.out.println("Cache hit!");
            return student;
        }

        return null;
    }

    public boolean addStudent(Student student) {
        Student savedStudent = storageService.saveStudent(student);

        if (savedStudent != null) {
            cacheService.set(savedStudent.getId().toString(), savedStudent, 6000L);
            return true;
        }

        return false;
    }

}
