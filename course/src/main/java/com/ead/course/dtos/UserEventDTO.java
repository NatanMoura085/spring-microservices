package com.ead.course.dtos;

import com.ead.course.models.UserModeL;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

@Data
public class UserEventDTO {
    private UUID userId;
    private String username;
    private String email;
    private String fullName;
    private String userStatus;
    private String userType;
    private String phoneNumber;
    private String cpf;
    private String imageUrl;
    private String actionType;

    public UserModeL convertToUserModel() {
        var userModel = new UserModeL();
        userModel.setFullName(this.fullName);
        BeanUtils.copyProperties(this, userModel);
        return userModel;
    }

}
