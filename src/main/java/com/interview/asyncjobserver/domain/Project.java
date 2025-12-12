package com.interview.asyncjobserver.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name="projects")
@Getter
@Setter
public class Project {
    @Id
    private UUID id;
    private String name;

    @ManyToOne
    @JoinColumn(name="owner_id")
    private User owner;

}
