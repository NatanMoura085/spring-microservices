package com.ead.course.repositories;

import com.ead.course.models.UserModeL;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModeL, UUID> {

}
