package cn.gdpu.mapper;

import cn.gdpu.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName UserDAO
 * @Author ttaurus
 * @Date Create in 2020/3/4 9:10
 */
@Mapper
@Repository
public interface UserDAO{

    User login(String username);
    
    List<User> selectAll();

    User getUserByUsername(String username);

    void updatePassword(User user);
}
