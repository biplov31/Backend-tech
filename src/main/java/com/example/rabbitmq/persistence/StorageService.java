package com.example.rabbitmq.persistence;

import com.example.rabbitmq.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

@Service
public class StorageService {

    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DB_NAME = "jdbcdemo";
    private static final String DB_TYPE = "mysql";

    @Autowired
    private CacheService cacheService;

    private static Connection getConnection() throws Exception {
        String jdbcUrl = "jdbc:" + DB_TYPE + "://" + HOST + ":" + PORT + "/" + DB_NAME;
        return DriverManager.getConnection(jdbcUrl, USER, PASSWORD);
    }

    public Student saveStudent(Student student) {
        // String user = "sa";
        // String password = "";
        // String jdbcUrl = "jdbc:h2:mem:studentdb;DB_CLOSE_DELAY=-1;";

        String insertionQuery = "insert into rcl_student (first_name, last_name, age, email) values ('" + student.getFirstName() + "', '" + student.getLastName() + "', '" + student.getAge() + "', '" + student.getEmail() + "');";

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            int inserted = statement.executeUpdate(insertionQuery, Statement.RETURN_GENERATED_KEYS);
            if (inserted != 0) {
                System.out.println("Student added!");

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    student.setId(id);
                    // cacheService.set(student.getId().toString(), student, 6000L);
                }

                connection.close();
                return student;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void readStudents() {
        String retrievalQuery = "select * from rcl_student";

        try (Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(retrievalQuery)
        ) {
            while (resultSet.next()) {
                System.out.println(
                        "Firstname: " + resultSet.getString("first_name") + " Lastname: " + resultSet.getString("last_name")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<Student> findStudentById(Integer id) {
        String retrievalQuery = "select * from rcl_student where id = " + id;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(retrievalQuery)
        ) {
            if (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setFirstName(resultSet.getString("first_name"));
                student.setLastName(resultSet.getString("last_name"));
                student.setAge(resultSet.getInt("age"));
                student.setEmail(resultSet.getString("email"));

                return Optional.of(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

}

