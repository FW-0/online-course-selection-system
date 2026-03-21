package cn.gdpu.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName UserCourse
 * @Author ttaurus
 * @Date Create in 2020/3/3 22:23
 */
@Data
public class UserCourse implements Serializable{

    private Integer id ; //自增id
    private String username; //用户id,学号
    private Integer courseId; //课程id
    private Date selectTime; //选择时间

}
