package cn.gdpu.controller;

import cn.gdpu.bean.SelectableCourse;
import cn.gdpu.service.SelectableCourseService;
import cn.gdpu.util.Msg;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SelectableCourseController
 * @Author ttaurus
 * @Date Create in 2020/3/6 20:40
 */
@RestController
@RequestMapping("/course")
@Api
public class SelectableCourseController {

    @Autowired
    SelectableCourseService selectableCourseService;

    /**
     * 获得全部课程
     * @param page
     * @param limit
     * @param username
     * @return
     */
    @PostMapping("/getAll")
    public Object getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "limit", defaultValue = "10") int limit,
                         @RequestParam(value = "username", required = false) String username) {

        return selectableCourseService.selectAll(page, limit, username);
    }

    /**
     * 根据课程类别搜索课程
     * @param page
     * @param limit
     * @param username
     * @param type
     * @return
     */
    @PostMapping("/getCourseByType")
    public Object getCourseByType(@RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "limit", defaultValue = "10") int limit,
                                  @RequestParam(value = "username", required = false) String username,
                                  @RequestParam("courseType") String type) {
        return selectableCourseService.selectCoursesByType(page, limit, type, username);
    }

    /**
     * 根据课程所属学院名称搜索课程
     * @param page
     * @param limit
     * @param username
     * @param name
     * @return
     */
    @PostMapping("/getCourseByCollege")
    public Object getCourseByCollege(@RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "limit", defaultValue = "10") int limit,
                                     @RequestParam(value = "username", required = false) String username,
                                     @RequestParam("college") String name) {
        return selectableCourseService.selectCoursesByCollege(page, limit, name, username);
    }

    /**
     * 根据课程名称模糊搜索课程
     * @param page
     * @param limit
     * @param username
     * @param courseName
     * @return
     */
    @PostMapping("selectByCourseName")
    public Object selectByCourseName(@RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "limit", defaultValue = "10") int limit,
                                     @RequestParam(value = "username", required = false) String username,
                                     @RequestParam("courseName") String courseName) {
        return selectableCourseService.selectByCourseName(page, limit, courseName, username);
    }

    /**
     * 根据课程剩余人数查找课程
     * @param page
     * @param limit
     * @param username
     * @param count
     * @return
     */
    @PostMapping("selectCourseByMemberCount")
    public Object selectByMemberCount(@RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "limit", defaultValue = "10") int limit,
                                      @RequestParam(value = "username", required = false) String username,
                                      @RequestParam("count") Integer count) {
        return selectableCourseService.selectCoursesByMemberCount(page, limit, count, username);
    }

    /**
     * 隐藏课程
     * @param courseId
     * @return
     */
    @PostMapping("hideBatch")
    @PreAuthorize("hasAuthority('admin')")
    public Object hideBatch(Integer courseId) {
        try {
            return selectableCourseService.hideBatch(courseId);
        } catch (Exception e) {
            return Msg.msg("操作异常!");
        }
    }

    @PostMapping("/addCourseByAdmin")
    @PreAuthorize("hasAuthority('admin')")
    public Object addCourse(@RequestParam("courseName") String courseName,
                            @RequestParam("courseType") String courseType,
                            @RequestParam("collegeId") Integer collegeId,
                            @RequestParam("teacher") String teacher,
                            @RequestParam("score") Integer score,
                            @RequestParam("stock") Integer stock,
                            @RequestParam("address") String address,
                            @RequestParam(value = "description", defaultValue = "") String description) {
        try {
            return selectableCourseService.addCourse(courseName, collegeId, courseType, teacher, score, stock, address, description);
        } catch (Exception e) {
            e.printStackTrace();
            return Msg.msg("出现异常,添加课程失败!");
        }
    }

    @GetMapping("/editCourse")
    @PreAuthorize("hasAuthority('admin')")
    public String editCoursePage(@RequestParam("courseId") Integer id, Model model) {
        SelectableCourse course = selectableCourseService.getCourseById(id);
        if (course == null) {
            return "redirect:/courseManage";
        }

        model.addAttribute("course", course);
        return "editCourse";
    }

    @PostMapping("/updateCourse")
    @PreAuthorize("hasAuthority('admin')")
    @ResponseBody
    public Object updateCourse(@RequestBody SelectableCourse selectableCourse) {
        try {
            // 数据验证
            if (selectableCourse.getCourseName() == null || selectableCourse.getCourseName().trim().isEmpty()) {
                return Msg.msg("课程名称不能为空");
            }
            if (selectableCourse.getTeacher() == null || selectableCourse.getTeacher().trim().isEmpty()) {
                return Msg.msg("任课教师不能为空");
            }
            if (selectableCourse.getStock() == null || selectableCourse.getStock() <= 0) {
                return Msg.msg("课程容量必须大于0");
            }

            return selectableCourseService.updateCourse(selectableCourse);
        } catch (Exception e) {
            e.printStackTrace();
            return Msg.msg("修改课程失败!");
        }
    }
}