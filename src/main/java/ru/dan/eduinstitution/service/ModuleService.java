package ru.dan.eduinstitution.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dan.eduinstitution.entity.Course;
import ru.dan.eduinstitution.entity.Module;
import ru.dan.eduinstitution.exception.ResourceNotFoundException;
import ru.dan.eduinstitution.model.ModuleCreateDto;
import ru.dan.eduinstitution.model.ModuleResponseDto;
import ru.dan.eduinstitution.model.ModuleUpdateDto;
import ru.dan.eduinstitution.repository.CourseRepository;
import ru.dan.eduinstitution.repository.ModuleRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с модулями.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;

    /**
     * Создание модуля.
     *
     * @param dto Данные для создания модуля
     * @return Созданный модуль
     */
    @Transactional
    public ModuleResponseDto createModule(@Valid ModuleCreateDto dto) {
        log.info("Creating module with title: {}", dto.getTitle());

        // Проверяем, что курс существует
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Course with id '%s' not found", dto.getCourseId())));

        Module module = new Module();
        module.setCourse(course);
        module.setTitle(dto.getTitle());
        module.setOrderIndex(dto.getOrderIndex());
        module.setDescription(dto.getDescription());

        module = moduleRepository.save(module);
        log.info("Module created with ID: {}", module.getId());

        return moduleResponseDtoFromModule(module);
    }

    /**
     * Получение модуля по ID.
     *
     * @param id ID модуля
     * @return Модуль
     */
    public ModuleResponseDto getModuleById(Long id) {
        log.info("Getting module by ID: {}", id);

        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Module with id '%s' not found", id)));

        return moduleResponseDtoFromModule(module);
    }

    /**
     * Обновление модуля.
     *
     * @param id  ID модуля
     * @param dto Данные для обновления модуля
     * @return Обновленный модуль
     */
    @Transactional
    public ModuleResponseDto updateModule(Long id, @Valid ModuleUpdateDto dto) {
        log.info("Updating module with ID: {}", id);

        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Module with id '%s' not found", id)));

        if (dto.getTitle() != null) {
            module.setTitle(dto.getTitle());
        }
        if (dto.getOrderIndex() != null) {
            module.setOrderIndex(dto.getOrderIndex());
        }
        if (dto.getDescription() != null) {
            module.setDescription(dto.getDescription());
        }

        module = moduleRepository.save(module);
        log.info("Module updated with ID: {}", module.getId());

        return moduleResponseDtoFromModule(module);
    }

    /**
     * Удаление модуля.
     *
     * @param id ID модуля
     */
    @Transactional
    public void deleteModule(Long id) {
        log.info("Deleting module with ID: {}", id);

        if (!moduleRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    String.format("Module with id '%s' not found", id));
        }

        moduleRepository.deleteById(id);
        log.info("Module deleted with ID: {}", id);
    }

    /**
     * Получение всех модулей по ID курса.
     *
     * @param courseId ID курса
     * @return Список модулей
     */
    public List<ModuleResponseDto> getModulesByCourseId(Long courseId) {
        log.info("Getting modules for course with ID: {}", courseId);

        List<Module> modules = moduleRepository.findByCourseIdWithCourse(courseId);
        return modules.stream()
                .map(this::moduleResponseDtoFromModule)
                .collect(Collectors.toList());
    }

    private ModuleResponseDto moduleResponseDtoFromModule(Module module) {
        ModuleResponseDto dto = new ModuleResponseDto();
        dto.setId(module.getId());
        dto.setTitle(module.getTitle());
        dto.setOrderIndex(module.getOrderIndex());
        dto.setDescription(module.getDescription());
        dto.setCourseId(module.getCourse().getId());
        dto.setCourseTitle(module.getCourse().getTitle());
        return dto;
    }
}