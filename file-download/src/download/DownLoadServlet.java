package download;

import utils.DownLoadUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 文件下载需求：
     1. 页面显示超链接
     2. 点击超链接后弹出下载提示框
     3. 完成图片文件下载
 */

@WebServlet("/downLoadServlet")
public class DownLoadServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //1.通过请求参数名获得请求参数（文件名称）
        String filename = req.getParameter("filename");

        //2.使用字节输入流加载进内存
        //2.1 获取文件真实路径(服务器)
        ServletContext servletContext = this.getServletContext();
        String realPath = servletContext.getRealPath("/img/"+filename);  //注意路径
        //2.2使用字节输入流进行关联
        FileInputStream fis = new FileInputStream(realPath);

        //3.设置response响应头
        //3.1设置响应头类型 content-type,告诉客户端实际返回的内容的内容类型
        String mimeType = servletContext.getMimeType(filename);
        resp.setHeader("content-type",mimeType);

        //解决浏览器遇到的中文问题
        String agent = req.getHeader("user-agent");
        filename = DownLoadUtils.getFileName(agent, filename);  //根据浏览器请求头的agent不同返回不同编码的filename

        //3.2设置响应头打开方式 content-disposition
        resp.setHeader("content-disposition","attachment;filename="+filename);  //以附件的形式展现

        //4将字节输入流写出到输出流中
        ServletOutputStream sos = resp.getOutputStream();
        byte[] buff = new byte[1024 * 5];
        int len = 0;
        while((len = fis.read(buff)) != -1){
            sos.write(buff,0,len);
        }
        fis.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req,resp);
    }
}
