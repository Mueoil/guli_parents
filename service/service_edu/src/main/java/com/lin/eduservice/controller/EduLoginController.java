package com.lin.eduservice.controller;

import com.lin.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduservice/user")
//解决跨域问题，加一个注解
//@CrossOrigin
public class EduLoginController {

//    关于vue-admin-template底层写两个方法
//    login
        @PostMapping("/login")
    public R login(){
//            这些参数是底层决定的
            return R.ok().data("token","admin");
        }

//    info
    @GetMapping("/info")
    public R info(){

            return R.ok().data("roles","[admin]").data("name","admin").data("avatar","https://th.bing.com/th/id/OIP.roVB1_XX6ZNds-ppqG2UfwHaFj?w=218&h=180&c=7&r=0&o=5&dpr=1.25&pid=1.7");
    }
}
