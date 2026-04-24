package com.tenco.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    // 회원 가입 페이지 요청
    // 주소 : http://localhost/join-form
    @GetMapping("/join-form")
    public String signupPage() {

        // view resolver 동작
        // classPath : src/main/resources/templates/
        return "user/join-form";
    }

    // 로그인 화면 페이지 요청
    @GetMapping("login")
    public String loginPage() {
        return "user/login-form";
    }
}
