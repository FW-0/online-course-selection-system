package cn.gdpu.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName User
 * @Author ttaurus
 * @Date Create in 2020/3/3 22:19
 */
@Data
public class User implements Serializable{
    private String studentName; //学生姓名
    private String username; //用户账户
    private String password;// 用户密码
    private String object; //学生专业
    private String role;


    public String getStudentName(){
        return studentName;
    }

    public void setStudentName(String studentName){
        this.studentName = studentName;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getObject(){
        return object;
    }

    public void setObject(String object){
        this.object = object;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }
}
