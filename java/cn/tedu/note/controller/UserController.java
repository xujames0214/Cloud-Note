package cn.tedu.note.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.tedu.note.entity.User;
import cn.tedu.note.service.PasswordException;
import cn.tedu.note.service.UserNameException;
import cn.tedu.note.service.UserNotFoundException;
import cn.tedu.note.service.UserService;
import cn.tedu.note.util.JsonResult;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController{
	@Resource
	private UserService userService;
	
	@RequestMapping("/login.do")
	@ResponseBody
	public Object login(
			String name,
			String password,
			HttpSession session){
		User user = userService.login(name, password);
		//��¼�ɹ���ʱ�򣬽�user��Ϣ���浽session
		//�����ڹ������м���¼���
		session.setAttribute("loginUser", user);
		return new JsonResult(user);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseBody
	public JsonResult handleUserNotFound(UserNotFoundException e){
		e.printStackTrace();
		return new JsonResult(2,e);
	}
	
	@ExceptionHandler(PasswordException.class)
	@ResponseBody
	public JsonResult handlePassword(
			PasswordException e){
		e.printStackTrace();
		return new JsonResult(3,e);
	}
	
	@ExceptionHandler(UserNameException.class)
	@ResponseBody
	public JsonResult handleUserName(UserNameException e){
		e.printStackTrace();
		return new JsonResult(4,e);
	}
	
	
	@RequestMapping("/regist.do")
	@ResponseBody
	public JsonResult regist(String name,String nick,String password,String confirm){
		User user = userService.regist(name.trim(), nick, password, confirm);
		return new JsonResult(user);
	}
	
	@RequestMapping(value="image.do",produces="image/png")
	@ResponseBody
	public byte[] image() throws IOException{
		return createPng();
	}
	
	@RequestMapping(value="/downloadimg.do",
			produces="application/octet-stream")
		@ResponseBody
	public byte[] downloadimg(HttpServletResponse res) 
		throws IOException{
		//�ο�: 19.5.1 Content-Disposition
		//Content-Disposition: attachment; filename="fname.ext"
		res.setHeader("Content-Disposition",
			"attachment; filename=\"demo.png\"");
		return createPng();
	}

	private byte[] createPng() throws IOException{
		BufferedImage img = new BufferedImage(200,80,BufferedImage.TYPE_3BYTE_BGR);
		img.setRGB(100, 40, 0xffffff);
		//��ͼƬ����ΪPNG
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(img, "png", out);
		out.close();
		byte[] png = out.toByteArray();
		return png;
	}
	
	
	@RequestMapping(value="/downloadexcel.do",
			produces="application/octet-stream")
		@ResponseBody
	public byte[] excel(HttpServletResponse res) 
		throws IOException{
		//�ο�: 19.5.1 Content-Disposition
		//Content-Disposition: attachment; filename="fname.ext"
		res.setHeader("Content-Disposition",
			"attachment; filename=\"demo.xls\"");
		return createExcel();
	}
	
	
	private byte[] createExcel() throws IOException{
		//����������
		HSSFWorkbook workbook = new HSSFWorkbook();
		//����������
		HSSFSheet sheet = workbook.createSheet("Demo");
		//�ڹ������д���������
		HSSFRow row = sheet.createRow(0);
		//�������еĸ���
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("Hello World!");
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);
		out.close();
		return out.toByteArray();
	}
	
	@RequestMapping("/upload.do")
	@ResponseBody
	public JsonResult upload(
			MultipartFile userfile1,
			MultipartFile userfile2) throws IllegalStateException, IOException{
		//Spring MVC�п�������MultipartFile�������ص��ļ���
		//�ļ��е�һ�����ݶ����Դ�Multipart�������ҵ�
		String file1 = userfile1.getOriginalFilename();
		String file2 = userfile2.getOriginalFilename();
		
		System.out.println(file1);
		System.out.println(file2);
		
		//�����ļ���3�ַ���
		//1. transferTo(Ŀ���ļ�),���ļ�ֱ�ӱ��浽Ŀ���ļ�,���Դ�����ļ�
		//2. userfile1.getBytes(),��ȡ�ļ���ȫ������,�ʺϴ���С�ļ�������
		//3. userfile1.getInputStream(),��ȡ�����ļ�����
		
		//�����Ŀ���ļ���
		File dir = new File("D:/demo");
		dir.mkdir();
		File f1 = new File(dir,file1);
		File f2 = new File(dir,file2);
		//��һ�ֱ��淽ʽ
		//userfile1.transferTo(f1);
		//userfile2.transferTo(f2);
		
		//������
		/*InputStream in1 = userfile1.getInputStream();
		FileOutputStream out1 = new FileOutputStream(f1);
		int b;
		while((b = in1.read()) != -1){
			out1.write(b);
		}*/
		return new JsonResult(true);
	}
}
