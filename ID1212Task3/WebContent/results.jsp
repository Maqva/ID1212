<%@page import="bean.QuestionBean"%>
<jsp:useBean class="bean.UserBean" id="user" scope="session"></jsp:useBean>
<jsp:useBean class="bean.QuestionBean" id="question1" scope="session"></jsp:useBean>
<jsp:useBean class="bean.QuestionBean" id="question2" scope="session"></jsp:useBean>
<jsp:useBean class="bean.QuestionBean" id="question3" scope="session"></jsp:useBean>
<%@ page language="java" contentType="text/html"%>
<!DOCTYPE html>


<html>
    <head><title>guess.jsp</title></head>
    <body>
    <h2>Congratulations!, <%=user.getName() %></h2>
    <%! int numRight; String guess; QuestionBean[] beans = new QuestionBean[4];%>
   	<% 	
   		beans[0] = question1;
    	beans[1] = question2;
    	beans[2] = question3;
    	numRight = 0;
    
    	for(int i = 0; i < 3; i++){
    		if( Integer.parseInt((String)session.getAttribute("guess"+(i+1)))==beans[i].getCorrectAnswer()){
    	    	numRight ++;
        	}
    	}
    	
    %>
    <h4>You got <%= numRight%> out of 3 questions correct!</h4>
    <br>
    <br>
    <form action="/Lab_Task_3/Quizz" method="GET">
    <input type ="submit" value="Play Again!">
    </form>
    </body>
    
</html>