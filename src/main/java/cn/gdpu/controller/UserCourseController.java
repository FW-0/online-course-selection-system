package cn.gdpu.controller;

import cn.gdpu.bean.SelectableCourse;
import cn.gdpu.mapper.SelectableCourseDAO;
import cn.gdpu.service.UserCourseService;
import cn.gdpu.util.Msg;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserCourseController
 * @Author ttaurus
 * @Date Create in 2020/3/9 15:08
 */
@RestController
@RequestMapping("usercourse")
@Api
public class UserCourseController{

    @Autowired
    UserCourseService userCourseService;
    @Autowired
    SelectableCourseDAO selectableCourseDAO;

    /**
     * 选课
     * @param courseId
     * @param username
     * @return
     */
    @PostMapping("choose")
    @PreAuthorize("hasAuthority('student')")
    public Object chooseCourse(@RequestParam("courseId") Integer courseId ,
                               @RequestParam("username") String username){
        Map<String,Object> map = new HashMap<>();
        try{
            return userCourseService.chooseCourse(courseId , username);
        }catch(Exception e){
            if(e instanceof DataIntegrityViolationException){
                map.put("msg","该课程已经被抢完啦。");
            }else{
                map.put("msg","出现其他异常,选课失败!");
            }
            map.put("flag",false);
            return JSON.toJSON(map);
        }
    }

    /**
     * 退课
     * @param courseId
     * @param username
     * @return
     */
    @PostMapping("cancel")
    @PreAuthorize("hasAuthority('student')")
    public Object cancelCourse(@RequestParam("courseId") Integer courseId ,
                               @RequestParam("username") String username){
        return userCourseService.cancelCourse(courseId,username);
    }

    /**
     * 获取学生所选全部课程
     * @param page
     * @param limit
     * @param username
     * @return
     */
    @PostMapping("studentInfo")
    @PreAuthorize("hasAuthority('admin') or hasAuthority('student')")
    public Object studentInfo(@RequestParam(value = "page", defaultValue = "1") int page ,
                              @RequestParam(value = "limit", defaultValue = "10") int limit ,
                              @RequestParam("username")String username){
        try{
            Map<String,Object> map = new HashMap<>();
            PageHelper.startPage(page , limit);
            List<SelectableCourse> list = selectableCourseDAO.selectByUser(username);
            if(list == null){
                return Msg.fail();
            }
            //System.out.println("=="+username+"==");
            PageInfo<SelectableCourse> pageInfo = new PageInfo<>(list);
            map.put("totalPage" , pageInfo.getPages());  //总页数
            map.put("totalCount" , pageInfo.getTotal());  //总条数
            map.put("currentPage" , page);  //当前页数。
            map.put("data" , pageInfo.getList()); //获得的数据量
            map.put("tCase",username);
            return JSON.toJSON(map);
        }catch(Exception e){
            e.printStackTrace();
            return Msg.fail();
        }
    }
    
    //测试。
    @PostMapping("cc")
    public Object cc(){
        Map<String,Object> map = new HashMap<>();
        try{
            selectableCourseDAO.updateMinCourseStock(1);
            return true;
        }catch(Exception e){
            if(e instanceof DataIntegrityViolationException){
                map.put("msg","该课程已经被抢完啦。");
            }else{
                map.put("msg","出现其他异常,选课失败!");
            }
            map.put("flag",false);
            return JSON.toJSON(map);
        }
    }
    /**
     * 获取学生已选课程（用于课表展示）
     * @param username 学生用户名
     * @return 包含课程信息和总学分的结果
     */
    @PostMapping("selectedCoursesForTimetable")
    @PreAuthorize("hasAuthority('student')")
    public Object getSelectedCoursesForTimetable(@RequestParam("username") String username) {
        try {
            // 调用服务层获取学生已选课程
            List<SelectableCourse> selectedCourses = userCourseService.selectByUser(username);
            Map<String, Object> result = new HashMap<>();

            if (selectedCourses == null || selectedCourses.isEmpty()) {
                result.put("courses", new ArrayList<>());
                result.put("totalCredit", 0);
                return JSON.toJSON(result);
            }

            // 计算总学分（使用实体类已有属性 courseScore）
            int totalCredit = selectedCourses.stream()
                    .mapToInt(course -> course.getCourseScore() != null ? course.getCourseScore() : 0)
                    .sum();

            result.put("courses", selectedCourses); // 课程列表（包含课程名、教师名等）
            result.put("totalCredit", totalCredit); // 总学分
            return JSON.toJSON(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("msg", "获取课程信息失败");
            error.put("flag", false);
            return JSON.toJSON(error);
        }
    }
    /**
     * 获取非冲突课程
     * @param username 学生用户名
     * @return 非冲突课程信息
     */
    @PostMapping("getNonConflictCourses")
    @PreAuthorize("hasAuthority('student')")
    public Object getNonConflictCourses(
            @RequestParam("username") String username,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "8") int limit) {
        try {
            Map<String, Object> map = new HashMap<>();

            // 1. 获取所有非冲突课程
            List<SelectableCourse> allNonConflictCourses = userCourseService.getNonConflictCourses(username);

            // 2. 手动实现内存分页
            int totalCount = allNonConflictCourses.size();
            int totalPage = (int) Math.ceil((double) totalCount / limit);

            // 计算分页范围
            int fromIndex = (page - 1) * limit;
            int toIndex = Math.min(fromIndex + limit, totalCount);
            List<SelectableCourse> pagedCourses = allNonConflictCourses.subList(fromIndex, toIndex);

            map.put("totalPage", totalPage);
            map.put("totalCount", totalCount);
            map.put("currentPage", page);
            map.put("data", pagedCourses);
            map.put("tCase", username);
            return JSON.toJSON(map);
        } catch (Exception e) {
            e.printStackTrace();
            return Msg.fail();
        }
    }

    /**
     * 检查课程是否冲突
     * @param username 学生用户名
     * @param courseId 课程 ID
     * @param courseTime 课程时间
     * @return 冲突检查结果
     */
    @PostMapping("checkCourseConflict")
    @PreAuthorize("hasAuthority('student')")
    public Object checkCourseConflict(@RequestParam("username") String username,
                                      @RequestParam("courseId") Integer courseId,
                                      @RequestParam("courseTime") String courseTime) {
        try {
            boolean conflict = userCourseService.checkCourseConflict(username, courseId, courseTime);
            Map<String, Object> result = new HashMap<>();
            result.put("conflict", conflict);
            return JSON.toJSON(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("msg", "检查课程冲突失败");
            error.put("flag", false);
            return JSON.toJSON(error);
        }
    }
}


