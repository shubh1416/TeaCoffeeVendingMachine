package com.yash.VendingMachineImpl;

import com.yash.vendingMachineHelper.ManageContainerStockHelper;


import com.yash.vendingMachineHelper.VendingMachineHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.yash.beans.BillOfOrder;
import com.yash.beans.Containers;
import com.yash.vendingMachine.TeaCoffeeVendingMachine;

public class TeaCoffeeVendingMachineImpl implements TeaCoffeeVendingMachine {

	static Logger log = Logger.getLogger(TeaCoffeeVendingMachineImpl.class);

	VendingMachineHelper vendingMachineHelper = new VendingMachineHelper();
	ManageContainerStockHelper manageContainerStockHelper = new ManageContainerStockHelper();

	public Boolean prepareDrink(Integer typeOfDrink, Integer quantityOrdered, List<BillOfOrder> billingList) {

		Map<String, Integer> requiredQuantityOfMaterials = vendingMachineHelper.calculateRequiredMaterialsForDrinks(typeOfDrink, quantityOrdered);
		Map<String, Integer> quantityOfMaterialsAvailableInStock = vendingMachineHelper.getAvailableMaterialQuantityFromStock();
		Integer countOfInsufficientMaterials = 0;
		Set<String> drinkContents=requiredQuantityOfMaterials.keySet();
		
		for (Map.Entry<String, Integer> materialsList : requiredQuantityOfMaterials.entrySet()) {

			Integer availableStockOfPaticularMaterial = quantityOfMaterialsAvailableInStock.get(materialsList.getKey());
			
			if (materialsList.getValue() > availableStockOfPaticularMaterial) {
				log.info(materialsList.getKey() + "  is not sufficient in stock   --->   Required ::: "+ materialsList.getValue() + "    Available::: "+ quantityOfMaterialsAvailableInStock.get(materialsList.getKey()));
				countOfInsufficientMaterials++;
			} else {
				quantityOfMaterialsAvailableInStock.put(materialsList.getKey(),
						availableStockOfPaticularMaterial - materialsList.getValue());
			}
		}
		if (countOfInsufficientMaterials > 0) {
			log.info("Kindly refill the container...");
			return false;
		} else {
			vendingMachineHelper.calculateTotalBillOfOrder(typeOfDrink, quantityOrdered, billingList, drinkContents);
			vendingMachineHelper.updateMaterialStockInContainers(quantityOfMaterialsAvailableInStock);
		}
		return true;
	}

	public Integer refillContainer(Map<String, Integer> containerStockToUpdate, Integer countOfContainerRefill) {

		Map<String, Integer> quantityOfMaterialsAvailableInStock = vendingMachineHelper.getAvailableMaterialQuantityFromStock();

		Map<String, Integer> containerUpdatedStock = checkAndRefillContainer(containerStockToUpdate,quantityOfMaterialsAvailableInStock);

		try {
			manageContainerStockHelper.writeUpdatedContainerStockToJSON(containerUpdatedStock);
			
			return ++countOfContainerRefill;
		} catch (Exception exception) {
			return countOfContainerRefill;
		}
	}

