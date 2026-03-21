package cn.gdpu.service;

import cn.gdpu.bean.SelectableCourse;
import cn.gdpu.bean.User;
import cn.gdpu.bean.UserCourse;
import cn.gdpu.mapper.AdminDAO;
import cn.gdpu.mapper.SelectableCourseDAO;
import cn.gdpu.mapper.UserCourseDAO;
import cn.gdpu.mapper.UserDAO;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName AdminService
 * @Author ttaurus
 * @Date Create in 2020/3/4 19:23
 */
@Service
public class AdminService{

    @Autowired
    AdminDAO adminDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    SelectableCourseDAO selectableCourseDAO;
    @Autowired
    UserCourseDAO userCourseDAO;

    //用户登录
    public User login(String username){
        return adminDAO.login(username);
    }

    //导出学生选课信息
    public void excelOut(HttpServletResponse response) throws IOException{
        int count = 0;
        //表头数据
        String[] header = { "姓名" , "学号" , "专业" };
        //学生数据
        List<User> users = userDAO.selectAll();
        //已被选择的课程数据
        List<SelectableCourse> courses = selectableCourseDAO.selectedCourses();
        //学生的选课数据.
        List<UserCourse> userCourses = userCourseDAO.selectAll();
        //声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个表格，设置表格名称为"学生表"
        HSSFSheet sheet = workbook.createSheet("学生选课表");
        //设置表格列宽度为10个字节
        sheet.setDefaultColumnWidth(10);

        for(SelectableCourse cours : courses){

            //创建第一行表头
            HSSFRow headrow = sheet.createRow(count);
            //创建一个单元格
            HSSFCell cell = headrow.createCell(0);
            //创建一个内容对象 (课程名称）
            HSSFRichTextString text = new HSSFRichTextString("课程名称："+cours.getCourseName()+"  任课老师："+cours.getTeacher()+"  上课时间："+cours.getAddress());
            //将内容对象的文字内容写入到单元格中
            cell.setCellValue(text);
            count++;
            //遍历添加表头(下面模拟遍历学生，也是同样的操作过程)
            //创建第2行表头
            HSSFRow row = sheet.createRow(count);
            for(int i = 0 ; i < header.length ; i++){
                //创建一个单元格
                HSSFCell cellRow = row.createCell(i);
                //创建一个内容对象
                HSSFRichTextString head2 = new HSSFRichTextString(header[i]);
                //将内容对象的文字内容写入到单元格中
                cellRow.setCellValue(head2);
            }
            count++;
            //遍历学生数据
            for(UserCourse uc : userCourses){
                for(User user : users){
                    if(uc.getCourseId().equals(cours.getCourseId())){
                        if(uc.getUsername().equals(user.getUsername()))
                        {
                            HSSFRow stuRow = sheet.createRow(count);
                            HSSFCell stuCell2 = stuRow.createCell(0);
                            HSSFRichTextString stuText2 = new HSSFRichTextString(user.getStudentName());
                            stuCell2.setCellValue(stuText2);

                            HSSFCell stuCell3 = stuRow.createCell(1);
                            HSSFRichTextString stuText3 = new HSSFRichTextString(user.getUsername());
                            stuCell3.setCellValue(stuText3);

                            HSSFCell stuCell4 = stuRow.createCell(2);
                            HSSFRichTextString stuText4 = new HSSFRichTextString(user.getObject());
                            stuCell4.setCellValue(stuText4);
                            count++;
                            break;
                        }
                    }
                }
            }
            HSSFRow newRow = sheet.createRow(count);
            count++;
            HSSFRow newRow2 = sheet.createRow(count);
            count++;
        }
        
        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");

        //这后面可以设置导出Excel的名称，此例中名为student.xls
        response.setHeader("Content-disposition" , "attachment;filename=student.xls");

        //刷新缓冲
        response.flushBuffer();

        //workbook将Excel写入到response的输出流中，供页面下载
        workbook.write(response.getOutputStream());
    }

}
