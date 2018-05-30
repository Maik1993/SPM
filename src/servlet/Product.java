package servlet;

public class Product implements Comparable<Product> {
	private int amount;
	private String title;

	public Product(String title, int amount) {
		this.amount = amount;
		this.title = title;
	}

	public String title() {
		return this.title;
	}

	public int amount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void increaseBy(int amountToIncrease) {
		this.amount += amountToIncrease;
	}

	public boolean equals(Product productToCompare) {
		if (this.title().equals(productToCompare.title())) {
			return true;
		}
		return false;

	}

	@Override
	public int compareTo(Product o) {
		if (this.amount() > o.amount()) {
			return -1;
		}
		if (this.amount() < o.amount()) {
			return 1;
		}
		return 0;
	}
}
