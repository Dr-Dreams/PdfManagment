package com.pdf.pdfmanagment.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="roles")
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String name;

    @ManyToMany(mappedBy="roles")
    private List<User> users;
}