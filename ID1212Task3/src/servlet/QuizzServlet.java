package servlet;

import java.beans.beancontext.BeanContext;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.QuestionBean;
import bean.UserBean;
import database.DataBaseObject;

public class QuizzServlet extends HttpServlet {
	private final int DEFAULT_NO_OF_QUESTIONS = 3;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizzServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		RequestDispatcher rd;
		//Not connected with a user yet, needs to log in
		if(session.getAttribute("user")==null) {
			rd = request.getRequestDispatcher("start.html");
		}
		//Has a user and just finished a round of the quizz needs new questions and restart quizzz
		else if(session.getAttribute("guess1")!=null){
			setupNewRound(request);
			rd = request.getRequestDispatcher("quizz.jsp");
		}
		//Is just trying to play their game man
		else {
			rd = request.getRequestDispatcher("quizz.jsp");
		}
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		RequestDispatcher rd;
		if(session != null) {
			//Sent in Quzz answers, should display end results and send back to empty GET to restart
			if(request.getParameter("guess1")!=null) {
				session.setAttribute("guess1", request.getParameter("guess1"));
				session.setAttribute("guess2", request.getParameter("guess2"));
				session.setAttribute("guess3", request.getParameter("guess3"));
				rd = request.getRequestDispatcher("results.jsp");
				rd.forward(request, response);
			}
			//Just sent in user data, register user and add to session and start quizz
			else if(request.getParameter("name")!= null) {
				String un = request.getParameter("name");
				String pass = request.getParameter("pwd");
				String mail = request.getParameter("email");
				bean.UserBean uB = new bean.UserBean();
				uB.setName(un);
				uB.setPassword(pass);
				uB.setEmail(mail);
				session.setAttribute("user", uB);
				DataBaseObject.saveUser(uB);
				setupNewRound(request);
				rd = request.getRequestDispatcher("quizz.jsp");
				rd.forward(request, response);
			}
		}
		//a error has occured, forbidden from proceeding
		
	}
	private void setupNewRound(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		session.removeAttribute("guess1");
		session.removeAttribute("guess2");
		session.removeAttribute("guess3");
		ArrayList<QuestionBean> list = loadQuestions();
		if(list != null) {
			Collections.shuffle(list);
			for(int i = 1; i <= DEFAULT_NO_OF_QUESTIONS; i++) {
				session.setAttribute(("question"+i), list.get(i-1));
			}
		}
	}
	
	private ArrayList<UserBean> loadUsers() {
			ObjectInputStream ois = DataBaseObject.loadUserBeanStream();
			ArrayList<UserBean> out = new ArrayList<UserBean>();
			UserBean b;
			while(true) {
				try {
					b = (UserBean)ois.readObject();
					out.add(b);
				} catch(EOFException e ) {
					return out;
				}				
				catch (ClassNotFoundException e) {
					return null;
				} catch (IOException e) {
					return null;
				}
			}
		
	}
	
	private ArrayList<QuestionBean> loadQuestions() {
			ObjectInputStream ois = DataBaseObject.loadQuestionBeanStream();
			ArrayList<QuestionBean> out = new ArrayList<QuestionBean>();
			QuestionBean b;
			while(true) {
				try {
					b = (QuestionBean)ois.readObject();
					out.add(b);
				} 
				catch(EOFException e) {
					return out;
				}catch (ClassNotFoundException e) {
					return null;
				} catch (IOException e) {
					return null;
				}
			}
	}
	
}
