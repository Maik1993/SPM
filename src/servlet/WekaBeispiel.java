package servlet;

/* Beispielprogramm, um WeKa (Apriori) in eclipse zu verwenden.

 Die Rohdaten liegen im CSV-Format vor. WeKa benötigt das arff-Format.

 Die Rohdaten enthalten die folgenden 24 Attribute,
 0..4  Kundendaten
 5..6  Einkaufsverhalten
 7..23 gekaufte Waren

 Hier sind  nur ein paar Beispiele zu sehen, wie man die Assoziationsanalyse verwenden kann.
 Weitere Einstellungen (falls nötig) selbst recherchieren!
 */

import java.io.*;

import weka.associations.Apriori;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericCleaner;
import weka.filters.unsupervised.attribute.NumericToNominal;

public class WekaBeispiel {

	public static void main(String[] args) throws Exception {
		// Eigenen Dateipfad eintragen, nicht meinen nehmen ;-)
		String path = "WebContent/";
		String roh = path + "kd_gross.csv";
		String arffDat = path + "kd.arff";
		String dateiMod = path + "kd.model.txt";
		Instances alleDaten, nurWaren;

		// CSV-Datei laden
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(roh));
		alleDaten = loader.getDataSet();

		// 0 durch ? ersetzen, um für die Auswertung nur die Waren zu
		// berücksichtigen, die gekauft wurden
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

		// Kundendaten rausnehmen, nur Warenkörbe stehen lassen
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

}
