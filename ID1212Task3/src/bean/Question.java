package bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {
	private String question;
    private ArrayList options;
    private int correctAnswer;
    
    public String getQuestion() {
    	return question;
    }
    
    public void setQuestion(String s) {
    	s = question;
    }
    
    public String getCorrectAnswer() {
    	return (String) options.get(correctAnswer);
    }
    
    public void setCorrectAnswerIndex(int i) {
    	correctAnswer = i;
    }
    
    public ArrayList getAnswers() {
    	return options;
    }
    
    public void setAnswers(ArrayList l) {
    	options = l;
    }
}
