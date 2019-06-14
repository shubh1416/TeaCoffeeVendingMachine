package com.yash.beans;

public enum Coffee implements Drinks {

	COFFEE("Coffee", 4, 1), WATER("Water", 20, 3), MILK("Milk", 80, 8), SUGER("Sugar", 15, 2);

	private String material;
	private Integer consumptionAmount;
	private Integer wasteAmount;

	public String getMaterial() {
		return material;
	}

	public Integer getConsumptionAmount() {
		return consumptionAmount;
	}

	public Integer getWasteAmount() {
		return wasteAmount;
	}

	private Coffee(String material, Integer consumptionAmount, Integer wasteAmount) {
		this.material = material;
		this.consumptionAmount = consumptionAmount;
		this.wasteAmount = wasteAmount;
	}

}
