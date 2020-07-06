package top.lunar.controller;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.lunar.utils.FileUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("file")
public class FileController {

    @ResponseBody
    @RequestMapping("uploadTxt")
    public String uploadTxt(@RequestParam("file")MultipartFile file, HttpServletRequest request,
                            HttpServletResponse response) throws IOException {

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        BufferedReader bf = null;
        try {
            if (file.isEmpty())
                return "file is empty";
            //将multipartFile抓换为File
            File realFile = File.createTempFile("input", "txt");
            file.transferTo(realFile);

            //读取txt中的文字到String
            bf = new BufferedReader(new FileReader(realFile));
            String content = "";
            StringBuilder sb = new StringBuilder();
            while((content = bf.readLine())!=null) {
                sb.append(content.trim());
            }

            content = sb.toString();

            //转换string为image
            File image = FileUtil.encode(content);

            //将file返回前端
            String fileName = image.getName();
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            byte[] buffer = new byte[1024];
            fis = new FileInputStream(image);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            int i = 0;
            while ((i = bis.read(buffer)) !=-1) {
                bos.write(buffer, 0, i);
            }
            bos.flush();
            return "download success";

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bf.close();
            fis.close();
            bis.close();
        }
        return "failure";
    }

    @RequestMapping("test")
    public String test() {
        return "test";
    }

    @RequestMapping("myPage")
    public String myPage() {
        return "myPage";
    }
    @ResponseBody
    @RequestMapping("uploadBmp")
    public String uploadBmp(@RequestParam("file")MultipartFile file) {
        try {
            if (file.isEmpty())
                return "file is empty";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "upload failure";
    }
}
