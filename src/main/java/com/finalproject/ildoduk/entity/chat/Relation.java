package com.finalproject.ildoduk.entity.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Relation {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long rel_No;

private String send;
private String accept;

}
//