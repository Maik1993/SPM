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
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Nicht neu erzeugt");
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addStrategy(String strategy) throws IOException {
		list.add(strategy);
		save();
	}
	
	public ArrayList<String> getList() {
		return list;
	}
	
	private void save() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter("Marketingstrategien"));
		for(String line:list) {
			bw.write(line + "\n");
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
