package com.clubvibeiq.backend.userpreference.entity;

import com.clubvibeiq.backend.utils.model.MusicLibrary;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID preferenceId;

    private UUID userId;
    private UUID clubId;
    private Boolean isActive; //if the user is in club currently

    @Column(columnDefinition = "jsonb")
    @Type(JsonType.class)
    private MusicLibrary musicLibrary;

}
