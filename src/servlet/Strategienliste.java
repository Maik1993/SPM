package servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileNotFoundException;

public class Strategienliste {
	private ArrayList<String> list;
	
	public Strategienliste() {
		list = new ArrayList<String>();
		try {
			list = load();
		} catch (FileNotFoundException f) {
			System.out.println("Nicht gefunden");
			File file = new File("Marketingstrategien.txt");
			try {
				if(!file.exists()) {
				file.createNewFile();
				System.out.println("Erstellt");
				} else {
					System.out.println("Vorhanden");
				}
			} catch (IOException e) {
				System.out.println("Nicht neu erzeugt");
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addStrategy(String strategy) {
		if(!list.contains(strategy)) {
			list.add(strategy);
			try {
				save();
			} catch (IOException e) {
				System.out.println("Fehler");
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<String> getList() {
		return list;
	}
	
	private void save() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter("Marketingstrategien.txt", false));
		for(String line:list) {
			bw.write(line);
			bw.newLine();
		}
		bw.close();
	}
	
	private ArrayList<String> load() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("Marketingstrategien.txt"));
		ArrayList<String> ladeliste = new ArrayList<>();
		String line = null;
		while((line = br.readLine()) != null) {
			ladeliste.add(line);
		}
		br.close();
		return ladeliste;
	}
}
