package bean;

public class User{
    private String name;
    private String email;
    
    public User(){}
    
    public void setName(String s){
        this.name = s;
    }
    
    public void setEmail(String s){
        this.email = s;
    }
    
    public String getName(){
        return name;
    }
    
    public String getEmail(){
        return email;
    }
}