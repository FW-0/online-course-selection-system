package cn.gdpu.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName SelectableCourse
 * @Author ttaurus
 * @Date Create in 2020/3/3 22:24
 */
@Data
public class SelectableCourse implements Serializable{

    private Integer courseId; //课程id 自增。
    private Integer collegeId; //学院id
    private String collegeName; //学院名称；
    private String courseName; //课程名称
    private String teacher; //任课老师
    private String type; //课程类型
    private Integer stock; //课程剩余人数。
    private Integer courseScore; //课程学分
    private String address; //课程地址
    private String description; //课程描述
    private boolean flag;
    private int isHide;

}
