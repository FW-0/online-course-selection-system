package cn.gdpu.mapper;

import cn.gdpu.bean.SelectableCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName SelectableCourseDAO
 * @Author ttaurus
 * @Date Create in 2020/3/4 9:04
 */
@Mapper
@Repository
public interface SelectableCourseDAO{

    List<SelectableCourse> selectCourses();
    
    List<SelectableCourse> selectCoursesStocks(String username);
    
    //根据课程类别搜索
    List<SelectableCourse> selectCoursesByType(String type,String username);
    
    //根据课程所属学院名称搜索
    List<SelectableCourse> selectCoursesByCollege(String name,String username);
    
    //根据剩余人数搜索课程
    List<SelectableCourse> selectCourseByMemberCount(Integer start,Integer end,String username);
    
    //增加库存
    void updateAddCourseStock(Integer courseId);

    void updateMinCourseStock(Integer courseId);
    
    //获得库存
    Integer getStockByCourseId(Integer courseId);
    
    //根据名称模糊搜索
    List<SelectableCourse> selectByCourseName(String name,String username);
    
    //搜索出该学生已选的课程
    List<SelectableCourse> selectByUser(String username);
    
    //拿到所有已被选择的课程
    List<SelectableCourse> selectedCourses();
    
    //隐藏课程
    void hideBatch(Integer courseId);
    
    //添加课程
    void addCourse(@Param("courseName") String courseName, @Param("collegeId")Integer collegeId, @Param("type")String courseType,
                   @Param("teacher")String teacher, @Param("courseScore")Integer score, @Param("stock")Integer stock,
                   @Param("address")String address, @Param("description")String description);

    /**
     * 更新课程信息
     * @param selectableCourse 课程对象
     */
    void updateCourse(SelectableCourse selectableCourse);

    SelectableCourse getCourseById(Integer courseId);

    List<SelectableCourse> getAllCourses();

}
