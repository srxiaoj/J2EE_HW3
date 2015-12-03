package edu.cmu.cs.webapp.todolist7.formbean;
/**
 * @author Haorui Wu
 * @date 11/23/2015
 * @courseNumber: 08672
 */

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class RegisterForm extends FormBean{
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String registration;
    private String action;
    
    public String getEmail()     { return email;     }
    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName;  }
    public String getPassword()  { return password;  }
    public String getRegistration()  {  return registration;  }
    public String getAction()    { return action;    }
    
//    public boolean isPresent()   { 
////        System.out.println();
//        return registration != null;
//    }

    public void setEmail(String email)               {   this.email = email;                }
    public void setFirstName(String firstName)       {   this.firstName = firstName;        }
    public void setLastName(String lastName)         {   this.lastName = lastName;          }
    public void setPassword(String password)         {   this.password = password;          }
    public void setRegistration(String registration) {   this.registration = registration;  }
    public void setAction(String s)                  {   this.action = s;                   }
    
    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<String>();

        if (email == null || email.length() == 0) errors.add("Email is required");
        if (firstName == null || firstName.length() == 0) errors.add("First name is required");
        if (lastName == null || lastName.length() == 0) errors.add("Last name is required");
        if (password == null || password.length() == 0) errors.add("Password is required");
        if (action == null) errors.add("Action is required");

        if (errors.size() > 0) return errors;

        if (!action.equals("Login") && !action.equals("Register")) errors.add("Invalid button");
        if (email.matches(".*[<>\"].*")) errors.add("Email may not contain angle brackets or quotes");
        
        return errors;
    }
}
