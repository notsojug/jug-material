package jug.cdi.scopes;

import java.io.IOException;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/random")
public class RandomServlet extends HttpServlet {

  private static final long serialVersionUID = 5020802587207130508L;
  
  @Inject
  RandomNumberGenerator rng;
  
  @Inject
  @Random
  Instance<Integer>randomInt;
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setCharacterEncoding("UTF-8");
    resp.setContentType("text/plain");
    if (req.getParameter("name") != null) {
      resp.getWriter().print(rng.getName());
    } else {
      resp.getWriter().print(randomInt.get());
    }
    resp.flushBuffer();
  }

}
