package servlet;

import java.awt.List;

/* Beispielprogramm, um WeKa (Apriori) in eclipse zu verwenden.

 Die Rohdaten liegen im CSV-Format vor. WeKa ben�tigt das arff-Format.

 Die Rohdaten enthalten die folgenden 24 Attribute,
 0..4  Kundendaten
 5..6  Einkaufsverhalten
 7..23 gekaufte Waren

 Hier sind  nur ein paar Beispiele zu sehen, wie man die Assoziationsanalyse verwenden kann.
 Weitere Einstellungen (falls n�tig) selbst recherchieren!
 */

import java.io.*;
import java.util.ArrayList;
import weka.associations.Apriori;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericCleaner;
import weka.filters.unsupervised.attribute.NumericToNominal;

public class Weka {
	public String roh;
	private final int PRODUCT_START = 7;

	public String excecuteWeka(String filepath, String fileNameWithCode, String arffFilenameWithCode,
			String txtFilenameWithCode) throws Exception {
		// Eigenen Dateipfad eintragen, nicht meinen nehmen ;-)
		String path = filepath;
		String roh = path + fileNameWithCode;

		this.setRoh(roh);

		String arffDat = path + arffFilenameWithCode;
		String dateiMod = path + txtFilenameWithCode;
		Instances alleDaten, nurWaren;

		// CSV-Datei laden
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(roh));
		alleDaten = loader.getDataSet();

		// 0 durch ? ersetzen, um f�r die Auswertung nur die Waren zu
		// ber�cksichtigen, die gekauft wurden
		NumericCleaner nc = new NumericCleaner();
		nc.setMinThreshold(1.0); // Schwellwert auf 1 setzen
		nc.setMinDefault(Double.NaN); // alles unter Schwellwert durch ?
										// ersetzen
		nc.setInputFormat(alleDaten);
		alleDaten = Filter.useFilter(alleDaten, nc); // Filter anwenden.

		// Die Daten als nominale und nicht als numerische Daten setzen
		NumericToNominal num2nom = new NumericToNominal();
		num2nom.setAttributeIndices("first-last");
		num2nom.setInputFormat(alleDaten);
		alleDaten = Filter.useFilter(alleDaten, num2nom);

		// als ARFF speichern -- in diesem Beispiel nicht notwendig
		ArffSaver saver = new ArffSaver();
		saver.setInstances(alleDaten);
		saver.setFile(new File(arffDat));
		saver.writeBatch();

		// ARFF Datei laden -- in diesem Beispiel nicht notwendig
		DataSource source = new DataSource(arffDat);
		alleDaten = source.getDataSet();

		// Apriori anwenden

		// Kundendaten rausnehmen, nur Warenk�rbe stehen lassen
		nurWaren = new Instances(alleDaten);
		for (int i = 0; i < 7; i++) {
			nurWaren.deleteAttributeAt(0); // ein einzelnes Attribut rausnehmen
		}

		Apriori model = new Apriori();
		Apriori warenModel = new Apriori();

		warenModel.setNumRules(3); // die besten drei Ergebnisse

		model.buildAssociations(alleDaten);
		warenModel.buildAssociations(nurWaren);

		//System.out.println(model);
		//System.out.println(warenModel);

		// Apriori-Auswertung in Datei speichern
		Writer fp1 = new FileWriter(dateiMod);
		fp1.write(model.toString());
		fp1.close();
		
		//-------------------------
		String zusammengekaufteWaren = warenModel.toString();
		String searchString = "1.";
		if (zusammengekaufteWaren.contains(searchString)) {
		  zusammengekaufteWaren = zusammengekaufteWaren.substring((int)zusammengekaufteWaren.indexOf(searchString), zusammengekaufteWaren.length());
		  zusammengekaufteWaren = zusammengekaufteWaren.replaceAll("<conf.{1,}conv:\\(\\d{1,}\\.\\d{1,}\\)", "<br/>");
		}
		
