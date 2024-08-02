package com.genieLogiciel.Umons.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class SignUpRequest {

    private String name;
    private String firstName;
    private String birthDate;
    private String address;
    private String city;
    private String phone;
    private String status = "pending";
    private String filial;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
