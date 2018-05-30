package servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet("/FileUploader")
@MultipartConfig(fileSizeThreshold = 6291456, // 6 MB
		maxFileSize = 10485760L, // 10 MB
		maxRequestSize = 20971520L // 20 MB
)

/**
 * Klasse fuer den File Uploader
 * CSV Datei Analyse durch Weka
 * @author Just Do it
 *
 */
public class FileUploader extends HttpServlet {
	private static final long serialVersionUID = 5619951677845873534L;
	
	private static final String UPLOAD_DIR = "uploads";
	
	/**
	 * Get Parameter Methode
	 * wird hier nicht gebraucht
	 * @param request
	 * @param response
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}
	
	/**
	 * CSV Datei Analyse
	 * Csv Datei wird vom FileUplaoder gelesen, abgelegt im Dateipfad und mit
	 * Weka analysiert
	 * @param request
	 * @param response
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true); 
		session.setAttribute("noData", false);
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
				
				
				part.write(uploadFilePath + File.separator + fileNameWithCode);
				
				String filepath = uploadFolder.getAbsolutePath() + File.separator;
				String escapedFilepath = filepath.replace("\\", "/");
				
				System.out.println(escapedFilepath);
				
				Weka weka = new Weka();
				ArrayList<Product> top5 = new ArrayList<Product>();
				ArrayList<Product> maenneranteil = new ArrayList<Product>();
				ArrayList<Product> frauenanteil = new ArrayList<Product>();
				ArrayList<Product> kinderanteil = new ArrayList<Product>();
				ArrayList<Product> berufsanteil = new ArrayList<Product>();
				ArrayList<Product> partneranteil = new ArrayList<Product>();
				//Tageanteil -----------
				ArrayList<Product> montaganteil = new ArrayList<Product>();
				ArrayList<Product> dienstaganteil = new ArrayList<Product>();
				ArrayList<Product> mittwochanteil = new ArrayList<Product>();
				ArrayList<Product> donnerstaganteil = new ArrayList<Product>();
				ArrayList<Product> freitaganteil = new ArrayList<Product>();
				ArrayList<Product> samstaganteil = new ArrayList<Product>();
				ArrayList<Product> einkaufstaganteil = new ArrayList<Product>();
				//------------ 
				//Alteranteil-----
				ArrayList<Product> alterjunganteil = new ArrayList<Product>();
				ArrayList<Product> altermittelanteil = new ArrayList<Product>();
				ArrayList<Product> alteraltanteil = new ArrayList<Product>();
				ArrayList<Product> altersteinanteil = new ArrayList<Product>();
				ArrayList<Product> alterzualtanteil = new ArrayList<Product>();
				ArrayList<Product> alteranteil = new ArrayList<Product>();
				//----------
				//Uhrzeitanteil-----
				ArrayList<Product> morgensnteil = new ArrayList<Product>();
				ArrayList<Product> mittagsanteil = new ArrayList<Product>();
				ArrayList<Product> nachmittagsanteil = new ArrayList<Product>();
				ArrayList<Product> abendanteil = new ArrayList<Product>();
				ArrayList<Product> nachtanteil = new ArrayList<Product>();
				ArrayList<Product> uhrzeitanteil = new ArrayList<Product>();
				//----------
				
				int anzahlDatensatze = 0;
				String zusammengekaufteWaren = "";
				try {
					zusammengekaufteWaren = weka.excecuteWeka(escapedFilepath, fileNameWithCode, arffFilenameWithCode, txtFilenameWithCode);
					anzahlDatensatze = weka.getAnzahlDatensaetze();
					top5 = weka.getTop5Artikel();
					maenneranteil = weka.getEineSpalte(0, "m");
					frauenanteil = weka.getEineSpalte(0, "w");
					kinderanteil = weka.getEineSpalte(2, "kinder");
					berufsanteil = weka.getEineSpalte(4, "beruf");
					partneranteil = weka.getEineSpalte(3, "partner");
					
					//Tageanteil
					montaganteil = weka.getEineSpalte(5, "tageMontag");
					dienstaganteil = weka.getEineSpalte(5, "tageDienstag");
					mittwochanteil = weka.getEineSpalte(5, "tageMittwoch");
					donnerstaganteil = weka.getEineSpalte(5, "tageDonnerstag");
					freitaganteil = weka.getEineSpalte(5, "tageFreitag");
					samstaganteil = weka.getEineSpalte(5, "tageSamstag");
					
					einkaufstaganteil.addAll(montaganteil);
					einkaufstaganteil.addAll(dienstaganteil);
					einkaufstaganteil.addAll(mittwochanteil);
					einkaufstaganteil.addAll(donnerstaganteil);
					einkaufstaganteil.addAll(freitaganteil);
					einkaufstaganteil.addAll(samstaganteil);
					//---------------
					
					//Alteranteil-----
					alterjunganteil = weka.getEineSpalte(1, "alterJung");
					altermittelanteil = weka.getEineSpalte(1, "alterMittel");
					alteraltanteil = weka.getEineSpalte(1, "alterAlt");
					altersteinanteil = weka.getEineSpalte(1, "alterSteinalt");
					alterzualtanteil = weka.getEineSpalte(1, "alterZuAlt");
					
					alteranteil.addAll(alterjunganteil);
					alteranteil.addAll(altermittelanteil);
					alteranteil.addAll(alteraltanteil);
					alteranteil.addAll(altersteinanteil);
					alteranteil.addAll(alterzualtanteil);
					//----------
					
					//Uhrzeitanteil-----
					morgensnteil = weka.getEineSpalte(6, "uhrzeitMorgen");
					mittagsanteil = weka.getEineSpalte(6, "uhrzeitMittag");
					nachmittagsanteil = weka.getEineSpalte(6, "uhrzeitNachmittag");
					abendanteil = weka.getEineSpalte(6, "uhrzeitAbend");
					nachtanteil = weka.getEineSpalte(6, "uhrzeitNacht");
					
					uhrzeitanteil.addAll(morgensnteil);
					uhrzeitanteil.addAll(mittagsanteil);
					uhrzeitanteil.addAll(nachmittagsanteil);
					uhrzeitanteil.addAll(abendanteil);
					uhrzeitanteil.addAll(nachtanteil);
					//----------
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Error encountered");
					RequestDispatcher req = request.getRequestDispatcher("startseite.jsp");
					
					session.setAttribute("wrongData", true);
					req.forward(request, response);
				}
				
				request.setAttribute("anzahlDaten", anzahlDatensatze);
				request.setAttribute("zusammengekaufteWaren", zusammengekaufteWaren);
				request.setAttribute("top5Artikel", top5);
				request.setAttribute("maenneranteil", maenneranteil);
				request.setAttribute("frauenanteil", frauenanteil);
				request.setAttribute("kinderanteil", kinderanteil);
				request.setAttribute("berufsanteil", berufsanteil);
				request.setAttribute("einkaufsanteil", einkaufstaganteil);
				request.setAttribute("partneranteil", partneranteil);
				request.setAttribute("alteranteil", alteranteil);
				request.setAttribute("uhrzeit", uhrzeitanteil);
				
				RequestDispatcher req = request.getRequestDispatcher("statistik.jsp");
				session.setAttribute("wrongData", false);
				session.setAttribute("noData", false);
				req.forward(request, response);
			}else {
				RequestDispatcher req = request.getRequestDispatcher("startseite.jsp");
				
				session.setAttribute("noData", true);
				req.forward(request, response);
			}
		}
	}
}