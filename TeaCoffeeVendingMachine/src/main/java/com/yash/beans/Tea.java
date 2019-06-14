package com.yash.beans;

public enum Tea implements Drinks {

	TEA("Tea", 5, 1), WATER("Water", 60, 5), MILK("Milk", 40, 4), SUGER("Sugar", 15, 2);

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

	private Tea(String material, Integer consumptionAmount, Integer wasteAmount) {
		this.material = material;
		this.consumptionAmount = consumptionAmount;
		this.wasteAmount = wasteAmount;
	}

}
