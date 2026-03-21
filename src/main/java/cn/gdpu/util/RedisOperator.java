package cn.gdpu.util;

import cn.gdpu.bean.SelectableCourse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * redis操作类
 */
@Component
public class RedisOperator{

    @Resource
    private RedisTemplate<String ,SelectableCourse> template;
    
    /**course**/
    public void setCourses(SelectableCourse course){
        //将course对象保存在缓存中
        template.opsForSet().add("course", course);
    }
    public SelectableCourse getCourses(){
        return template.opsForValue().get("course");
    }

    //更新缓存
    public void updateCourse(Integer key, SelectableCourse course){
        template.delete("course"+key);
        template.opsForValue().set("course"+key,course);
    }

    public Boolean infoisexist(Integer key){
        if(template.hasKey("course"+key)){
            return true;
        }
        return false;
    }
    
    //获取缓存集合。
    public Set<SelectableCourse> getList(){
        return template.opsForSet().members("course");
    }

    /**
     * 移除key
     */
    public void removeKey() {
        template.delete("course");
    }
}