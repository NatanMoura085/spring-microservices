package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.repositories.CourserRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourserRepository courserRepository;

    @Autowired
    CourseUserRepository courseUserRepository;
    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    AuthUserClient authUserClient;

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        boolean deleteCourseUserInAuthUser = false;
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

        List<CourseUserModel> courseUserModelsList = courseUserRepository.findAllCourseUserIntoCourse(courseModel.getCourseId());
        if (!courseUserModelsList.isEmpty()) {
            courseUserRepository.deleteAll(courseUserModelsList);
            deleteCourseUserInAuthUser = true;
        }
        courserRepository.delete(courseModel);
        if (deleteCourseUserInAuthUser) {
            authUserClient.deleteCourseInAuthUser(courseModel.getCourseId());
        }

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


}
