package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
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
