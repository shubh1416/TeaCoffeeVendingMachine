package com.yash.vendingMachine;

import java.util.List;

import java.util.Map;

import com.yash.beans.BillOfOrder;

public interface TeaCoffeeVendingMachine {

	public Boolean prepareDrink(Integer typeOfDrink, Integer quantityOrdered, List<BillOfOrder> billingList);

	public Integer refillContainer(Map<String, Integer> containerStockToUpdate, Integer countOfContainerRefill);

	public void prepareReport(Integer reportOption, List<BillOfOrder> billingList, Integer countOfContainerRefill);

	public void getContainerStatus() ;

	public Boolean reset();

}
