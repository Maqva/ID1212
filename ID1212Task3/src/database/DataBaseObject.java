package database;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import bean.QuestionBean;
import bean.UserBean;

public class DataBaseObject {
	private static final String DB_PATH = "D:\\Programing\\ID1212\\ID1212Task3\\src\\database";
	private static final String QUIZZ_QUESTION_DIRECTORY = DB_PATH+"\\QuizzEntries";
	private static final String USER_DIRECTORY = DB_PATH+"\\QuizzUsers";
	
	public static ArrayList<UserBean> loadUserBeans(){
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DIRECTORY));
			ArrayList<UserBean> out = new ArrayList<UserBean>();
			UserBean b;
			while(true) {
				try {
					b = (UserBean)ois.readObject();
					out.add(b);
				} 
				catch(EOFException e) {
					ois.close();
					return out;
				}catch (ClassNotFoundException e) {
					ois.close();
					return null;
				} catch (IOException e) {
					ois.close();
					return null;
				}
			}
		}
		catch (IOException e) {
			return null;
		}
	}
	public static ArrayList<QuestionBean> loadQuestionBeans() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(QUIZZ_QUESTION_DIRECTORY));
			ArrayList<QuestionBean> out = new ArrayList<QuestionBean>();
			QuestionBean b;
			while(true) {
				try {
					b = (QuestionBean)ois.readObject();
					out.add(b);
				} 
				catch(Exception e) {
					ois.close();
					return (out.size()>0)?out:null;
				}
			}
		}
		catch (IOException e) {
			return null;
		}
	}
	public static void saveUser(UserBean b) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DIRECTORY));
			oos.writeObject(b);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
