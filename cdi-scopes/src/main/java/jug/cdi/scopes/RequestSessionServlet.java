package jug.cdi.scopes;

import java.io.IOException;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/request")
public class RequestSessionServlet extends HttpServlet {

  private static final long serialVersionUID = 525913093595118422L;

  @Inject
  @New
  Instance<SessionBean> sessionBean;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setCharacterEncoding("UTF-8");
    resp.setContentType("text/plain");
    if (sessionBean.isUnsatisfied() || sessionBean.isAmbiguous()) {
      System.out.println("Instance not found");
      throw new ServletException();
    } else {
      resp.getWriter().print(sessionBean.get().count());
    }
    resp.flushBuffer();
  }

}
