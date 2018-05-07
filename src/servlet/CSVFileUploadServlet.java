package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CSVFileUploadServlet
 */
@WebServlet("/CSVFileUploadServlet")
public class CSVFileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CSVFileUploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//in your jsp form tag add enctype="multipart/form-data" method="post"
		//In servlet, you need to get content type first.
		//content-type  (nach dem Namen des Feldes) ähnlich mime type
		//gibt darüber Auskunft, welche Daten an den Webserver gesendet werden
		String contentType = request.getContentType();
		
		System.out.println(request.getInputStream());
		//now this contentType contains multipart data
		//multipart: Dieser Typ gibt einen Hinweis auf Dateien, die aus mehreren Teilen bestehen.
		if (contentType.toLowerCase().contains("multipart")) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String data= null;
		do {

		    // skip first header lines which contains the header data such as Content-Disposition or Content-Type
		        data = reader.readLine(); 
		    } while(!(data.toLowerCase().contains("content-type")));

		    while( (data = reader.readLine()) != null) {
		        if(data.startsWith("--")) {
		            continue;
		        }                   

		        if (!data.toLowerCase().contains("webkitformboundary")) {
		            if (!data.toLowerCase().contains("content-disposition")) {
		                System.out.println("+++++++++++++++++++ data: "+data);
		            }
		        }
		    }
		}
	}

}
