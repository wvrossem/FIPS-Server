package ips.server;

public enum IPSServlet {

	DataUploadServlet("DataUploadServlet"), 
	PositioningServlet("PositioningServlet");

	private String servletPath;

	IPSServlet(String servletName) {

		this.servletPath = servletName;
	}

	public String getServletPath() {
		return servletPath;
	}
}
