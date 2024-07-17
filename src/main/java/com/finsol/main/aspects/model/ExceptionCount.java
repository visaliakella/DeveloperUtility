package com.finsol.main.aspects.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "dailyexceptioncount")
public class ExceptionCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private LocalDateTime date;

    @Column(name ="Exception_Count")
    private long Exception_Count;

    @Column(name="Exception_date")
    private  LocalDateTime Exception_date;


}
