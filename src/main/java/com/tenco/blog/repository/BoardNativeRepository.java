package com.tenco.blog.repository;

import com.tenco.blog.model.Board;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // IoC + DI
@RequiredArgsConstructor
public class BoardNativeRepository {

    // EntityManager : JPA 핵심 인터페이스
    // 데이터베이스와 모든 작업을 담당
    private final EntityManager em;

    // DI - 생성자 의존 주입
//    public BoardNativeRepository(EntityManager em) {
//        this.em = em;
//    }

    // 트랜잭션 처리
    @Transactional
    public void save(String title, String content, String username) {
        Query query = em.createNativeQuery("insert into board_tb(title, content, username, created_at) values(?, ?, ?, now())");
        query.setParameter(1, title);
        query.setParameter(2, content);
        query.setParameter(3, username);

        query.executeUpdate();
    }

    public List<Board> findAll() {
        String sql = """
                select * from board_tb order by id desc
                """;

        Query query = em.createNativeQuery(sql, Board.class);
        return query.getResultList();
    }

    // 게시글 상세 보기 (특정 ID로 조회)
    public Board findById(Integer id) {

        String strQuery = """
                SELECT * FROM board_tb WHERE id = ?
                """;

        try {
            Query query = em.createNativeQuery(strQuery, Board.class);
            query.setParameter(1, id);
            return (Board) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    // 특정 게시글 삭제 요청
    // 정합성 팬텀리드? -> select나 단일 delete에도 transaction 달아주는 이유 . --> blog
    @Transactional
    public boolean deleteById(Integer id) {
        try {
            Query query = em.createNativeQuery("delete from board_tb where id = ?");
            query.setParameter(1, id);
            return query.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // 게시글 수정 시 --> 다시 사용자가 게시글 작성할 수 있도록 설계
    // /board/{{board.id}}/update-form
    @Transactional
    public boolean updateById(String title, String content, Integer id) {
        String sql = """
                UPDATE board_tb SET title = ?, content = ? where id = ?
                """;

        Query query = em.createNativeQuery(sql);
        query.setParameter(1, title);
        query.setParameter(2, content);
        query.setParameter(3, id);

        return query.executeUpdate() > 0;
    }
}
