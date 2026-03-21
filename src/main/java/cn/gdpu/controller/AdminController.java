package cn.gdpu.controller;

import cn.gdpu.service.AdminService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName AdminController
 * @Author ttaurus
 * @Date Create in 2020/3/12 10:04
 */
@Controller
@Api
public class AdminController{

    @Autowired
    AdminService adminService;
    /**
     * Excel表格导出接口
     * http://localhost:8080/ExcelDownload
     * @param response response对象
     */
    @GetMapping("/ExcelDownload")
    @PreAuthorize("hasAuthority('admin')")
    public void excelDownload(HttpServletResponse response) throws IOException{
        adminService.excelOut(response);
    }

    /**
     * 课程管理
     * @return
     */
    @GetMapping("/courseManage")
    @PreAuthorize("hasAnyAuthority('admin')")
    public String courseManage(){
        return "courseManage";
    }

    /**
     * 添加课程
     * @return
     */
    @GetMapping("/addCourse")
    @PreAuthorize("hasAuthority('admin')")
    public String addCourse(){
        return "addCourse";
    }

    @GetMapping("/editCourse")
    @PreAuthorize("hasAnyAuthority('admin')")
    public String editCourse() {return "editCourse";}

}


