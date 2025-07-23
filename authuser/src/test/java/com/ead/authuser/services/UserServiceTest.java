package com.ead.authuser.services;

import com.ead.authuser.controllers.UserController;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.impl.UserServiceIMPL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.hateoas.Link;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    private UserServiceIMPL userServiceIMPL;

    private UserModel userModel = new UserModel();

    @BeforeEach
    public void loadUser() {
        UUID id = UUID.fromString("92beee48-d846-4f12-9593-6de289aec03f");
        userModel.setUserId(id);
        userModel.setUserType(UserType.INSTRUCTOR);
        userModel.setPassword("23343");
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setImageUrl("");
        userModel.setFullname("joão");
        userModel.setPhoneNumber("32432432");
        userModel.setCpf("43243243443");
        userModel.setEmail("mouranatan933@gmail.com");
    }

    @Test
    void givenSpecAndPageable_whenFindAll_thenReturnsPagedUsers() {
        Specification<UserModel> spec = (root,query,cb) ->cb.equal(root.get("fullName"),"joão");
        Pageable pageable = PageRequest.of(0,5);
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
    void givenUsersPage_whenNotEmpty_thenAddSelfLinks() {
        Page<UserModel> page = new PageImpl<>(List.of(userModel));
        UUID id = UUID.fromString("92beee48-d846-4f12-9593-6de289aec03f");
        if (!page.isEmpty()) {
            for (UserModel u : page.toList()) {
                u.add(linkTo(methodOn(UserController.class).getOneUser(u.getUserId())).withSelfRel());
            }
        }
        Assertions.assertFalse(userModel.getLinks().isEmpty());
        Link selflink = userModel.getLink("self").orElse(null);
        Assertions.assertNotNull(selflink);
        Assertions.assertTrue(selflink.getHref().contains(id.toString()));
    }
    @Test
    void givenValidUserId_whenFindById_thenReturnsUser() {
        UUID id = UUID.fromString("92beee48-d846-4f12-9593-6de289aec03f");
        userModel.setFullname("joao");
        userModel.setUserType(UserType.INSTRUCTOR);
        userModel.setCpf("4435");
        userModel.setEmail("mouranatan933@gmail.com");
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setPassword("3333");

        Mockito.when(userServiceIMPL.findById(id)).thenReturn(Optional.of(userModel));
        Optional<UserModel> result = userServiceIMPL.findById(id);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("joao",result.get().getFullname());
        Mockito.verify(userRepository,Mockito.times(1)).findById(id);
    }


}
