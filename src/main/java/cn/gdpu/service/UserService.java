package cn.gdpu.service;

import cn.gdpu.bean.User;
import cn.gdpu.mapper.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName UserService
 * @Author ttaurus
 * @Date Create in 2020/3/4 19:32
 */
@Service
public class UserService{
    
    @Autowired
    UserDAO userDAO;

    public User login(String username){
        return userDAO.login(username);
    }
    
    public List<User> selectAll(){
        return userDAO.selectAll();
    }

    /**
     * 根据用户名获取用户信息
     */
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    /**
     * 更新密码
     */
    public void updatePassword(User user) {
        userDAO.updatePassword(user);
    }
}
