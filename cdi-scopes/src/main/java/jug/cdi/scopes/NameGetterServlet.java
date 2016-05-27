package jug.cdi.scopes;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/myname")
public class NameGetterServlet extends HttpServlet {

  private static final long serialVersionUID = -5744346441827856414L;

  @Inject
  SessionBean sessionBean;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setCharacterEncoding("UTF-8");
    resp.setContentType("text/plain");
    resp.getWriter().print(sessionBean.getUserName());
    resp.flushBuffer();
  }

}
