package vn.iotstar.controller.auth;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {
        @Override
        protected void doGet(
                HttpServletRequest request,
                HttpServletResponse response
        )
        throws IOException{
                HttpSession session = request.getSession(false);
                if(session != null){
                        session.invalidate();
                }
                response.sendRedirect(request.getContextPath()+"/login");
        }
}