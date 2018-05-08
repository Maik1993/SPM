package servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/FileUploader")
@MultipartConfig(fileSizeThreshold = 6291456, // 6 MB
		maxFileSize = 10485760L, // 10 MB
		maxRequestSize = 20971520L // 20 MB
)
public class FileUploader extends HttpServlet {
	private static final long serialVersionUID = 5619951677845873534L;
	
	private static final String UPLOAD_DIR = "uploads";
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		// gets absolute path of the web application
		String applicationPath = request.getServletContext().getRealPath("");
		
		// constructs path of the directory to save uploaded file
		String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
		// creates upload folder if it does not exists
		File uploadFolder = new File(uploadFilePath);
		if (!uploadFolder.exists()) {
			uploadFolder.mkdirs();
		}
		PrintWriter writer = response.getWriter();
		// write all files in upload folder
		for (Part part : request.getParts()) {
			if (part != null && part.getSize() > 0) {
				String fileName = part.getSubmittedFileName();
				String contentType = part.getContentType();
				
				Timestamp tstamp = new Timestamp(System.currentTimeMillis());
				long timestamp = tstamp.getTime();
				String fileNameWithCode = timestamp+"_"+fileName;
				String arffFilenameWithCode = timestamp+"_"+"kd.arff";
				String txtFilenameWithCode = timestamp+"_"+"kd.model.txt";
				
				
				// allows only CSV files to be uploaded
//				if (!contentType.equalsIgnoreCase("application/ms-excel")) {
//					csvType = true;
//				}
				
				part.write(uploadFilePath + File.separator + fileNameWithCode);
				
				String filepath = uploadFolder.getAbsolutePath() + File.separator;
				String escapedFilepath = filepath.replace("\\", "/");
				
				System.out.println(escapedFilepath);
				
				Weka weka = new Weka();
				try {
					weka.excecuteWeka(escapedFilepath, fileNameWithCode, arffFilenameWithCode, txtFilenameWithCode);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				RequestDispatcher req = request.getRequestDispatcher("startseite.jsp");
				req.forward(request, response);
				

			}
		}
	}
}