package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionBean implements Serializable {
	private static final long serialVersionUID = -2599445434206233791L;
	private String question;
    private String[] options;
    private int correctAnswer;
    
    public String getQuestion() {
    	return question;
    }
    
    public void setQuestion(String s) {
    	question = s;
    }
    
    public int getCorrectAnswer() {
    	return correctAnswer-1;
    }
    
    public void setCorrectAnswer(int input) {
    	correctAnswer = input;
    }
    
    public String[] getAnswers() {
    	return options;
    }
    
    public void setAnswers(String[] arr) {
    	options = arr;
    }
}
