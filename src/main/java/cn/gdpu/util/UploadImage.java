package cn.gdpu.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName UploadImage  上传图片工具类
 * @Author ttaurus
 * @Date Create in 2020/1/19 15:23
 */
public class UploadImage{

    private static Logger logger = LoggerFactory.getLogger(UploadImage.class);
    
    //上传图片
    public static String uploadImage(HttpServletRequest request , MultipartFile file){
        BufferedOutputStream stream = null;
        if(!file.isEmpty()){
            try{
                String uploadFilePath = file.getOriginalFilename(); //获取文件原名称 C:\Users\ttaurus\Pictures\222.jpg

                // 截取上传文件的文件名
                String uploadFileName = System.currentTimeMillis() + "";
                //System.out.println("multiReq.getFile()文件名为:" + uploadFileName);

                // 截取上传文件的后缀
                String uploadFileSuffix = uploadFilePath.substring(
                        uploadFilePath.indexOf('.') + 1 , uploadFilePath.length());
                //System.out.println("uploadFileSuffix:文件后缀名为" + uploadFileSuffix);

                //防止文件名重复 然后导致覆盖 使用字符串
                String uuid = UUID.randomUUID().toString().replace("-" , "");
                uploadFileName = uploadFileName + "_" + uuid;

                String path = request.getServletContext().getRealPath("/img");
                new File(path).mkdirs();

                String dbPath = "/img/" + uploadFileName + "." + uploadFileSuffix;
                stream = new BufferedOutputStream(new FileOutputStream(new File(
                        path , uploadFileName + "." + uploadFileSuffix))); //指定存入地址。。

                byte[] bytes = file.getBytes();
                stream.write(bytes , 0 , bytes.length);
                logger.debug("文件上传成功 " + uploadFileName);

                return dbPath;

            }catch(Exception e){
                e.printStackTrace();
            }finally{
                try{
                    if(stream != null){
                        stream.close();
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
