package cn.gdpu.service;

import cn.gdpu.bean.SelectableCourse;
import cn.gdpu.config.LoadListener;
import cn.gdpu.mapper.SelectableCourseDAO;
import cn.gdpu.mapper.UserCourseDAO;
import cn.gdpu.util.Msg;
import cn.gdpu.util.RedisOperator;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


/**
 * @ClassName SelectableCourseService
 * @Author ttaurus
 * @Date Create in 2020/3/6 20:38
 */
@Service
public class SelectableCourseService{

    @Resource
    @Autowired
    SelectableCourseDAO selectableCourseDAO;
    @Autowired
    UserCourseDAO userCourseDAO;
    @Autowired
    RedisOperator redisOperator; //操作缓存对象。
    @Autowired
    LoadListener loadListener;

    /**
     * 搜索全部课程
     * @param username
     * @return
     */
    public Object selectAll(Integer page , Integer limit , String username){
        try{
            Map<String,Object> map = new HashMap<>();
            PageHelper.startPage(page , limit);
            Set<SelectableCourse> redisCourses = redisOperator.getList();
            System.out.println("缓存中有数据!\n");
            List<SelectableCourse> courses = new ArrayList<>();
            if(redisCourses.size() == 0){
                loadListener.init();
                redisCourses = redisOperator.getList();
            }
            List<SelectableCourse> coursesIds = selectableCourseDAO.selectCoursesStocks(username);
            PageInfo<SelectableCourse> pageInfo = new PageInfo<>(coursesIds);
            
            for(SelectableCourse sc : pageInfo.getList()){
                for(SelectableCourse rsc : redisCourses){
                    if(sc.getCourseId().equals(rsc.getCourseId())){
                        rsc.setStock(sc.getStock());
                        rsc.setIsHide(sc.getIsHide());
                        courses.add(rsc);
                        break;
                    }
                }
            }
            //此处 如果不是admin访问 则是学生访问 所以需要进一步判断学生已选的课程与需要展示的课程是否一致
            //一致则赋予true标志 否则为false标志
            if(!"admin".equals(username)){
                List<Integer> integers = userCourseDAO.selectByUsername(username);
                if(integers.size() == 0){
                    map.put("totalPage" , pageInfo.getPages());  //总页数
                    map.put("totalCount" , pageInfo.getTotal());  //总条数
                    map.put("currentPage" , page);  //当前页数。
                    map.put("data" , courses); //获得的数据量
                    return JSON.toJSON(map);
                }

                for(SelectableCourse course : courses){
                    for(Integer id : integers){
                        if(course.getCourseId().equals(id)){
                            course.setFlag(true);
                            break;
                        }
                    }
                }
            }
            map.put("totalPage" , pageInfo.getPages());  //总页数
            map.put("totalCount" , pageInfo.getTotal());  //总条数
            map.put("currentPage" , page);  //当前页数。
            map.put("data" , courses); //获得的数据量
            System.out.println(courses);
            return JSON.toJSON(map);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 根据课程类别搜索课程
     * @param type
     * @param username
     * @return
     */
    public Object selectCoursesByType(Integer page , Integer limit ,String type , String username){
        try{
            Map<String,Object> map = new HashMap<>();
            PageHelper.startPage(page , limit);
            Set<SelectableCourse> redisCourses = redisOperator.getList();
            System.out.println("缓存中有数据!\n");
            if(redisCourses.size() == 0){
                loadListener.init();
                redisCourses = redisOperator.getList();
            }
            List<SelectableCourse> courses = new ArrayList<>();
            List<SelectableCourse> types = selectableCourseDAO.selectCoursesByType(type,username);
            PageInfo<SelectableCourse> pageInfo = new PageInfo<>(types);
            for(SelectableCourse sc : pageInfo.getList()){
                for(SelectableCourse rsc : redisCourses){
                    if(sc.getCourseId().equals(rsc.getCourseId())){
                        rsc.setStock(sc.getStock());
                        rsc.setIsHide(sc.getIsHide());
                        courses.add(rsc);
                        break;
                    }
                }
            }
            //此处 如果不是admin访问 则是学生访问 所以需要进一步判断学生已选的课程与需要展示的课程是否一致
            //一致则赋予true标志 否则为false标志
            if(!"admin".equals(username)){
                List<Integer> integers = userCourseDAO.selectByUsername(username);
                if(integers.size() == 0){
                    map.put("totalPage" , pageInfo.getPages());  //总页数
                    map.put("totalCount" , pageInfo.getTotal());  //总条数
                    map.put("currentPage" , page);  //当前页数。
                    map.put("data" , courses); //获得的数据量
                    map.put("tCase" , type);
                    return JSON.toJSON(map);
                }
                for(SelectableCourse course : courses){
                    for(Integer id : integers){
                        if(course.getCourseId().equals(id)){
                            course.setFlag(true);
                            break;
                        }
                    }
                }
            }
            map.put("totalPage" , pageInfo.getPages());  //总页数
            map.put("totalCount" , pageInfo.getTotal());  //总条数
            map.put("currentPage" , page);  //当前页数。
            map.put("data" , courses); //获得的数据量
            map.put("tCase",type);
            return JSON.toJSON(map);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据课程所属学院搜索课程
     * @param name
     * @param username
     * @return
     */
    public Object selectCoursesByCollege(Integer page , Integer limit ,String name , String username){
        try{
            Map<String,Object> map = new HashMap<>();
            PageHelper.startPage(page , limit);
            Set<SelectableCourse> redisCourses = redisOperator.getList();
            System.out.println("缓存中有数据!\n");
            if(redisCourses.size() == 0){
                loadListener.init();
                redisCourses = redisOperator.getList();
            }
            List<SelectableCourse> courses = new ArrayList<>();
            List<SelectableCourse> colleges = selectableCourseDAO.selectCoursesByCollege(name,username);
            PageInfo<SelectableCourse> pageInfo = new PageInfo<>(colleges);
            for(SelectableCourse sc : pageInfo.getList()){
                for(SelectableCourse rsc : redisCourses){
                    if(sc.getCourseId().equals(rsc.getCourseId())){
                        rsc.setStock(sc.getStock());
                        rsc.setIsHide(sc.getIsHide());
                        courses.add(rsc);
                        break;
                    }
                }
            }
            //此处 如果不是admin访问 则是学生访问 所以需要进一步判断学生已选的课程与需要展示的课程是否一致
            //一致则赋予true标志 否则为false标志
            if(!"admin".equals(username)){
                List<Integer> integers = userCourseDAO.selectByUsername(username);
                if(integers.size() == 0){
                    map.put("totalPage" , pageInfo.getPages());  //总页数
                    map.put("totalCount" , pageInfo.getTotal());  //总条数
                    map.put("currentPage" , page);  //当前页数。
                    map.put("data" , courses); //获得的数据量
                    map.put("tCase" , name);
                    return JSON.toJSON(map);
                }
                for(SelectableCourse course : courses){
                    for(Integer id : integers){
                        if(course.getCourseId().equals(id)){
                            course.setFlag(true);
                            break;
                        }
                    }
                }
            }
            map.put("totalPage" , pageInfo.getPages());  //总页数
            map.put("totalCount" , pageInfo.getTotal());  //总条数
            map.put("currentPage" , page);  //当前页数。
            map.put("data" , courses); //获得的数据量
            map.put("tCase",name);
            return JSON.toJSON(map);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据课程剩余人数查找课程
     * @param count
     * @param username
     * @return
     */
    public Object selectCoursesByMemberCount(Integer page , Integer limit ,Integer count , String username){
        try{
            Map<String,Object> map = new HashMap<>();
            PageHelper.startPage(page , limit);
            Set<SelectableCourse> redisCourses = redisOperator.getList();
            System.out.println("缓存中有数据!\n");
            if(redisCourses.size() == 0){
                loadListener.init();
                redisCourses = redisOperator.getList();
            }
            List<SelectableCourse> courses = new ArrayList<>();
            List<SelectableCourse> members;
            if(count == 50){
                members = selectableCourseDAO.selectCourseByMemberCount(count , 500,username);
            }else if(count == 30){
                members = selectableCourseDAO.selectCourseByMemberCount(30 , 50,username);
            }else if(count == 10){
                members = selectableCourseDAO.selectCourseByMemberCount(10 , 30,username);
            }else{
                members = selectableCourseDAO.selectCourseByMemberCount(0 , 10,username);
            }
            PageInfo<SelectableCourse> pageInfo = new PageInfo<>(members);
            for(SelectableCourse sc : pageInfo.getList()){
                for(SelectableCourse rsc : redisCourses){
                    if(sc.getCourseId().equals(rsc.getCourseId())){
                        rsc.setStock(sc.getStock());
                        rsc.setIsHide(sc.getIsHide());
                        courses.add(rsc);
                        break;
                    }
                }
            }
            //此处 如果不是admin访问 则是学生访问 所以需要进一步判断学生已选的课程与需要展示的课程是否一致
            //一致则赋予true标志 否则为false标志
            if(!"admin".equals(username)){
                List<Integer> integers = userCourseDAO.selectByUsername(username);
                if(integers.size() == 0){
                    map.put("totalPage" , pageInfo.getPages());  //总页数
                    map.put("totalCount" , pageInfo.getTotal());  //总条数
                    map.put("currentPage" , page);  //当前页数。
                    map.put("data" , courses); //获得的数据量
                    map.put("tCase" , count);
                    return JSON.toJSON(map);
                }
                for(SelectableCourse course : courses){
                    for(Integer id : integers){
                        if(course.getCourseId().equals(id)){
                            course.setFlag(true);
                            break;
                        }
                    }
                }
            }
            map.put("totalPage" , pageInfo.getPages());  //总页数
            map.put("totalCount" , pageInfo.getTotal());  //总条数
            map.put("currentPage" , page);  //当前页数。
            map.put("data" , courses); //获得的数据量
            map.put("tCase",count);
            return JSON.toJSON(map);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据课程名称模糊搜索课程
     * @param courseName
     * @param username
     * @return
     */
    public Object selectByCourseName(Integer page , Integer limit ,String courseName , String username){
        try{
            Map<String,Object> map = new HashMap<>();
            PageHelper.startPage(page , limit);
            Set<SelectableCourse> redisCourses = redisOperator.getList();
            System.out.println("缓存中有数据!\n");
            if(redisCourses.size() == 0){
                loadListener.init();
                redisCourses = redisOperator.getList();
            }
            List<SelectableCourse> courses = new ArrayList<>();
            List<SelectableCourse> names = selectableCourseDAO.selectByCourseName(courseName,username);
            PageInfo<SelectableCourse> pageInfo = new PageInfo<>(names);
            for(SelectableCourse sc : pageInfo.getList()){
                for(SelectableCourse rsc : redisCourses){
                    if(sc.getCourseId().equals(rsc.getCourseId())){
                        rsc.setStock(sc.getStock());
                        rsc.setIsHide(sc.getIsHide());
                        courses.add(rsc);
                        break;
                    }
                }
            }
            //此处 如果不是admin访问 则是学生访问 所以需要进一步判断学生已选的课程与需要展示的课程是否一致
            //一致则赋予true标志 否则为false标志
            if(!"admin".equals(username)){
                List<Integer> integers = userCourseDAO.selectByUsername(username);
                if(integers.size() == 0){
                    map.put("totalPage" , pageInfo.getPages());  //总页数
                    map.put("totalCount" , pageInfo.getTotal());  //总条数
                    map.put("currentPage" , page);  //当前页数。
                    map.put("data" , courses); //获得的数据量
                    map.put("tCase" , courseName);
                    return JSON.toJSON(map);
                }
                for(SelectableCourse course : courses){
                    for(Integer id : integers){
                        if(course.getCourseId().equals(id)){
                            course.setFlag(true);
                            break;
                        }
                    }
                }
            }
            map.put("totalPage" , pageInfo.getPages());  //总页数
            map.put("totalCount" , pageInfo.getTotal());  //总条数
            map.put("currentPage" , page);  //当前页数。
            map.put("data" , courses); //获得的数据量
            map.put("tCase",courseName);
            return JSON.toJSON(map);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 隐藏课程
     * @param courseId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object hideBatch(Integer courseId){
        try{
            selectableCourseDAO.hideBatch(courseId);
            return Msg.ok();
        }catch(Exception e){
            throw e;
        }
    } 
    
    @Transactional(rollbackFor = Exception.class)
    public Object addCourse(String courseName,Integer collegeId,String collegeType,String teacher,Integer score,Integer stock,
                            String address,String description){
        selectableCourseDAO.addCourse(courseName,collegeId,collegeType,teacher,score,stock,address,description);
        loadListener.init();
        return Msg.ok();
    }
    /**
     * 更新课程信息
     * @param selectableCourse 课程对象
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Object updateCourse(SelectableCourse selectableCourse) {
        try {
            // 检查课程是否存在
            SelectableCourse existingCourse = selectableCourseDAO.getCourseById(selectableCourse.getCourseId());
            if (existingCourse == null) {
                return Msg.msg("课程不存在");
            }

            // 更新课程信息
            selectableCourseDAO.updateCourse(selectableCourse);
            // 更新缓存
            loadListener.init();
            return Msg.ok();
        } catch (Exception e) {
            throw e;
        }
    }

    public SelectableCourse getCourseById(Integer courseId) {
        return selectableCourseDAO.getCourseById(courseId);
    }
}
