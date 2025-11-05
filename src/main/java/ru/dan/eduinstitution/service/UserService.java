package ru.dan.eduinstitution.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.dan.eduinstitution.entity.User;
import ru.dan.eduinstitution.model.UserCreateDto;
import ru.dan.eduinstitution.model.UserResponseDto;
import ru.dan.eduinstitution.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final EntityToDtoMapper entityToDtoMapper;

    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        log.info("Creating user entity with name: {}", userCreateDto.getName());
        User user = new User(userCreateDto);
        User savedUser = userRepository.save(user);
        log.info("User entity saved with ID: {}", savedUser.getId());
        
        // Создаем UserResponseDto без вложенных объектов
        UserResponseDto responseDto = new UserResponseDto(savedUser);
        return responseDto;
    }

    public UserResponseDto getUserById(Long id) {
        log.info("Retrieving user entity by ID: {}", id);
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            log.warn("User not found with id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id);
        }
        
        User user = userOptional.get();
        log.info("User entity found by ID: {}", id);
        
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(user.getId());
        responseDto.setName(user.getName());
        responseDto.setEmail(user.getEmail());
        responseDto.setRole(user.getRole().name());

        if (user.getProfile() != null) {
            responseDto.setBio(user.getProfile().getBio());
            responseDto.setAvatarUrl(user.getProfile().getAvatarUrl());
        }
        responseDto.setCoursesTaught(entityToDtoMapper.mapCoursesToDtos(user.getCoursesTaught()));
        responseDto.setEnrollments(entityToDtoMapper.mapEnrollmentsToDtos(user.getEnrollments()));
        responseDto.setSubmissions(entityToDtoMapper.mapSubmissionsToDtos(user.getSubmissions()));
        responseDto.setQuizSubmissions(entityToDtoMapper.mapQuizSubmissionsToDtos(user.getQuizSubmissions()));
        responseDto.setCourseReviews(entityToDtoMapper.mapCourseReviewsToDtos(user.getCourseReviews()));

        return responseDto;
    }
}
