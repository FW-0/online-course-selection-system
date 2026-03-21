package cn.gdpu.config;

import cn.gdpu.bean.SelectableCourse;
import cn.gdpu.mapper.SelectableCourseDAO;
import cn.gdpu.util.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Set;

/**
 * @ClassName RedisCache
 * @Author ttaurus
 * @Date Create in 2020/3/11 17:03
 */
@Component
public class LoadListener implements CommandLineRunner{

    @Autowired
    private SelectableCourseDAO selectableCourseDAO;
    @Autowired
    RedisOperator redisOperator;


    public void init() {
        //系统启动中。。。加载codeMap
        List<SelectableCourse> codeList = selectableCourseDAO.selectCourses();
        for(SelectableCourse sc : codeList){
            redisOperator.setCourses(sc);
        }
        System.out.println("课程缓存完毕!");
        Set<SelectableCourse> redisCourses = redisOperator.getList();
        System.out.println("数据为:"+redisCourses);
    }

    @PreDestroy
    public void destroy() {
        //系统运行结束
        redisOperator.removeKey();
        System.out.println("已销毁缓存！");
    }
    
    @Override
    public void run(String... args) throws Exception{
        init();
    }



}
