package com.ibm.websphere.samples.pbw.war;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Servlet to handle K8s ready probe
 */
@WebServlet("/servlet/ready")
public class K8sReadyServlet extends HttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see javax.servlet.Servlet#init(ServletConfig)
	 */
	/**
	 * Servlet initialization.
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// Uncomment the following to generated debug code.
		// Util.setDebug(true);

	}
	/**
	 * Process incoming HTTP GET requests
	 *
	 * @param req Object that encapsulates the request to the servlet
	 * @param resp Object that encapsulates the response from the servlet
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.println("OK");
	}
}
