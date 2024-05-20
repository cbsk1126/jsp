<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>fileUpload5.jsp</title>
  <%@ include file = "/include/bs4.jsp" %>
  <script>
    'use strict';
    
    function fCheck() {
    	let fName = document.getElementById("file").value;
    	let fNames = "";
    	let ext = "";
    	let fileSize = 0;
    	let maxSize = 1024 * 1024 * 10;
    	
    	if(fName.trim() == "") {
    		alert("업로드할 파일을 선택하세요");
    		return false;
    	}
    	
    	let fileLength = document.getElementById("file").files.length;	// 선택한 파일의 갯수
    	
    	for(let i=0; i<fileLength; i++) {
    		fName = document.getElementById("file").files[i].name;		// 선택된 1개의 파일이름가져오기
    		ext = fName.substring(fName.lastIndexOf(".")+1).toLowerCase();
	    	fileSize = document.getElementById("file").files[i].size;
	    	if(ext != 'jpg' && ext != 'gif' && ext != 'png' && ext != 'zip' && ext != 'hwp' && ext != 'ppt' && ext != 'pptx' && ext != 'doc' && ext != 'pdf' && ext != 'xlsx' && ext != 'txt') {
	    		alert("업로드 가능한 파일은 'jpg/gif/png/zip/hwp/ppt/pptx/doc/pdf/xlsx/txt'만 가능합니다.");
	    	}
	    	fNames += fName + "/";
    	}
    	
    	if(fileSize > maxSize) {
    		alert("업로드 파일의 최대용량은 10MByte입니다.");
    	}
    	else {
    		document.getElementById("fNames").value = fNames;
    		myform.submit();
    	}
    }
    
  </script>
</head>
<body>
<jsp:include page="/include/header.jsp" />
<jsp:include page="/include/nav.jsp" />
<p><br/></p>
<div class="container">
  <h2>파일 업로드 연습(싱글파일처리)</h2>
  <p>Java에서의 파일 업로드</p>
  <div>(Part객체 이용)</div>
  <hr/>
  <form name="myform" method="post" action="FileUpload5Ok" enctype="multipart/form-data">
    파일명 : 
    <div>
    	<input type="file" name="fName" id="file" class="form-control-file border mb-2" />	<!-- 실제로는 1개파일만 전송된다. -->
    </div>
    <input type="button" value="파일전송" onclick="fCheck()" class="btn btn-success form-control"/>
    <input type="hidden" name="nickName" value="${sNickName}"/>
    <input type="hidden" name="fNames" id="fNames"/>
  </form>
  <hr/>
  <div><a href="FileDownLoad.st" class="btn btn-primary form-control">다운로드폴더로 이동하기</a></div>
</div>
<p><br/></p>
<jsp:include page="/include/footer.jsp" />
</body>
</html>