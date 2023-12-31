package com.pdf.pdfmanagment.service;



import com.pdf.pdfmanagment.dto.UserDto;
import com.pdf.pdfmanagment.entity.User;
import com.pdf.pdfmanagment.exception.UserNotFoundException;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto, String token);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();

    void updateResetPasswordToken(String token, String email);

    User getByResetPasswordToken(String token);

    void updatePassword(User customer, String password);
    void verifyEmail(String email, String verificationCode) throws UserNotFoundException;


    User verifyUser(String verificationToken);
    boolean isVerified(User user);

    void updateVerification(User customer);
}