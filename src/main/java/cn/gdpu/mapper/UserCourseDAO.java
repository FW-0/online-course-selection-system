package cn.gdpu.mapper;

import cn.gdpu.bean.UserCourse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName UserCourseDAO
 * @Author ttaurus
 * @Date Create in 2020/3/4 9:11
 */
@Mapper
@Repository
public interface UserCourseDAO{
    
    List<Integer> selectByUsername(String username);
    
    Integer checkCourse(String username,Integer courseId);
    
    void insert(UserCourse userCourse);
    
    void delete(Integer courseId,String username);
    
    //拿到所有选择课程的数据
    List<UserCourse> selectAll();
    
}
