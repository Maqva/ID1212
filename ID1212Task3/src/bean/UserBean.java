package bean;

import java.io.Serializable;

public class UserBean implements Serializable{
	private static final long serialVersionUID = -8907786096077321606L;
	private String name;
    private String email;
    private String password;
    
    public UserBean(){}
    
    public void setName(String s){
        this.name = s;
    }
    
    public void setEmail(String s){
        this.email = s;
    }
    
    public void setPassword(String s) {
    	this.password=s;
    }
    
    public String getName(){
        return name;
    }
    
    public String getEmail(){
        return email;
    }
    
    public String getPassword() {
    	return password;
    }
}