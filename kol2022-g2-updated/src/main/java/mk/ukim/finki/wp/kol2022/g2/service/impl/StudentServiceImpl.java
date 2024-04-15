package mk.ukim.finki.wp.kol2022.g2.service.impl;

import mk.ukim.finki.wp.kol2022.g2.model.Course;
import mk.ukim.finki.wp.kol2022.g2.model.Student;
import mk.ukim.finki.wp.kol2022.g2.model.StudentType;
import mk.ukim.finki.wp.kol2022.g2.model.exceptions.InvalidStudentIdException;
import mk.ukim.finki.wp.kol2022.g2.repository.StudentRepository;
import mk.ukim.finki.wp.kol2022.g2.service.CourseService;
import mk.ukim.finki.wp.kol2022.g2.service.StudentService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final CourseService courseService;

    public StudentServiceImpl(StudentRepository studentRepository, CourseService courseService) {
        this.studentRepository = studentRepository;

        this.courseService = courseService;
    }

    @Override
    public List<Student> listAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow(InvalidStudentIdException::new);
    }

    @Override
    public Student create(String name, String email, String password, StudentType type, List<Long> courseId, LocalDate enrollmentDate) {
        Student student = new Student(name,
                email,
                password,
                type,
                courseId.stream().map(courseService::findById).collect(Collectors.toList()),
                enrollmentDate);

        return studentRepository.save(student);
    }

    @Override
    public Student update(Long id, String name, String email, String password, StudentType type, List<Long> coursesId, LocalDate enrollmentDate) {
        Student student = studentRepository.findById(id).orElseThrow(InvalidStudentIdException::new);
        student.setName(name);
        student.setEmail(email);
        student.setPassword(password);
        student.setType(type);
        student.setCourses(coursesId.stream().map(courseId -> courseService.findById(courseId)).collect(Collectors.toList()));
        student.setEnrollmentDate(enrollmentDate);
        return studentRepository.save(student);
    }

    @Override
    public Student delete(Long id) {
        Student student = this.findById(id);
        studentRepository.delete(student);
        return student;
    }

    @Override
    public List<Student> filter(Long courseId, Integer yearsOfStudying) {
        if (courseId != null && yearsOfStudying != null) {
            Course course = courseService.findById(courseId);
            LocalDate studyingAfter = LocalDate.now().minusYears(yearsOfStudying);

            List<Student> students = studentRepository.findByEnrollmentDateAfterAndCoursesContaining(studyingAfter,course);
            return students;

        } else if (courseId == null && yearsOfStudying == null) {
            return studentRepository.findAll();

        } else if (courseId != null) {
            return studentRepository.findByCoursesContaining(courseService.findById(courseId));

        } else {
            LocalDate enrollmentDate = LocalDate.now().minusYears(yearsOfStudying);
            return studentRepository.findByEnrollmentDateAfter(enrollmentDate);

        }
    }
}
