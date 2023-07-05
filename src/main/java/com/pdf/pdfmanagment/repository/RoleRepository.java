package com.pdf.pdfmanagment.repository;


import com.pdf.pdfmanagment.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}