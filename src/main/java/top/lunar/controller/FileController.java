package top.lunar.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.lunar.utils.FileUtil;


import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("file")
public class FileController {

    @ResponseBody
    @RequestMapping("uploadTxt")
    public String uploadTxt(@RequestParam("file")MultipartFile file, HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        BufferedReader bf = null;
        try {
            if (file.isEmpty()) {
                return "file is empty";
            }
            //将multipartFile抓换为File
            File realFile = File.createTempFile("input", "txt");
            file.transferTo(realFile);

            //读取txt中的文字到String
            bf = new BufferedReader(new FileReader(realFile));
            String content = "";
            StringBuilder sb = new StringBuilder();
            while((content = bf.readLine())!=null) {
                sb.append(content);
                sb.append('\n');
            }

            content = sb.toString();

            //转换string为image
            File image = FileUtil.encode(content);

            Map map = new HashMap();
            map.put("filename", image.getName());
            return JSON.toJSONString(map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bf != null) {
                bf.close();
            }
        }
        return "failure";
    }

    @RequestMapping("test")
    public String test() {
        return "test";
    }

    @ResponseBody
    @RequestMapping("uploadBmp")
    public String uploadBmp(@RequestParam("file")MultipartFile file, HttpServletResponse response) throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;

        try {
            if (file.isEmpty()) {
                return "file is empty";
            }
            //将multipartFile抓换为File
            File realFile = File.createTempFile("input", "bmp");
            file.transferTo(realFile);
            BufferedImage bi = ImageIO.read(realFile);
            File outFile = FileUtil.decode(bi);

            //将file返回前端
            String fileName = outFile.getName();
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            byte[] buffer = new byte[1024];
            fis = new FileInputStream(outFile);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i!=-1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            os.flush();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fis.close();
            bis.close();
        }
        return "upload failure";
    }

    @ResponseBody
    @RequestMapping(value = "download", method = RequestMethod.GET)
    public void download(String filename, HttpServletResponse response) throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        if (filename != null){
            File file = new File(filename);
            if (file.exists()){
                try {
                    response.setHeader("content-type", "application/octet-stream");
                    response.setContentType("application/octet-stream");
                    // 下载文件能正常显示中文
                    response.setHeader("Content-Disposition", "attachment;filename=" + filename);
                    byte[] buffer = new byte[1024];
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i!=-1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    os.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (fis != null) {
                        fis.close();
                    }
                    if (bis != null) {
                        bis.close();
                    }
                }
            }
        }
    }
}