		return zusammengekaufteWaren;
		
	}

	public void setRoh(String roh) {
		this.roh = roh;
	}

	public String getRoh() {
		return this.roh;
	}

	public ArrayList<Product> getTop5Artikel() throws Exception {
		CSVLoader loader = this.getInitializedLoader();
		Instances allData = loader.getDataSet();
		ProductList productList = this.generateProductList(allData);
		int topListLength = 5;
		ArrayList<Product> top5 = productList.getTopProducts(topListLength);
		
		return top5;
	}

	private CSVLoader getInitializedLoader() {
		// CSV-Datei laden
		CSVLoader loader = new CSVLoader();
		try {
			String roh = this.getRoh();

			loader.setSource(new File(roh));

			// Header Information hinzufügen ------------
			// -siehe Doku
			// http://weka.sourceforge.net/doc.dev/weka/core/converters/CSVLoader.html
			String[] options = new String[1];
			options[0] = "-H";
			loader.setOptions(options);
		} catch (Exception ex) {
			System.err.println("Achtung!CSV-Loader konnte nicht geladen werden");
			System.err.println(ex.getMessage());
		}
		return loader;
	}
	
	public ArrayList<Product> getEineSpalte(int spaltenNummer, String anotherInformation) throws Exception {
		CSVLoader loader = this.getInitializedLoader();
		Instances allData = loader.getDataSet();
		
		ProductList spalte = this.generateSpalte(allData, spaltenNummer, anotherInformation);
		ArrayList<Product> spaltenanteil = spalte.getSpalteErgebnis();
		
		return spaltenanteil;
		
	}

	private ProductList generateProductList(Instances allData) {
		ArrayList<String> titleList = new ArrayList<String>();
		String headerRow = allData.get(0).toString();
		String[] headerRowAsArray = headerRow.split(",");
		for (int headerIndex = this.PRODUCT_START; headerIndex < headerRowAsArray.length; headerIndex++) {
			String title = headerRowAsArray[headerIndex];
			titleList.add(title);
		}

		ProductList productList = new ProductList();

		for (int i = 1; i < allData.size(); i++)
		{
			String ganzeZeile = allData.get(i).toString();
			String[] items = ganzeZeile.split(",");
			for (int
					productCounterIndex = this.PRODUCT_START; 
					productCounterIndex < items.length; 
					productCounterIndex++) 
			{
				String title = titleList.get(productCounterIndex - this.PRODUCT_START);
				Product productToAdd = new Product(title, Integer.parseInt(items[productCounterIndex]));
				productList.add(productToAdd);
			}
		}

		return productList;
	}
	
	private ProductList generateSpalte(Instances allData, int spaltenNummer, String anotherInformation) {
		ArrayList<String> titleList = new ArrayList<String>();
		String headerRow = allData.get(0).toString();
		String[] headerRowAsArray = headerRow.split(",");
			String title = headerRowAsArray[spaltenNummer];
			titleList.add(title);

		ProductList productList = new ProductList();

		for (int i = 1; i < allData.size(); i++)
		{
			String ganzeZeile = allData.get(i).toString();
			String[] items = ganzeZeile.split(",");
			
			//Besondere Informationen in den Datensaetzen anstatt 0 und 1
			if(anotherInformation.equals("m")) {
				if(items[spaltenNummer].equals("m")) {
					items[spaltenNummer] = "1";
				} else {
					items[spaltenNummer] = "0";
				} 
			} else if(anotherInformation.equals("w")) {
				if(items[spaltenNummer].equals("w")) {
					items[spaltenNummer] = "1";
				} else {
					items[spaltenNummer] = "0";
				} 
			} else if((anotherInformation.equals("kinder")) || (anotherInformation.equals("beruf"))) {
				if(items[spaltenNummer].equals("ja")) {
					items[spaltenNummer] = "1";
				} else {
					items[spaltenNummer] = "0";
				} 
			}
			
					Product productToAdd = new Product(title, Integer.parseInt(items[spaltenNummer]));
					productList.add(productToAdd);
			
		}

		return productList;
	}
	
	public int getAnzahlDatensaetze() throws Exception {
		int anzahlDatensaetze = 0;
		CSVLoader loader = this.getInitializedLoader();
		Instances allData = loader.getDataSet();
		//-1 wegen der HeaderInfomationen
		anzahlDatensaetze = allData.size() - 1;
		
		return anzahlDatensaetze;
		
	}

}
