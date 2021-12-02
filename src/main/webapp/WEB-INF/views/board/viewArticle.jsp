<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<%
	request.setCharacterEncoding("utf-8");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글상세</title>
<script  src="http://code.jquery.com/jquery-latest.min.js"></script> 
<style>
#tr_file_upload{
	display:none;
}
#tr_btn_modify{
	display:none;
}
</style>
<script type="text/javascript">
	function backToList(obj){
		obj.action="${contextPath}/board/listArticles.do";
		obj.submit();
	}
	
	function fn_enable(obj){
		document.getElementById("i_title").disabled=false;
		document.getElementById("i_content").disabled=false;
		document.getElementById("i_imageFileName").disabled=false;
	}
	
	function readURL(input){
		if(input.files && input.files[0]){
			var reader = new FileReader();
			reader.onload = function(e){
				$("#preview").attr('src',e.target.result);
			}
			reader.readAsDataURL(input.files[0]);
		}
	}
</script>
</head>
<body>
<form name="frmArtilce" action="${contextPath}" method="post" enctype="multipart/form-data">
	<table border=0 align="center">
		<tr>
			<td width="150" align="center" bgcolor="#FF9933">글번호</td>
			<td>
				<input type="text" value="${article.articleNO}" disabled/>
				<input type="hidden" name="articleNO" value="${articleNO}" />
			</td>
		</tr>
		
		<tr>
			<td width="150" align="center" bgcolor="#FF9933">작성자 아이디</td>
			<td>
				<input type="text" value="${article.id}" name="writer" disabled/>
			</td>
		</tr>
		
		<tr>
			<td width="150" align="center" bgcolor="#FF9933">제목</td>
			<td>
				<input type="text" value="${article.title}" name="title" id="i_title" disabled/>
			</td>
		</tr>
		
		<tr>
			<td width="150" align="center" bgcolor="#FF9933">내용</td>
			<td><textarea rows="20" cols="60" name="content" id="i_content" disabled>${article.content}</textarea></td>
		</tr>
		
		<c:choose>
			<c:when test="${not empty article.imageFileName && article.imageFileName!='null'}">
				<tr>
					<td width="150" align="center" bgcolor="#FF9933" rowspan="2">이미지</td>
					<td>
						<input type="hidden" name="originalFileName" value="${article.imageFileName}"/>
						<img src="${contextPath}/download.do?articleNO=${article.articleNO}&imageFileName=${article.imageFileName}" id="preview"  /><br>
					</td>
				</tr>
				
				<tr>
					<td></td>
					<td>
						<input type="file" name="imageFileName" id="i_imageFileName" disabled onchange="readURL(this);"/>
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<tr id="tr_file_upload">
					<td width="150" align="center" bgcolor="#FF9933" rowspan="2">이미지</td>
					<td>
						<input type="hidden" name="originalFileName" value="${article.imageFileName}"/>
					</td>					
				</tr>
				<tr>
					<td></td>
					<td>
						<img id="preview"/><br>
						<input type="file" name="imageFileName" id="i_imageFileName" disabled onchange="readURL(this);"/>
					</td>
				</tr>
			</c:otherwise>
		</c:choose>
		<tr>
			<td width="150" align="center" bgcolor="#FF9933">등록일자</td>
			<td>
				<input type="text" value="<fmt:formatDate value="${article.writeDate}"/>" disabled/>
			</td>
		</tr>
		
		<tr id="tr_btn">
			<td colspan="2" align="center">
				<c:if test="${member.id==article.id}">
					<input type="button" value="수정하기" onclick="fn_enable(this.form)"/>
					<input type="button" value="삭제하기" onclick="fn_remove_article('${contextPath}/board/removeArticle.do',${artilce.artilceNO})"/>
				</c:if>
				<input type="button" value="리스트로 돌아가기" onclick="backToList(this.form)"/>
				<input type="button" value="답글쓰기" onclick="fn_reply_form('${contextPath}/board/replyForm.do',${artilce.articleNO})"/>
			</td>
		</tr>		
	</table>
</form>

</body>
</html>