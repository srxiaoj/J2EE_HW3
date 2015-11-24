

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jdk.nashorn.internal.ir.RuntimeNode.Request;

public class RegisterForm  {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String button;
    
    public RegisterForm(HttpServletRequest request) {
        email = request.getParameter("email");
        firstName = request.getParameter("firstName");
        lastName = request.getParameter("lastName");
        password = request.getParameter("password");
        button   = request.getParameter("button");
    }
    public String getEmail()     { return email;     }
    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName;  }
    public String getPassword()  { return password;  }
    public String getButton()    { return button;    }
    
    public boolean isPresent()   { return button != null; }
    public boolean isFirstTimeVisit(HttpServletRequest request) {
        String isFirst = request.getParameter("First Time Visit");
        if (isFirst == null) {
            return true;
        } else 
            return false;
    }

    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<String>();

        if (email == null || email.length() == 0) errors.add("Email is required");
        if (firstName == null || firstName.length() == 0) errors.add("First name is required");
        if (lastName == null || lastName.length() == 0) errors.add("Last name is required");
        if (password == null || password.length() == 0) errors.add("Password is required");
        if (button == null) errors.add("Button is required");

        if (errors.size() > 0) return errors;

        if (!button.equals("Login") && !button.equals("Register")) errors.add("Invalid button");
        if (email.matches(".*[<>\"].*")) errors.add("Email may not contain angle brackets or quotes");
        
        return errors;
    }
}
