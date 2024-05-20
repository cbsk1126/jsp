package study2.pdstest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
@WebServlet("/FileUpload5Ok")
public class FileUpload5Ok extends HttpServlet {
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String realPath = request.getServletContext().getRealPath("/images/pdstest/");
			
			Part filePart = request.getPart("fName");					// form 태그 중 name="fName" 인 요청 파트
			String fileName = filePart.getSubmittedFileName();// 요청된 파트의 전송된 파일 이름
			InputStream fis = filePart.getInputStream();			// 입력 스트림
			
			String uid = UUID.randomUUID().toString().substring(0,13);					// 파일명 중복방지를 위한처리
			fileName = fileName.substring(0,fileName.lastIndexOf(".")) + "_" + uid + fileName.substring(fileName.lastIndexOf("."));		// 같은 파일명 구분을 위한 처리
			
			FileOutputStream fos = new FileOutputStream(realPath + fileName);	// 파일 출력 스트림
			
			// 1024byte 씩 버퍼에 담아 읽어오는 과정, write(buffer, offset, length); 를 통해 읽어온 만큼만 쓰는 방법
			byte[] buffer = new byte[2048];
			int size = 0;
			while((size=fis.read(buffer)) != -1) {
				fos.write(buffer, 0, size);
			}
			fos.close();
			fis.close();
			
			request.setAttribute("message", "파일이 업로드 되었습니다.");
			request.setAttribute("url", "FileUpload5.st");
			
			String viewPage = "/include/message.jsp";
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
			dispatcher.forward(request, response);
	}
}
