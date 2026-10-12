package com.tenco.spring_bblog.board;

import com.tenco.spring_bblog.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@Table(name = "board_tb")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

    @Id // 기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 값 자동 생성
    private Long id;
    private String title;
    private String content;
    @CreationTimestamp // now 사용 안 해도 자동으로 PC 날짜 주입
    private Timestamp createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Board (String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    // 트랜잭션이 없는 이유 - 트랜잭션이 적용된 곳에서 사용할 것임
    public void update(BoardRequest.UpdateDto updateDto) {
        updateDto.validate();

        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();
    }

    // 게시글 작성자가 현재 로그인한 사용자인지 확인하는 메서드
    public boolean isOwner(Long userId) {
        return this.user.getId().equals(userId);
    }
}
