package com.axcent.TimeSheet.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSheetMensile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false)
    private String sede;

    @Column(nullable = false)
    private int anno;

    @Column(nullable = false)
    private int mese;

    @OneToMany(mappedBy = "timesheetMensile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeSheetGiornaliero> giornalieri = new ArrayList<>();
}
