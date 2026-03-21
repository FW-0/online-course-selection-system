package cn.gdpu.controller;

import cn.gdpu.bean.User;
import cn.gdpu.service.UserService;
import cn.gdpu.util.Msg;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserController
 * @Author ttaurus
 * @Date Create in 2020/3/10 13:36
 */
@RestController
@RequestMapping("user")
@Api
public class UserController{

    @Autowired
    UserService userService;

    /**
     * 获取全部用户
     * @param page
     * @param limit
     * @return
     */
    @PostMapping("getUsers")
    @PreAuthorize("hasAuthority('admin')")
    public Object getAll(@RequestParam(value = "page", defaultValue = "1") int page ,
                         @RequestParam(value = "limit", defaultValue = "10") int limit ){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(page , limit);
        List<User> list = userService.selectAll();
        if(list == null){
            return Msg.fail();
        }
        //System.out.println("=="+username+"==");
        PageInfo<User> pageInfo = new PageInfo<>(list);
        map.put("totalPage" , pageInfo.getPages());  //总页数
        map.put("totalCount" , pageInfo.getTotal());  //总条数
        map.put("currentPage" , page);  //当前页数。
        map.put("data" , pageInfo.getList()); //获得的数据量
        return JSON.toJSON(map);
    }

    /**
     * 获取用户个人信息
     */
    @PostMapping("getUserInfo")
    @PreAuthorize("hasAuthority('student')")
    public User getUserInfo(@RequestParam String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping("changePassword")
    @PreAuthorize("hasAuthority('student')")
    public Msg changePassword(@RequestParam String username,
                              @RequestParam String oldPassword,
                              @RequestParam String newPassword) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return Msg.fail("用户不存在");
        }

        // 关键修复：使用BCrypt验证原密码（密文与明文比对）
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(oldPassword, user.getPassword())) { // 注意参数顺序：明文在前，密文在后
            return Msg.fail("原密码错误");
        }

        // 新密码加密后存储
        String encodedNewPassword = encoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        userService.updatePassword(user);
        return Msg.success("密码修改成功");
    }
}
