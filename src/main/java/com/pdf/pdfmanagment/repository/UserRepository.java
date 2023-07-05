package com.pdf.pdfmanagment.repository;


import com.pdf.pdfmanagment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    public User findByResetPasswordToken(String token);

    User findByVerificationToken(String verificationToken);
}