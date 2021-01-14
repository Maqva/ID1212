package database;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import bean.UserBean;

public class DataBaseObject {
	private static final String DB_PATH = "D:\\Programing\\ID1212\\ID1212Task3\\src\\database";
	private static final String QUIZZ_QUESTION_DIRECTORY = DB_PATH+"\\QuizzEntries";
	private static final String USER_DIRECTORY = DB_PATH+"\\QuizzUsers";
	
	public static ObjectInputStream loadUserBeanStream(){
		try {
			return new ObjectInputStream(new FileInputStream(USER_DIRECTORY));
			
		} catch (IOException e) {
			return null;
		} 
	}
	
	public static ObjectInputStream loadQuestionBeanStream() {
		try {
			return new ObjectInputStream(new FileInputStream(QUIZZ_QUESTION_DIRECTORY));
			
		} catch (IOException e) {
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
