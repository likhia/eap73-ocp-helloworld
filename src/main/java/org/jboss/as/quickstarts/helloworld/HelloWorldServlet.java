package org.jboss.as.quickstarts.helloworld;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@SuppressWarnings("serial")
@WebServlet("/HelloWorld")
public class HelloWorldServlet extends HttpServlet {

    static final Logger logger = LogManager.getLogger(HelloWorldServlet.class.getName());

    static String PAGE_HEADER = "<html><head><title>Test Session Replication</title></head><body>";

    static String PAGE_FOOTER = "</body></html>";

    @Inject
    HelloService helloService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        HttpSession session = req.getSession();

        logger.error("======== Get shared session ======== " + (String) session.getAttribute("replica"));
        logger.error("======== Get count session ======== " + session.getAttribute("count"));


        CountObject count = (CountObject) session.getAttribute("count");
        if(count != null) {
            count.increment();
        } else {
            count = new CountObject();
        }

        session.setAttribute("count", count);

        PrintWriter writer = resp.getWriter();
        writer.println(PAGE_HEADER);
        writer.println("<h1>" + helloService.createHelloMessage("World : " + count.getCount()) + "</h1>");
        writer.println(PAGE_FOOTER);
        writer.close();
    }

}
