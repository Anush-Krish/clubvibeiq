package com.clubvibeiq.backend.club.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "clubs")
@Getter
@Setter
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID clubId;
    private String clubName;

    private Double longitude;
    private Double latitude;
    private String locationLabel;
}
