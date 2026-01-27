package com.op.heroManager.user.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "phones")
@Table(name = "phones", indexes = @Index(name = "idx_phone_user_id", columnList = "user_id"))
public class Phone {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String areacode;
    private String number;

    @ManyToOne
    @JoinColumn(name = "user_id") 
    private User user;
}
