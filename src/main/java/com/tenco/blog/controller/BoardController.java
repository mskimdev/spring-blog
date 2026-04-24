package com.tenco.blog.controller;

import com.tenco.blog.model.Board;
import com.tenco.blog.repository.BoardNativeRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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


    // 게시글 상세보기 화면 요청
    // http://localhost/board/1
    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Integer id, Model mo){
        mo.addAttribute("board", boardNativeRepository.findById(id));


        return "board/detail";
    }


    // 게시물 삭제 요청
    // post -> redirect -> get 흐름
    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable(name="id") Integer id){
        if(boardNativeRepository.deleteById(id)){
            // PRG 패턴 (Post Redirect Get) 적용
            return "redirect:/";
        } else{
            return "redirect:/board/{id}";
        }
    }

    // 게시물 수정 시 게시물 수정 페이지로
    @GetMapping("/board/{id}/update-form")
    public String updateForm(@PathVariable(name="id") Integer id, Model mo){
        // 사용자에게 해당 게시물 내용을 보여줘야 한다.
        mo.addAttribute("board", boardNativeRepository.findById(id));
        return "board/update-form";
    }

    @PostMapping("/board/{id}/update-form")
    public String updateProc(
            @PathVariable("id") Integer id,
            @RequestParam("title") String title,
            @RequestParam("content") String content){

        if(boardNativeRepository.updateById(title, content, id)){
            // redirect : view resolver 동작이 아닌
            // 그냥 새로운 http get 요청임
            return "redirect:/board/" + id;
        }
        else {
            return "redirect:/board/" + id + "/update-form";
        }
    }
}
