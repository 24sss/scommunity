package com.scommunity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/6 18:48
 */
@Controller
public class k {
    @RequestMapping("/x")
    @ResponseBody
    public String sdf(){
        return "sdvf";
    }
}
