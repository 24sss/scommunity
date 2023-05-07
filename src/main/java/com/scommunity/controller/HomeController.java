package com.scommunity.controller;

import com.scommunity.entity.DiscussPost;
import com.scommunity.entity.Page;
import com.scommunity.entity.User;
import com.scommunity.service.DiscussPostService;
import com.scommunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/6 14:28
 */
@Controller
public class HomeController {
    //controller调用service
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;


    //响应的是网页，不是数据，所以就不用requestbody
    //返回给前端的数据：model分装或者String（视图的名字）
    //接收前端传过来的分页有关的条件，用Page来封装
    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){
        //Page是页面的一些信息
        //算出数据的总数
        page.setRows(discussPostService.findDiscussPostRows(0));
        //设置路径
        page.setPath("/index");
        //还有一些值需要返回给页面的，page可以自动算出来：
        //当前页码，每一页显示多少条数据是页面传过来的，就不是服务器操心的事情了
        //页的起始行，总页数，从第几页到第几页，在Page类里已经定义好了逻辑

       //查询数据
       List<DiscussPost> list= discussPostService.findDiscussPosts(0,page.getOffset(),page.getLimit() );
       //存放帖子和用户名
       List<Map<String,Object>> discussPosts = new ArrayList<>();
       if(list!=null){
           //遍历每一个数据，并被帖子和用户装入集合
           for (DiscussPost post :
                  list ) {
               //存帖子和用户名
               Map<String,Object> map = new HashMap<>();
               map.put("post",post);
               //得到根据post得到userid，进而得到user完整数据
               User user = userService.findUserById(post.getUserId());
               map.put("user",user);
               //全部放在这个集合中
               discussPosts.add(map);
           }
       }
       //page应该在这里返回给页面：model.addAttribute("",page);
         /**
          * 但是这里可以省略
          * 方法调用前，SpringMVC会自动实例化Model和Page，并将Page注入Model
          * 所以在thymeleaf中可以直接访问Page对象中的数据，就不需要model.add..("page",page)
        */
       //把要给页面展示的结果写进Model中，页面才能得到
       model.addAttribute("discussPosts",discussPosts);
        //返回这个页面
        return "/index";
    }





}
