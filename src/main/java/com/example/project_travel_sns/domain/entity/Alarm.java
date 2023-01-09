package com.example.project_travel_sns.domain.entity;

import com.example.project_travel_sns.util.alarm.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class Alarm extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String alarmType;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @ManyToOne
    @JoinColumn(name = "fromUserID")
    private User fromUser;
    @ManyToOne
    @JoinColumn(name = "targetId")
    private Post targetPost;

    private String text;

    public static Alarm of(AlarmType alarmType, User user, Post post) {
        return Alarm.builder()
                .alarmType(alarmType.name())
                .user(post.getUser())
                .fromUser(user)
                .targetPost(post)
                .text(alarmType.getMessage())
                .build();
    }
}
