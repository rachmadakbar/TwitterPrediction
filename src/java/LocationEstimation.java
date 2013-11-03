/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Rachmad-
 */
@WebServlet(urlPatterns = {"/LocationEstimation"})
public class LocationEstimation extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String mode = request.getParameter("mode");
        if (mode.equals("1")) {
            String from = request.getParameter("from");
            LocationEstimator loc = new LocationEstimator();
            String[] f = loc.estimateStreet(from);
            HaversineAndPath hap = new HaversineAndPath(f, f, 1);
            HttpSession route = request.getSession(true);
            route.setAttribute("from", f[0]);
            route.setAttribute("to", "-");
            route.setAttribute("lat2", hap.point2[0]);
            route.setAttribute("long2", hap.point2[1]);
            route.setAttribute("lat1", hap.point1[0]);
            route.setAttribute("long1", hap.point1[1]);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } else if(mode.equals("2")){
            String from = request.getParameter("from");
            String to = request.getParameter("to");
            LocationEstimator loc = new LocationEstimator();
            String[] f = loc.estimate(from);
            loc = new LocationEstimator();
            String[] t = loc.estimate(to);
            HaversineAndPath hap = new HaversineAndPath(f, t, 2);
            hap.getPoint2();
//        PrintWriter out = response.getWriter();
//        try {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet LocationEstimation</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("From1<br/>");
//            out.println(hap.from.first1[0]+"<br/>");
//            out.println(hap.from.first1[1]+"<br/>");
//            out.println(hap.from.center1[0]+"<br/>");
//            out.println(hap.from.center1[1]+"<br/>");
//            out.println(hap.from.last1[0]+"<br/>");
//            out.println(hap.from.last1[1]+"<br/>");
//            out.println("From2"+"<br/>");
//            out.println(hap.from.first2[0]+"<br/>");
//            out.println(hap.from.first2[1]+"<br/>");
//            out.println(hap.from.center2[0]+"<br/>");
//            out.println(hap.from.center2[1]+"<br/>");
//            out.println(hap.from.last2[0]+"<br/>");
//            out.println(hap.from.last2[1]+"<br/>");
//            out.println("To1"+"<br/>");
//            out.println(hap.to.first1[0]+"<br/>");
//            out.println(hap.to.first1[1]+"<br/>");
//            out.println(hap.to.center1[0]+"<br/>");
//            out.println(hap.to.center1[1]+"<br/>");
//            out.println(hap.to.last1[0]+"<br/>");
//            out.println(hap.to.last1[1]+"<br/>");
//            out.println("To2"+"<br/>");
//            out.println(hap.to.first2[0]+"<br/>");
//            out.println(hap.to.first2[1]+"<br/>");
//            out.println(hap.to.center2[0]+"<br/>");
//            out.println(hap.to.center2[1]+"<br/>");
//            out.println(hap.to.last2[0]+"<br/>");
//            out.println(hap.to.last2[1]+"<br/>");
//            out.println("</body>");
//            out.println("</html>");
//        } finally {            
//            out.close();
//        }
        
            HttpSession route = request.getSession(true);
            route.setAttribute("from", f[0]);
            route.setAttribute("to", t[0]);
            route.setAttribute("lat2", hap.point2[0]);
            route.setAttribute("long2", hap.point2[1]);
            route.setAttribute("lat1", hap.point1[0]);
            route.setAttribute("long1", hap.point1[1]);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }else{
            String street = request.getParameter("street");
            String from = request.getParameter("from");
            String to = request.getParameter("to");
            LocationEstimator loc = new LocationEstimator();
            String[] f = loc.estimate(from);
            loc = new LocationEstimator();
            String[] t = loc.estimate(to);
            loc = new LocationEstimator();
            String[] s = loc.estimate(street);
            HaversineAndPath hap = new HaversineAndPath(s, f, t);
        }
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
