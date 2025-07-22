package com.ead.authuser.services;

import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.impl.UserServiceIMPL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    private UserServiceIMPL userServiceIMPL;

    @Test
    void testFindAllWithSpecAnPageable() {
        Specification<UserModel> spec = (root,query,cb) ->cb.equal(root.get("fullName"),"joão");
        Pageable pageable = PageRequest.of(0,5);
        UserModel userModel = new UserModel();
        userModel.setUserId(UUID.randomUUID());
        userModel.setUserType(UserType.INSTRUCTOR);
        userModel.setPassword("23343");
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setImageUrl("");
        userModel.setFullname("joão");
        userModel.setPhoneNumber("32432432");
        userModel.setCpf("43243243443");
        userModel.setEmail("mouranatan933@gmail.com");
        List<UserModel> users = List.of(userModel);
        Page<UserModel> page = new PageImpl<>(users,pageable,users.size());

        Mockito.when(userRepository.findAll(spec,pageable)).thenReturn(page);

        Page<UserModel> result = userServiceIMPL.findAll(spec,pageable);

        Assertions.assertEquals(1,result.getTotalElements());
        Assertions.assertEquals("joão",result.getContent().get(0).getFullname());


        Mockito.verify(userRepository,Mockito.times(1)).findAll(spec,pageable);


    }

    @Test
    void testFindByIdWithIdNull() {
        UUID id = UUID.fromString("92beee48-d846-4f12-9593-6de289aec03f");
        UserModel userModel = new UserModel();
        userModel.setFullname("joao");
        userModel.setUserId(id);
        userModel.setUserType(UserType.INSTRUCTOR);
        userModel.setCpf("4435");
        userModel.setEmail("mouranatan933@gmail.com");
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setPassword("3333");

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(userModel));
        Optional<UserModel> result = userServiceIMPL.findById(id);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("joao",result.get().getFullname());
        Mockito.verify(userRepository,Mockito.times(1)).findById(id);
    }
}
