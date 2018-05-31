package servlet;

import java.awt.List;

/* Beispielprogramm, um WeKa (Apriori) in eclipse zu verwenden.

 Die Rohdaten liegen im CSV-Format vor. WeKa benï¿½tigt das arff-Format.

 Die Rohdaten enthalten die folgenden 24 Attribute,
 0..4  Kundendaten
 5..6  Einkaufsverhalten
 7..23 gekaufte Waren

 Hier sind  nur ein paar Beispiele zu sehen, wie man die Assoziationsanalyse verwenden kann.
 Weitere Einstellungen (falls nï¿½tig) selbst recherchieren!
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
import java.util.Set;
import java.util.TreeSet;
import java.util.Iterator; 

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

		// 0 durch ? ersetzen, um fï¿½r die Auswertung nur die Waren zu
		// berï¿½cksichtigen, die gekauft wurden
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

		// Kundendaten rausnehmen, nur Warenkï¿½rbe stehen lassen
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
		
		loeschen(filepath);
		
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

			// Header Information hinzufÃ¼gen ------------
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
			} else if(anotherInformation.equals("partner")) {
					if(items[spaltenNummer].equals("Partnerschaft")) {
						items[spaltenNummer] = "1";
					} else {
						items[spaltenNummer] = "0";
					} 
			} else if(anotherInformation.contains("tage")) {
				
				if(anotherInformation.contains("Montag")) {
					title = "Montag";
					if(items[spaltenNummer].equals("Montag")) {
						items[spaltenNummer] = "1";
					}else {
						items[spaltenNummer] = "0";
					}
				} else if(anotherInformation.contains("Dienstag")) {
					title = "Dienstag";
					if(items[spaltenNummer].equals("Dienstag")) {
						items[spaltenNummer] = "1";
					}else {
						items[spaltenNummer] = "0";
					}
				} else if(anotherInformation.contains("Mittwoch")) {
					title = "Mittwoch";
					if(items[spaltenNummer].equals("Mittwoch")) {
						items[spaltenNummer] = "1";
					}else {
						items[spaltenNummer] = "0";
					}
				} else if(anotherInformation.contains("Donnerstag")) {
					title = "Donnerstag";
					if(items[spaltenNummer].equals("Donnerstag")) {
						items[spaltenNummer] = "1";
					}else {
						items[spaltenNummer] = "0";
					}
				} else if(anotherInformation.contains("Freitag")) {
					title = "Freitag";
					if(items[spaltenNummer].equals("Freitag")) {
						items[spaltenNummer] = "1";
					}else {
						items[spaltenNummer] = "0";
					}
				} else if(anotherInformation.contains("Samstag")) {
					title = "Samstag";
					if(items[spaltenNummer].equals("Samstag")) {
						items[spaltenNummer] = "1";
					}else {
						items[spaltenNummer] = "0";
					}
				}
			} else if(anotherInformation.contains("alter")) {
				if(anotherInformation.contains("Jung")) {
					title = "18-30";
					if(items[spaltenNummer].equals("18-30")) {
						items[spaltenNummer] = "1";
					}else {
						items[spaltenNummer] = "0";
					}
				} else if(anotherInformation.contains("Mittel")) {
					title = "31-40";
					if(items[spaltenNummer].equals("31-40")) {
						items[spaltenNummer] = "1";
					}else {
						items[spaltenNummer] = "0";
					}
				} else if(anotherInformation.contains("Alt")) {
					title = "41-50";
					if(items[spaltenNummer].equals("41-50")) {
						items[spaltenNummer] = "1";
					}else {
						items[spaltenNummer] = "0";
					}
				} else if(anotherInformation.contains("Steinalt")) {
					title = "51-60";
					if(items[spaltenNummer].equals("51-60")) {
						items[spaltenNummer] = "1";
					}else {
						items[spaltenNummer] = "0";
					}
				} else if(anotherInformation.contains("ZuAlt")) {
					title = ">60";
					if(items[spaltenNummer].equals(">60")) {
						items[spaltenNummer] = "1";
					}else {
						items[spaltenNummer] = "0";
					}
				}
			} else if(anotherInformation.contains("uhrzeit")) {
				if(anotherInformation.contains("Morgen")) {
					title = "<10 Uhr";
					if(items[spaltenNummer].contains("<10 Uhr")) {
						items[spaltenNummer] = "1";
					}else {
						items[spaltenNummer] = "0";
					}
				} else if(anotherInformation.contains("Mittag")) {
					title = "10-12 Uhr";
					if(items[spaltenNummer].contains("10-12 Uhr")) {
						items[spaltenNummer] = "1";
					}else {
						items[spaltenNummer] = "0";
					}
				} else if(anotherInformation.contains("Nachmittag")) {
					title = "12-14 Uhr";
					if(items[spaltenNummer].contains("12-14 Uhr")) {
						items[spaltenNummer] = "1";
					}else {
						items[spaltenNummer] = "0";
					}
				}else if(anotherInformation.contains("Abend")) {
					title = "14-17 Uhr";
					if(items[spaltenNummer].contains("14-17 Uhr")) {
						items[spaltenNummer] = "1";
					}else {
						items[spaltenNummer] = "0";
					}
				}else if(anotherInformation.contains("Nacht")) {
					title = ">17 Uhr";
					if(items[spaltenNummer].contains(">17 Uhr")) {
						items[spaltenNummer] = "1";
					}else {
						items[spaltenNummer] = "0";
					}
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
	
	
	/**
	 * Funktion um immer genau die letzten 5 Ausgaben im Ausgabeordner zu garantieren
	 *  Macht also nur etwas ab 6 Dateien im Ausgabeordner
	 * 
	 * @param allData
	 * @return
	 */
	
	public void loeschen(String filepath) {


	//Das auszulesende Verzeichnis
	File verzeichnis = new File (filepath);

	//Inhalt von "verzeichnis" in das Array "dateien" einlesen
	String[] dateinamen = verzeichnis.list();

	int ii =0;

	String link=filepath;
	
	System.out.println("Link:" +filepath);
	/**
	 * Treeset erzeugen um die txt Dateien sortiert nach ihrem Timestamp abzuspeichern
	 */
	Set<String> liste = new TreeSet();

	//Aus allen Dateien die Txt Dateien herausfiltern
	for(String i: dateinamen)
	{

		//System.out.println(i);
		if(i.endsWith(".txt"))
		{
			liste.add(i);
		}
	}
	


	//System.out.println("Listeneinträge:" +liste.size());
	//ArrayList erzeugt
	ArrayList<String> eraser = new ArrayList<String>();
/**
 * ArrayList mit allen überschüssigen Txt Dateien befüllen
 */
	do {
	if(liste.size() >5)
	{
		ii++;
		Iterator<String> it = liste.iterator();
		eraser.add(it.next());  //erstes element des treesets abspeichern
		liste.remove(eraser.get((ii-1)));
	}

	}while(liste.size()>5);

/**
 * Dateien löschen
 *	Dateien in einer For-Schleife aufrufen und nach und nach löschen
 */
	
	String concat="";
	boolean check=false;
	for(ii=0; ii<eraser.size(); ii++)
	{
	concat=link;
	//concat+="";
	concat+=eraser.get(ii);

	//	System.out.println("Item:" +concat);
	
	
	//System.out.println(concat);
	File erasable = new File(concat);
	check= erasable.delete();

	/*
	 * Wenn Erfolgreich oder nicht, entsprechende Ausgabe
	 */
	
	if(!check) {
		System.out.println("Alte Datei gelöscht");
		

	}
	else
	{	
		/**
		 * Die höchste Nummer entspricht dem ersten Eintrag in der ArrayList und somit der Txt Datei mit dem ältesten Timestamp
		 */
		
		System.out.println("Die " +(eraser.size()-ii)+ ". Datei konnte nicht gelöscht werden");
	}

	}

	}


	}
	



