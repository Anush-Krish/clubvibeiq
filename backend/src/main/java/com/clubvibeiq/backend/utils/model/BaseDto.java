package com.clubvibeiq.backend.utils.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class BaseDto implements Serializable {
    private Date createdAt;
    private Date updatedAt;
}



