package ru.dan.eduinstitution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dan.eduinstitution.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}