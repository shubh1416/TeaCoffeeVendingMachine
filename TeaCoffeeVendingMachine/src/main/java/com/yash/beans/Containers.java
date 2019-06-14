package com.yash.beans;

public enum Containers {

	Tea(2000, "gm"), Coffee(2000, "gm"), Sugar(8000, "gm"), Water(15000, "ml"), Milk(10000, "ml");

	private Integer capacity;
	private String unit;

	private Containers(Integer capacity, String unit) {

		this.capacity = capacity;
		this.unit = unit;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public String getUnit() {
		return unit;
	}

}
