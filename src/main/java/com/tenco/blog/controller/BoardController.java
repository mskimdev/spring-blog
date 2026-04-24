package com.tenco.blog.controller;

import com.tenco.blog.model.Board;
import com.tenco.blog.repository.BoardNativeRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    // DI 처리
    private final BoardNativeRepository boardNativeRepository;

    // 생성자 주입 해도 되고, 위에 어노테이션 @RequredArgsConstructor 사용해도 됨.
//    public BoardController(BoardNativeRepository boardNativeRepository) {
//        this.boardNativeRepository = boardNativeRepository;
//    }

    @GetMapping("/board/save-form")
    public String saveForm() {
        return "board/save-form";
    }

    // 게시글 작성 기능 요청
    @PostMapping("/board/save")
    public String saveProc(
            @RequestParam("username") String username,
            @RequestParam("title") String title,
            @RequestParam("content") String content){
        log.info("username : " + username);
        log.info("title : " + title);
        log.info("content : " + content);

        // insert + 트랜잭션 처리
        boardNativeRepository.save(title, content, username);

        // redirect: 다시 URL 요청
        return "redirect:/";
    }

//    @GetMapping("/")
//    public String list(){
//        return "index";
//    }

    /**
     * 게시글 목록 화면 요청
     * @return 페이지 반환
     * 주소 설계 : http://localhost/
     */
    @GetMapping("/")
    public String list(Model mo){

        List<Board> boardList = boardNativeRepository.findAll();
        mo.addAttribute("boardList", boardList);
        return "board/list";
    }

}
