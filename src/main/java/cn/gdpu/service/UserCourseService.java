package cn.gdpu.service;

import cn.gdpu.bean.SelectableCourse;
import cn.gdpu.bean.UserCourse;
import cn.gdpu.mapper.SelectableCourseDAO;
import cn.gdpu.mapper.UserCourseDAO;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @ClassName UserCourseService
 * @Author ttaurus
 * @Date Create in 2020/3/9 14:38
 */
@Service
public class UserCourseService{

    @Autowired
    SelectableCourseDAO selectableCourseDAO;
    @Autowired
    UserCourseDAO userCourseDAO;

    /**
     * 退选课程
     * @param courseId
     * @param username
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object cancelCourse(Integer courseId,String username){
        Map<String ,Object> map= new HashMap<>();
        try{
            if(userCourseDAO.checkCourse(username,courseId) == null){  //判断是否已选过该课程
                map.put("msg","你已退选该课程!");
                map.put("flag",false);
                return JSON.toJSON(map);
            }
            selectableCourseDAO.updateAddCourseStock(courseId);
            userCourseDAO.delete(courseId,username);
            map.put("msg","退课成功!");
            map.put("flag",true);
            return JSON.toJSON(map);
        }catch(Exception e){
            System.out.println("退选异常！已回滚操作");
            throw e;
        }
    }

    /**
     * 选择课程
     * @param courseId
     * @param username
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object chooseCourse(Integer courseId,String username){
        Map<String ,Object> map= new HashMap<>();
        try{
            Integer stock = selectableCourseDAO.getStockByCourseId(courseId);
            if(stock <= 0){  //判断课程是否已经无剩余人数
                map.put("msg","该课程已被选完啦!");
                map.put("flag",false);
                return JSON.toJSON(map);
            }
            List<Integer> ids = userCourseDAO.selectByUsername(username);
            if(ids.contains(courseId)){  //判断是否已选过该课程
                map.put("msg","你已选择该课程,请勿重复选择!");
                map.put("flag",false);
                return JSON.toJSON(map);
            }
            if(ids.size() >= 3){
                map.put("msg","每个人至多选择3门课程!");
                map.put("flag",false);
                return JSON.toJSON(map);
            }
            selectableCourseDAO.updateMinCourseStock(courseId); //如果出异常 必定这个地方! 
            UserCourse course = new UserCourse();
            Date date = new Date();
            course.setCourseId(courseId);
            course.setUsername(username);
            course.setSelectTime(date);
            userCourseDAO.insert(course);
            map.put("msg","选课成功!");
            map.put("flag",true);
            return JSON.toJSON(map);
        }catch(Exception e){
            System.out.println("系统繁忙,请稍后再进行尝试!");
            throw e;
        }
    }

    /**
     * 拿到该学生的全部课程
     * @param username
     * @return
     */
    public List<SelectableCourse> selectByUser(String username){
        try{
            return selectableCourseDAO.selectByUser(username);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 获取非冲突课程
     * @param username 学生用户名
     * @return 非冲突课程列表
     */
    public List<SelectableCourse> getNonConflictCourses(String username) {
        try {
            // 1. 获取用户已选课程ID（用于标记flag）
            List<Integer> selectedCourseIds = userCourseDAO.selectByUsername(username);

            // 2. 获取用户已选课程的时间列表（用于冲突判断）
            List<SelectableCourse> selectedCourses = selectableCourseDAO.selectByUser(username);
            List<String> selectedCourseTimes = selectedCourses.stream()
                    .map(SelectableCourse::getAddress)
                    .collect(Collectors.toList());

            // 3. 获取所有课程
            List<SelectableCourse> allCourses = selectableCourseDAO.getAllCourses();

            // 4. 筛选非冲突课程，并标记flag
            return allCourses.stream()
                    .filter(course -> !isTimeConflict(course.getAddress(), selectedCourseTimes))
                    .peek(course -> {
                        course.setFlag(selectedCourseIds.contains(course.getCourseId()));
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * 检查课程是否冲突
     * @param username 学生用户名
     * @param courseId 课程 ID
     * @param courseTime 课程时间
     * @return 是否冲突
     */
    public boolean checkCourseConflict(String username, Integer courseId, String courseTime) {
        List<SelectableCourse> selectedCourses = selectableCourseDAO.selectByUser(username);
        List<String> selectedCourseTimes = selectedCourses.stream()
                .filter(course -> !course.getCourseId().equals(courseId))
                .map(SelectableCourse::getAddress)
                .collect(Collectors.toList());

        return isTimeConflict(courseTime, selectedCourseTimes);
    }

    /**
     * 判断时间是否冲突
     * @param time 待检查的时间
     * @param selectedTimes 已选课程的时间列表
     * @return 是否冲突
     */
    private boolean isTimeConflict(String time, List<String> selectedTimes) {
        Pattern pattern = Pattern.compile("(星期[一二三四五六日])第(\\d+-\\d+)节");
        Matcher matcher = pattern.matcher(time);
        if (matcher.find()) {
            String day = matcher.group(1);
            String period = matcher.group(2);
            for (String selectedTime : selectedTimes) {
                Matcher selectedMatcher = pattern.matcher(selectedTime);
                if (selectedMatcher.find()) {
                    String selectedDay = selectedMatcher.group(1);
                    String selectedPeriod = selectedMatcher.group(2);
                    if (day.equals(selectedDay) && period.equals(selectedPeriod)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
