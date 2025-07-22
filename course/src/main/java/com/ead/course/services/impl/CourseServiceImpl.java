package com.ead.course.services.impl;

import com.ead.course.dtos.NotificationComandDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.models.UserModeL;
import com.ead.course.publishers.NotificationCommandPublisher;
import com.ead.course.repositories.CourserRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.repositories.UserRepository;
import com.ead.course.services.CourseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourserRepository courserRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    NotificationCommandPublisher notificationCommandPublisher;

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        List<ModuleModel> moduleModelList = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());
        if (!moduleModelList.isEmpty()) {
            for (ModuleModel module : moduleModelList) {
                List<LessonModel> lessonModels = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
                if (!lessonModels.isEmpty()) {
                    lessonRepository.deleteAll(lessonModels);
                }
            }
            moduleRepository.deleteAll(moduleModelList);
        }

        courserRepository.deleteCourseUserByCourse(courseModel.getCourseId());
        courserRepository.delete(courseModel);

    }

    @Override
    public CourseModel save(CourseModel courseModel) {
        return courserRepository.save(courseModel);
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        return courserRepository.findById(courseId);
    }

    @Override
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
        return courserRepository.findAll(spec,pageable);
    }

    @Override
    public boolean existsByCourseAndUser(UUID courseId, UUID userId) {
        return courserRepository.existsByCourseAndUser(courseId, userId);
    }

    @Transactional
    @Override
    public void saveSubscriptionUserInCourse(UUID courseId, UUID userId) {
        courserRepository.saveCourseUser(courseId, userId);
    }

    @Transactional
    @Override
    public void saveSubscriptionUserInCourseAndSendNotification(CourseModel course, UserModeL user) {
        courserRepository.saveCourseUser(course.getCourseId(), user.getUserId());

        try {
            var notificationCommandDto = new NotificationComandDto();
            ;
            notificationCommandDto.setTitle("Bem-Vindo(a) ao curso" + course.getName());
            notificationCommandDto.setMessage(user.getFullName() + "a sua inscrição foi realizada com sucesso!");
            notificationCommandDto.setUserId(user.getUserId());
            notificationCommandPublisher.publishNotificationCommand(notificationCommandDto);

        } catch (Exception e) {
            log.warn("Error sending notification");
        }
    }
}
