package ru.dan.eduinstitution.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dan.eduinstitution.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    
    @EntityGraph(attributePaths = {
        "coursesTaught",
        "enrollments",
        "submissions",
        "quizSubmissions",
        "courseReviews"
    })
    Optional<User> findById(Long id);
}