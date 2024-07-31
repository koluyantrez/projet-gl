package com.genieLogiciel.Umons.extensionOussama.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne
    private Cours cours;

    @ManyToOne
    private Room room;

    private String timeSlot; // e.g., "Monday 9-11"
}
