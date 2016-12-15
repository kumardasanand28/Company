package main.java.com.listner.servletcontext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Context Destroyed!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Context Initialized!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		ServletContext context = event.getServletContext();
		context.setAttribute("name", "Anand");
		
	}

}
