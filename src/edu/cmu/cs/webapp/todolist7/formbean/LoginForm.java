package edu.cmu.cs.webapp.todolist7.formbean;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class LoginForm extends FormBean {
    private String email;
    private String password;
    private String action;
    
    public String getEmail()     { return email;    }
    public String getPassword()  { return password; }
    public String getAction()    { return action;   }
    
    public void setEmail(String s)     { email = s.trim();    }
    public void setPassword(String s)  { password = s.trim(); }
    public void setAction(String s)    { action   = s;        }

    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<String>();

        if (email == null || email.length() == 0) errors.add("User Name is required");
        if (password == null || password.length() == 0) errors.add("Password is required");
        if (action == null) errors.add("Button is required");

        if (errors.size() > 0) return errors;

        if (!action.equals("Login") && !action.equals("Register")) errors.add("Invalid button");
        if (email.matches(".*[<>\"].*")) errors.add("User Name may not contain angle brackets or quotes");
        
        return errors;
    }
}
