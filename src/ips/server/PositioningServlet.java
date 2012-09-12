package ips.server;

import ips.algorithm.Context;
import ips.algorithm.PositioningAlgorithmType;
import ips.algorithm.PositioningResult;
import ips.algorithm.knn.NNResults;
import ips.data.DataManager;
import ips.data.entities.wlan.WLANFingerprint;
import ips.data.serialization.Serializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PositioningServlet
 */
@WebServlet("/PositioningServlet")
public class PositioningServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Context context;

	private Serializer sz;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PositioningServlet() {
		super();

		context = new Context(new DataManager());

		sz = Serializer.getInstance();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		out.println("Indoor Positioning Get Location API");
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
			// Measurement measurement = (Measurement)
			// sz.deserialize(Measurement.class, data);
			PositioningRequest req = (PositioningRequest) sz.deserialize(
					PositioningRequest.class, data);

			WLANFingerprint measurement = req.fp;
			PositioningAlgorithmType algoType = req.algorithm;

			context.setPositioningAlgorithm(algoType);

			// Calculate the position from the measurements

			String xmlRes = "";

			switch (algoType) {
			case NearestNeighbors:
				NNResults posRes = (NNResults) context
						.calculatePosition(measurement);
				xmlRes = Serializer.getInstance().serializeToXML(posRes);
				break;
			case BayesPositioning:
				PositioningResult posRes1 = context
						.calculatePosition(measurement);
				xmlRes = Serializer.getInstance().serializeToXML(posRes1);
				break;
			}

			// Prepare the response
			// Set content type to json
			response.setHeader("content-type", "text/xml");
			// OutputStream outStream = response.getOutputStream();

			// outStream.write(new ByteArrayOutputStream().w

			PrintWriter out = response.getWriter();

			out.write(xmlRes);

			// out.println(xmlPos);

			out.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
