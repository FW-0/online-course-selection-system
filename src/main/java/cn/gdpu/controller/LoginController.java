package cn.gdpu.controller;

import cn.gdpu.VO.ImgVO;
import cn.gdpu.bean.User;
import cn.gdpu.service.AdminService;
import cn.gdpu.service.UserService;
import cn.gdpu.util.Msg;
import cn.gdpu.util.UUIDUtil;
import cn.gdpu.util.VerifyCodeUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @ClassName AdminController
 * @Author ttaurus
 * @Date Create in 2020/3/4 19:28
 */
@Controller
@Api
public class LoginController{
    
    @Autowired
    AdminService adminService;
    @Autowired
    UserService userService;

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/")
    public String index() {
        return "success";
    }
    
    
    @GetMapping("/manager")
    @PreAuthorize("hasAuthority('admin')")
    public String manager(){
        return "manager";
    }
    //@RequestMapping("/error")
    //public String error(){
    //    return "error";
    //}
    
    @GetMapping("/info")
    @PreAuthorize("hasAuthority('student')")
    public String info(){
        return "studentInfo";
    }

    @GetMapping("/getCode")
    @ResponseBody
    public Object getCode(HttpServletRequest request) {

        /* 生成验证码字符串 */
        String verifyCode = VerifyCodeUtil.generateVerifyCode(4);
        String uuid = UUIDUtil.GeneratorUUIDOfSimple();
        
        HttpSession session = request.getSession();
        session.setAttribute(uuid,verifyCode); //将验证码与生成的uuid绑定在一起
        System.out.println("生成的验证码为:" + verifyCode);

        int width = 111,height = 36;

        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            VerifyCodeUtil.outputImage(width, height, stream, verifyCode);
            return Msg.msg("data",new ImgVO("data:image/gif;base64,"+ Base64Utils.encodeToString(stream.toByteArray()),uuid));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public User getUser() { //为了session从获取用户信息,可以配置如下
        User user = new User();
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails) user = (User) auth.getPrincipal();
        return user;
    }

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
