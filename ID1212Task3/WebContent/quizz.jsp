<%@page import="bean.QuestionBean"%>
<jsp:useBean class="bean.QuestionBean" id="question1" scope="session"></jsp:useBean>
<jsp:useBean class="bean.QuestionBean" id="question2" scope="session"></jsp:useBean>
<jsp:useBean class="bean.QuestionBean" id="question3" scope="session"></jsp:useBean>
<%@ page language="java" contentType="text/html"%>
<!DOCTYPE html>


<html>
    <head><title>guess.jsp</title></head>
    <body>
    <h3>Answer the following Questions:</h3>
    <form  action="/Lab_Task_3/Quizz" method="POST">
    
    <%! int i; int y; %>
    <%! QuestionBean[] beans = new QuestionBean[4];%>
   	<% 	beans[0] = question1;
    	beans[1] = question2;
    	beans[2] = question3;%>
    <%for(i = 1; i < 4; i++){%>
    	<p><%= beans[(i-1)].getQuestion()%></p>
    	<%for(y = 0; y < 3; y++){%>
    		<input type="radio" id="<%=""+i+":"+y %>" name = "guess<%=i %>" value = "<%=y%>"required>
    		<label for = "<%=y %>"><%= beans[i-1].getAnswers()[y]%></label><br>
    	<%}%>
    	<br>
    <%}%>
    <br>
    <input type="submit" value="Submit">
    </form>
    </body>
    
</html>