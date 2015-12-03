package edu.cmu.cs.webapp.todolist7.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import edu.cmu.cs.webapp.todolist7.databean.UserBean;
import edu.cmu.cs.webapp.todolist7.formbean.RegisterForm;
import edu.cmu.cs.webapp.todolist7.model.Model;
import edu.cmu.cs.webapp.todolist7.model.UserDAO;

/*
 * Register action
 */
public class RegisterAction extends Action {
    private UserDAO userDAO;
    private FormBeanFactory<RegisterForm> registerFormFactory = FormBeanFactory.getInstance(RegisterForm.class);
    public RegisterAction(Model model) {
        userDAO = model.getUserDAO();
    }

    public String getName() {
        return "register.do";
    }

    public String perform(HttpServletRequest request) {
        HttpSession session = request.getSession();
      List<String> errors = new ArrayList<String>();
      RegisterForm form = null;
      try {
          form = registerFormFactory.create(request);
          String action = form.getAction();
          System.out.println("entering register.do");
          if (action != null) {
              if (action.equals("Login")) {
                  System.out.println("rediect to login");
                  return "login.do";
              }
          }

          request.setAttribute("errors", errors);
          if (!form.isPresent()) {
              return "register.jsp";
          }
          errors.addAll(form.getValidationErrors());
          if (errors.size() != 0) {
              return "register.jsp";
          }
          UserBean user = new UserBean();

          if (form.getAction().equals("Register")) {
               System.out.println("click on register");
              user = userDAO.read(form.getEmail());
              if (user != null) {
                  errors.add("Email already used");
                  return "register.jsp";
              }
              user = new UserBean();
              user.setEmail(form.getEmail());
              user.setFirstName(form.getFirstName());
              user.setLastName(form.getLastName());
              user.setPassword(form.getPassword());
               System.out.println("saving user bean in register page");
              // create user bean with primaryKey and override the original
              // user bean
              userDAO.createAutoIncrement(user);
          } else {
              errors.addAll(form.getValidationErrors());
              if (errors.size() != 0) {
                  return "register.jsp";
              }
          }
          session.setAttribute("user", user);
           System.out.println("In register page: ");
           System.out.println("userId is: " + user.getUserId());
          return "favoritelist.do";
          // System.out.println("redirect to favorite list page");
      } catch (RollbackException e) {
          errors.add(e.getMessage());
          return "error.jsp";
      } catch (FormBeanException e) {
          errors.add(e.getMessage());
          return "error.jsp";
      }
    }
}
