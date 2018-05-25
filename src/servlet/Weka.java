package servlet;

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

	public void excecuteWeka(String filepath, String fileNameWithCode, String arffFilenameWithCode,
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

		System.out.println(model);
		System.out.println(warenModel);

		// Apriori-Auswertung in Datei speichern
		Writer fp1 = new FileWriter(dateiMod);
		fp1.write(model.toString());
		fp1.close();

	}

	public void setRoh(String roh) {
		this.roh = roh;
	}

	public String getRoh() {
		return this.roh;
	}

	public ArrayList<Product> getTop5Artikel() throws Exception {
		CSVLoader loader = this.getInitializedLoader();
		// -----------------------------------------
		Instances allData = loader.getDataSet();
		ProductList productList = this.generateProductList(allData);
		int topListLength = 5;
		ArrayList<Product> top5 = productList.getTopProducts(topListLength);
		
//		int place = 1;
//		System.out.println("Top " + topListLength);
//		for (Product topProducts : top5) {
//			System.out.println("Platz " + place + " " + topProducts.title() + ": " + topProducts.amount());
//			place++;
//		}
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

}
