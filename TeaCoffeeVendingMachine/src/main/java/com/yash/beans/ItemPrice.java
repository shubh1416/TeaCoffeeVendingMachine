package com.yash.beans;

public enum ItemPrice {

	TEA("Tea", 10), BLACK_TEA("Black Tea", 5), COFFEE("Coffee", 15), BLACK_COFFEE("Black Coffee", 10);

	private String drinkName;
	private Integer priceOfDrink;

	private ItemPrice(String drinkName, Integer priceOfDrink) {
		this.drinkName = drinkName;
		this.priceOfDrink = priceOfDrink;
	}

	public String getDrinkName() {
		return drinkName;
	}

	public Integer getPriceOfDrink() {
		return priceOfDrink;
	}

}
