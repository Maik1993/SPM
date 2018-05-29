package servlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductList {
	private List<Product> productList;

	public ProductList() {
		this.productList = new ArrayList<Product>();
		// TODO Auto-generated constructor stub
	}

	public void add(Product productToAdd) {
		boolean alreadyInserted = false;
		for (Product product : this.productList) {
			if (product.equals(productToAdd)) {
				product.increaseBy(productToAdd.amount());
				alreadyInserted = true;
			}
		}

		if (!alreadyInserted) {
			this.productList.add(productToAdd);
		}
	}
	
	public void add2(Product productToAdd) {
		boolean alreadyInserted = false;
		for (Product product : this.productList) {
			if (product.equals(productToAdd)) {
				product.increaseBy(productToAdd.amount());
				alreadyInserted = true;
			}
		}

		if (!alreadyInserted) {
			this.productList.add(productToAdd);
		}
	}

	public int size() {
		return this.productList.size();
	}

	public Product get(int index) {
		if (index >= this.size()) {
			throw new IllegalArgumentException("Achtung! Index ist höher als Liste lang ist!");
		}

		return this.productList.get(index);
	}

	public void sortProducts() {
		Collections.sort(this.productList);
	}

	public ArrayList<Product> getTopProducts(int topProductListLength) {
		this.sortProducts();
		ArrayList<Product> topProductsSorted = new ArrayList<Product>();
		for (int i = 0; i < topProductListLength; i++) {
			topProductsSorted.add(this.productList.get(i));
		}

		return topProductsSorted;
	}
	
	public ArrayList<Product> getSpalteErgebnis() {
		this.sortProducts();
		ArrayList<Product> spaltesortedErgebnis = new ArrayList<Product>();
		for (int i = 0; i < this.productList.size(); i++) {
			spaltesortedErgebnis.add(this.productList.get(i));
		}

		return spaltesortedErgebnis;
	}

}
