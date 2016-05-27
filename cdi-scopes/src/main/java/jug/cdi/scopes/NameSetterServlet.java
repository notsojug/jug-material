package jug.cdi.scopes;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/name")
public class NameSetterServlet extends HttpServlet {

  private static final long serialVersionUID = 536367411057214346L;

  @Inject
  SessionBean sessionBean;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String userName = req.getParameter("userName");
    if (userName != null && !userName.isEmpty()) {
      System.out.println("User Name: " + userName);
      sessionBean.setUserName(userName);
    }
    // No content
    resp.setContentLength(0);
    resp.setStatus(204);
  }

}
