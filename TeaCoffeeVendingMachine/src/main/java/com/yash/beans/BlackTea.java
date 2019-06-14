package com.yash.beans;

public enum BlackTea implements Drinks {
	TEA("Tea", 3, 0), WATER("Water", 100, 12), SUGER("Sugar", 15, 2);

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

	private BlackTea(String material, Integer consumptionAmount, Integer wasteAmount) {
		this.material = material;
		this.consumptionAmount = consumptionAmount;
		this.wasteAmount = wasteAmount;
	}

}
