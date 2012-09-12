package ips.server;

import ips.data.serialization.Serializer;
import ips.data.upload.DataUploader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DataUploadServlet
 */
@WebServlet("/DataUploadServlet")
public class DataUploadServlet extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 1L;

	private Serializer sz;
	private DataUploader du;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DataUploadServlet() {

		super();

		sz = Serializer.getInstance();
		du = new DataUploader();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		out.println("Indoor Positioning Data Uploader API");
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		BufferedReader reader = request.getReader();

		// Read the xml from the request
		StringBuilder sb = new StringBuilder();
		String line = reader.readLine();
		while (line != null) {
			sb.append(line + "\n");
			line = reader.readLine();
		}
		reader.close();
		String data = sb.toString();

		try {
			DataUploadRequest uploadRequest = (DataUploadRequest) sz
					.deserialize(DataUploadRequest.class, data);

			if (uploadRequest.doClearData()) {

				du.clearData();
			}

			du.upload(uploadRequest);

			// Prepare the response
			// Set content type to xml
			response.setHeader("content-type", "text/xml");

			PrintWriter out = response.getWriter();

			// TODO
			out.write("ok");

			out.flush();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
