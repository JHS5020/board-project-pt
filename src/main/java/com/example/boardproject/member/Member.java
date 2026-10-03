package com.example.boardproject.member;

import com.example.boardproject.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    /**
     * @Enumerated(EnumType.STRING): DB에 enum을 저장할 때 "이름 그대로"(예: "USER") 저장.
     *      기본값인 EnumType.ORDINAL(순서 번호, 0/1/2...)을 쓰면
     *      나중에 enum 순서가 바뀌거나 중간에 값이 추가될 때 기존 데이터가 전부 틀어지는
     *      치명적인 버그가 생깁니다. 그래서 실무에서는 무조건 STRING을 씁니다.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Builder
    private Member(String username, String password, String nickname, String email, Role role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}