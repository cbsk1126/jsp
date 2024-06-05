package photoGallery;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PhotoGalleryCommand implements PhotoGalleryInterface {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String part=request.getParameter("part")==null ? "전체" : request.getParameter("part");
		String choice=request.getParameter("choice")==null ? "최신순" : request.getParameter("choice");
		
		String imsiChoice = "";
		if(choice.equals("추천순")) imsiChoice = "goodCount";
		else if(choice.equals("조회순")) imsiChoice = "readNum";
		else if(choice.equals("댓글순")) imsiChoice = "replyCnt";	
		else imsiChoice = choice;
		
		PhotoGalleryDAO dao = new PhotoGalleryDAO();
		
		ArrayList<PhotoGalleryVO> vos = dao.getPhotoGalleryList(part, imsiChoice);
		System.out.println("choice : " + choice);
		request.setAttribute("vos", vos);
		request.setAttribute("part", part);
		request.setAttribute("choice", choice);
	}

}
