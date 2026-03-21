package cn.gdpu.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName College
 * @Author ttaurus
 * @Date Create in 2020/3/3 22:22
 */
@Data
public class College implements Serializable{

    private Integer collegeId; //学院id
    private String name; //学院名称

}
