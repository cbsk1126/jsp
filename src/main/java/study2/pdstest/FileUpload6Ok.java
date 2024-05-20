package study2.pdstest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@SuppressWarnings("serial")
@MultipartConfig(
	// location="/tmp",							// 파일 업로드 시에 임시 저장 디렉터리를 지정한다.(String)
	fileSizeThreshold=1024*1024,		// fileUpload 시에 메모리에 저장되는 임시 파일 크기를 정의(int)
	maxFileSize=1024*1024*10,				// 업로드할 1개 파일의 최대 크기를 지정한다.(long)
	maxRequestSize=1024*1024*10*10	// 한번에 request 시에 최대 크기를 지정한다.(long)
)
@WebServlet("/FileUpload6Ok")
public class FileUpload6Ok extends HttpServlet {
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String realPath = request.getServletContext().getRealPath("/images/pdstest/");
			
		  // request 객체 내부 getParts() 메소드 사용하여 배열로 받는다.
			Collection<Part> fileParts = request.getParts();
			
			for(Part filePart : fileParts) {		
				if(!filePart.getName().equals("fName")) continue;	// 요청의 여러 파트(Form태그의 Part)들 중 name 이 fname 으로 설정된 것
				if(filePart.getSize() == 0) continue;

				String fileName = filePart.getSubmittedFileName();
				InputStream fis = filePart.getInputStream();
				
				String uid = UUID.randomUUID().toString().substring(0,13);					// 파일명 중복방지를 위한처리
				fileName = fileName.substring(0,fileName.lastIndexOf(".")) + "_" + uid + fileName.substring(fileName.lastIndexOf("."));
				
				FileOutputStream fos = new FileOutputStream(realPath + fileName);
				
				byte[] buf = new byte[2048];
				int size = 0;
				while((size=fis.read(buf)) != -1) {
					fos.write(buf, 0, size);
				}
				fos.close();
				fis.close();
			}
			
			request.setAttribute("message", "파일이 업로드 되었습니다.");
			request.setAttribute("url", "FileUpload6.st");
			
			String viewPage = "/include/message.jsp";
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
			dispatcher.forward(request, response);
	}
}
