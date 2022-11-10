package com.sparta.actualpractice.entity;

import com.sparta.actualpractice.dto.request.MemberReqeustDto;
import com.sparta.actualpractice.util.TimeStamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Member extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column
    private String statusMessage;

    @Column
    private String imageUrl;


    @OneToMany(mappedBy = "member")
    private List<Schedule> scheduleList;

    @OneToMany(mappedBy = "member")
    private List<Album> albumList;

    @OneToMany(mappedBy = "member")
    private List<Comment> commentList;

    @OneToMany(mappedBy = "member")
    private List<MemberParty> memberPartyList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOTIFICATION_ID")
    private Notification notification;

    @OneToMany(mappedBy = "member")
    private List<Admin> adminList;

    public Member(MemberReqeustDto memberReqeustDto, String password) {
        this.email = memberReqeustDto.getEmail();
        this.password = password;
        this.name = memberReqeustDto.getName();
    }

}