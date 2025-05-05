package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserExpec spec, @PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserModel> userModelsPage = userService.findAll(spec,pageable);

         if (!userModelsPage.isEmpty()){
             for (UserModel user:userModelsPage.toList()){
                 user.add(linkTo(methodOn((UserController.class)).getOneUser(user.getUserId())).withSelfRel());
             }
         }
        return ResponseEntity.status(HttpStatus.OK).body(userModelsPage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> userModel = userService.findById(userId);
        if (!userModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body("não existe user");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(userModel.get());
        }

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> userModelOptional = userService.findById(userId);

        if (!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("nao exite user");
        } else {
           userService.deleteUser(userModelOptional.get());
           return ResponseEntity.status(HttpStatus.OK).body("USER FOI DELETADO COM SUCESSO");
        }


    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId, @RequestBody @Valid @JsonView(UserDto.UserView.UserPut.class)  UserDto userDto) {
        Optional<UserModel> userModelUpdate = userService.findById(userId);
        if (!userModelUpdate.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não encontrouo user");
        } else {
            var userModelUpdateGet = userModelUpdate.get();
            userModelUpdateGet.setFullname(userDto.getFullname());
            userModelUpdateGet.setPhoneNumber(userDto.getPhoneNumber());
            userModelUpdateGet.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModelUpdateGet);
            return ResponseEntity.status(HttpStatus.OK).body(userModelUpdateGet);
        }

    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,@RequestBody @Valid @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto) {
        Optional<UserModel> userModelPassword = userService.findById(userId);
        if (!userModelPassword.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("não encontrou usuario");
        } else if (!userModelPassword.get().getPassword().equals(userDto.getOldPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error esta usando senha velha");
        } else {
            var userModel = userModelPassword.get();
            userModel.setPassword(userDto.getPassword());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);
            return ResponseEntity.status(HttpStatus.OK).body("Senha Atualizada com Sucesso");


        }

    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId, @JsonView(UserDto.UserView.ImagePut.class) @RequestBody @Valid UserDto userDto) {
        Optional<UserModel> userModelImage = userService.findById(userId);
        if (!userModelImage.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Errro nao encontrou user");
        } else {
            var userModel = userModelImage.get();
            userModel.setImageUrl(userDto.getImageUrl());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);
            return ResponseEntity.status(HttpStatus.OK).body(userModel);
        }
    }
}
