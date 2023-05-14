package com.scommunity.controller;

import com.scommunity.entity.User;
import com.scommunity.service.UserService;
import com.scommunity.util.CommunityUtil;
import com.scommunity.util.HstHolder;
import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/14 16:17
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**长传路径（保存在这里）
     * */
    @Value("${community.path.upload}")
    private String uploadPath;

    /**域名
     * */
    @Value("${community.path.domain}")
    private String domain;

    /**项目访问路径
     * */
    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**修改头像的service
     * */
    @Autowired
    private UserService userService;

    /**修改谁的头像——当前用户的
     * 从hstHolder里取
     * */
    private HstHolder hstHolder;



    /**点击设置
     * */
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }

    /**长传文件
     *MultipartFile :接收前端传过来的头像文件
     *Model:返回给前端一些数据
     * */
    @RequestMapping(path = "upload",method = RequestMethod.POST)
    public String uploadHeadler(MultipartFile headerImage, Model model) {
        if(headerImage==null){
            model.addAttribute("error","您还没有返回图片");
            return "/site/setting";
        }
        /**读取得到的文件的后缀
         * */
        String filename = headerImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("errpr","文件格式不正确");
            return "/site/setting";
        }

        /**生成随机文件名
         * */
        String s = CommunityUtil.generateUUID()+suffix;
        /**确定文件的存放路径
         * */
        File file = new File(uploadPath+"/"+filename);
        try {
            //存储文件
            headerImage.transferTo(file);
        } catch (IOException e) {
          logger.error("上传文件失败"+e.getMessage());
          throw new RuntimeException("上传文件失败",e);
        }
        /**更新当前头像的路径（Web路径）
         * http://localhost:8080/sss/user/header/xxx.png
         * 先获取当前用户
         * */
        User user = hstHolder.getUser();
        String headerUrl = domain+contextPath+"/user/header"+filename;
        userService.updateHeader(user.getId(),headerUrl);

        return "redirect:/index";

    }

    /**获取头像
     * */
    @RequestMapping(path = "")






}












