package com.example;

import java.io.File;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

public class TomcatService {

	static Tomcat tomcat;
	
	public static String appRoot = "/";
	
	public static String docRoot = "src/test/resources/example/build";	
	
	public static void before() throws Exception {
		startServer(appRoot,docRoot);
	}
	
	public static void after() throws Exception {
		stopServer();
	}	
	
	public static void startServer(String appRoot,String rootDirectory) throws LifecycleException,ServletException {
		tomcat = new Tomcat();
		tomcat.setPort(8080);
		tomcat.getHost();
		Context context = tomcat.addContext(appRoot,new File(docRoot).getAbsolutePath());
		Wrapper defaultServlet = context.createWrapper();
		defaultServlet.setName("d");
		defaultServlet.setServletClass("org.apache.catalina.servlets.DefaultServlet");
		defaultServlet.addInitParameter("debug", "0");
		defaultServlet.addInitParameter("listings", "false");
		defaultServlet.setLoadOnStartup(1);
		context.addChild(defaultServlet);
		context.addServletMapping("/*", "d");
		context.addServletMapping("/static/*", "d");
		context.addServletMapping("/data/*", "d");
		context.addWelcomeFile("index.html");
		tomcat.start();
	}
	
	public static void stopServer() throws LifecycleException {
		tomcat.getServer().stop();
	}	
	
}
