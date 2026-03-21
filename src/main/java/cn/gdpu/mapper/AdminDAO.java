package cn.gdpu.mapper;

import cn.gdpu.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @ClassName AdminDAO
 * @Author ttaurus
 * @Date Create in 2020/3/4 9:10
 */
@Mapper
@Repository
public interface AdminDAO{

    User login(String username); 
}
