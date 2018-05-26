package servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Strategienliste {
	private ArrayList<String> list;
	
	public Strategienliste() {
		list = new ArrayList<String>();
		try {
			list = load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