	public void prepareReport(Integer reportType, List<BillOfOrder> billingList, Integer refillContainerCount) {

		Map<String, Integer> itemNameWithQuantity = billingList.stream().collect(Collectors.groupingBy(BillOfOrder::getItemName, Collectors.summingInt(n -> n.getQuantityOrdered())));

		if (reportType == 1) {
			
			Map<String, Integer> itemNameWithTotal = billingList.stream().collect(Collectors.groupingBy(BillOfOrder::getItemName, Collectors.summingInt(n -> n.getTotal())));
			log.info("Product Name            No of cups          Total amount    ");				
			itemNameWithTotal.entrySet().forEach(item->log.info(item.getKey() + "                       " + itemNameWithQuantity.get(item.getKey())+ "                        " + item.getValue()));
		}
		else if (reportType == 2) {

			Integer totalSales = billingList.stream().map(n -> n.getTotal()).reduce((n1, n2) -> n1 + n2).orElse(0);
			Integer totalCups = billingList.stream().map(n -> n.getQuantityOrdered()).reduce((n1, n2) -> n1 + n2).orElse(0);
			log.info("Total sales of all drinks ::: " + totalCups + " cups     Amount ::: " + totalSales+" Rs.");
		} 
		else if (reportType == 3) {
			
			log.info("Container refilled for ::::  " + refillContainerCount + " times");
		}
		else if(reportType == 4){
			
			Map<String, Integer> quantityWastedForEachMaterial = new HashMap<>();	
		
			for(Map.Entry<String, Integer> drinkNameWithQuantity : itemNameWithQuantity.entrySet()) {
				vendingMachineHelper.calculateTotalMaterialWasted(drinkNameWithQuantity.getKey(), drinkNameWithQuantity.getValue(),quantityWastedForEachMaterial);
			}
			System.out.println();
			log.info("Total ingredients wasted  ::::  " + quantityWastedForEachMaterial);
		}	
		else {
			log.info("---------------Kindly enter a valid report type ----------------");
		}
	}

	public void getContainerStatus() {
	
		Supplier<Map<String,Integer>> containerStock=manageContainerStockHelper::readRemainingMaterialStockFromJSON;
		
		log.info("Current Container Status is as follows :::::::");
		
		for (Map.Entry<String, Integer> quantity : containerStock.get().entrySet()) {
			if (quantity.getKey().equals("Water") || quantity.getKey().equals("Milk"))
				log.info(quantity.getKey() + " :: " + quantity.getValue() + " ml");
			else
				log.info(quantity.getKey() + " :: " + quantity.getValue() + " gm");
		}

	}

	public Boolean reset() {
		Map<String, Integer> containerUpdatedStock = new HashMap<>();
		containerUpdatedStock.put("Tea", 2000);
		containerUpdatedStock.put("Water", 15000);
		containerUpdatedStock.put("Coffee", 2000);
		containerUpdatedStock.put("Milk", 10000);
		containerUpdatedStock.put("Sugar", 8000);
		try {
			manageContainerStockHelper.writeUpdatedContainerStockToJSON(containerUpdatedStock);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	private Map<String, Integer> checkAndRefillContainer(Map<String, Integer> containerStockToUpdate,
			Map<String, Integer> quantityOfMaterialsAvailableInStock) {

		Map<String, Integer> containerNewUpdatedStock = new HashMap<>();

		for (Map.Entry<String, Integer> availableQuantityOfMaterialInStock : quantityOfMaterialsAvailableInStock.entrySet()) {

			Integer updatedAmountOfMaterial = availableQuantityOfMaterialInStock.getValue()+ containerStockToUpdate.get(availableQuantityOfMaterialInStock.getKey());
			Integer containerCapacityOfMaterial = Containers.valueOf(availableQuantityOfMaterialInStock.getKey()).getCapacity();

			if ((updatedAmountOfMaterial) < containerCapacityOfMaterial) {
				containerNewUpdatedStock.put(availableQuantityOfMaterialInStock.getKey(), updatedAmountOfMaterial);
				log.info("Stock of  " + availableQuantityOfMaterialInStock.getKey()+ " updated successfully New Stock:::::" + updatedAmountOfMaterial + "  "+ Containers.valueOf(availableQuantityOfMaterialInStock.getKey()).getUnit());
			} else {
				containerNewUpdatedStock.put(availableQuantityOfMaterialInStock.getKey(),availableQuantityOfMaterialInStock.getValue());
				log.info(availableQuantityOfMaterialInStock.getKey()+ "  Amount exceeded the Capacity ::: Available space:::"
						+ (containerCapacityOfMaterial - availableQuantityOfMaterialInStock.getValue()) + "  "
						+ Containers.valueOf(availableQuantityOfMaterialInStock.getKey()).getUnit());

			}
		}
		return containerNewUpdatedStock;
	}

}
