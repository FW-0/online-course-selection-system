package cn.gdpu.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName Admin
 * @Author ttaurus
 * @Date Create in 2020/3/3 22:21
 */
@Data
public class Admin implements Serializable{

    private Integer adminId; // 管理员id
    private String adminAccount; //管理员账号
    private String adminPassword; //管理员密码

}
