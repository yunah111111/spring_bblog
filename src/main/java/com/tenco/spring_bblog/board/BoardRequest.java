package com.tenco.spring_bblog.board;

import com.tenco.spring_bblog._core.error.Exception400;
import com.tenco.spring_bblog.user.User;
import lombok.Data;

public class BoardRequest {

    @Data
    public static class SaveDto {
        private String title;
        private String content;

        public void validate() {
            if (title == null || title.trim().isEmpty()) {
                throw new Exception400("제목은 필수입니다.");
            }
            if (content == null || content.trim().isEmpty()) {
                throw new Exception400("내용은 필수입니다.");
            }
        }

        // Request DTO에 담긴 데이터를 Board 엔티티 객체로 바꿈
        public Board toEntity(User user) {
            return Board.builder()
                    .title(this.title)
                    .content(this.content)
                    .user(user)
                    .build();
        }
    }

    @Data
    public static class UpdateDto {
        private String title;
        private String content;

        public void validate() {
            if(title == null || title.trim().isEmpty()) {
                throw new Exception400("제목은 필수입니다.");
            }
            if(content == null || content.trim().isEmpty()) {
                throw new Exception400("내용은 필수입니다.");
            }
        }
    }


}
