package com.tenco.spring_bblog.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardJpaRepository extends JpaRepository<Board, Long> {

    /**
     * save() : 저장 및 수정
     * findById() : ID로 단건 조회 (Optional)
     * findAll() : 전체 조회 (List)
     * deleteById() : ID로 삭제
     * count() : 전체 데이터 개수
     * existsById() : ID 존재 여부 확인 (boolean)
     */

    @Query("select b from Board b join fetch b.user u where b.id = :id")
    Optional<Board> findByIdJoinUser(@Param("id") Long id);
    // findByIdJoinUser에서 "id"에 넣은 id로 게시글을 조회했을 때, 조회 결과가 없을 수도 있으니까 Optional로 반환한다

    @Query("select b from Board b join fetch b.user u order by b.id desc")
    List<Board> findAllJoinUser();
}
