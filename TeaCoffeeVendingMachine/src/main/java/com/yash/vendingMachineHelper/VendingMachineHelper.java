package com.yash.vendingMachineHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.log4j.Logger;

import com.yash.beans.BillOfOrder;
import com.yash.beans.BlackCoffee;
import com.yash.beans.BlackTea;
import com.yash.beans.Coffee;
import com.yash.beans.Drinks;
import com.yash.beans.ItemPrice;
import com.yash.beans.Tea;

public class VendingMachineHelper {
	static Logger log = Logger.getLogger(VendingMachineHelper.class);
	ManageContainerStockHelper manageContainerStockHelper = new ManageContainerStockHelper();

	public Map<String, Integer> calculateRequiredMaterialsForDrinks(Integer typeOfDrink, Integer noOfDrinksToPrepare) {
	
		Map<String, Integer> requiredQuantityOfEachMaterial = new HashMap<>();
		List<Drinks> materialsListForParticularDrink=null;
		
		if (typeOfDrink == 1) {
			materialsListForParticularDrink = Arrays.asList(Tea.values());

		} else if (typeOfDrink == 2) {
			materialsListForParticularDrink = Arrays.asList(BlackTea.values());
	
		} else if (typeOfDrink == 3) {
			materialsListForParticularDrink = Arrays.asList(Coffee.values());
	
		} else if (typeOfDrink == 4) {
			materialsListForParticularDrink = Arrays.asList(BlackCoffee.values());
	
		} else {
			log.error("Invalid choice");
			return null;
		}
		requiredQuantityOfEachMaterial = calculateRequiredMaterialForAnyDrink(materialsListForParticularDrink,
				requiredQuantityOfEachMaterial, noOfDrinksToPrepare);

		return requiredQuantityOfEachMaterial;
	}

	public Map<String, Integer> getAvailableMaterialQuantityFromStock() {

		return manageContainerStockHelper.readRemainingMaterialStockFromJSON();
	}

	private String getDrinkNameById(String drinkType) {

		if (drinkType.equals("1"))
			return "Tea";
		else if (drinkType.equals("2"))
			return "Black Tea";
		else if (drinkType.equals("3"))
			return "Coffee";
		else
			return "Black Coffee";

	}

	public void calculateTotalBillOfOrder(Integer typeOfDrink, Integer quantityOrdered, List<BillOfOrder> billingList, Set<String> drinkContents) {

		BillOfOrder billOfOrder = new BillOfOrder();
		String itemName = getDrinkNameById(typeOfDrink.toString());

		Optional<Integer> unitPriceOfParticularDrink = Arrays.asList(ItemPrice.values()).stream()
				.filter(n -> n.getDrinkName().equalsIgnoreCase(itemName)).map(n -> n.getPriceOfDrink()).findAny();

		billOfOrder.setItemName(itemName);
		billOfOrder.setQuantityOrdered(quantityOrdered);
		billOfOrder.setUnitPrice(unitPriceOfParticularDrink.get());
		billOfOrder.setTotal(unitPriceOfParticularDrink.get() * quantityOrdered);
		billingList.add(billOfOrder);

		log.info("Item Name::: " + billOfOrder.getItemName()+"    contains :: "+drinkContents + "      Quantity ordered::: "
				+ billOfOrder.getQuantityOrdered() + "      Unit Price::: " + billOfOrder.getUnitPrice());
		log.info("Total price::::::  " + billOfOrder.getTotal());
	}

	public void calculateTotalMaterialWasted(String typeOfDrink, Integer noOfDrinksToPrepare,Map<String, Integer> quantityWastedForEachMaterial) {

		List<Drinks> materialsListOfParticularDrink=null;
		
		if (typeOfDrink.equals("Tea") ) {
			materialsListOfParticularDrink = Arrays.asList(Tea.values());

		} 
		if (typeOfDrink.equals("Black Tea")) {
			materialsListOfParticularDrink= Arrays.asList(BlackTea.values());

		} 
		if (typeOfDrink.equals("Coffee")) {
			materialsListOfParticularDrink= Arrays.asList(Coffee.values());

		}
		if (typeOfDrink.equals("Black Coffee")) {
			materialsListOfParticularDrink = Arrays.asList(BlackCoffee.values());
		}
		calculateTotalWasteMaterialForAnyDrink(materialsListOfParticularDrink, quantityWastedForEachMaterial,
				noOfDrinksToPrepare);
	}

	public void updateMaterialStockInContainers(Map<String, Integer> quantityOfMaterialsAvailableInStock) {
		try {
			manageContainerStockHelper.writeUpdatedContainerStockToJSON(quantityOfMaterialsAvailableInStock);
		} catch (Exception exception) {
			log.error("Failed in updating stock after prepapring drink ...");
		}

	}

	private Map<String, Integer> calculateRequiredMaterialForAnyDrink(List<Drinks> inputItemList,
			Map<String, Integer> requiredQuantityOfEachMaterial, Integer noOfDrinksToPrepare) {

		for (Drinks materialWithQuanity : inputItemList) {
			Integer totalQuantityRequiredForEachContent = (materialWithQuanity.getConsumptionAmount()
					+ materialWithQuanity.getWasteAmount()) * noOfDrinksToPrepare;
			requiredQuantityOfEachMaterial.put(materialWithQuanity.getMaterial(), totalQuantityRequiredForEachContent);
		}
		return requiredQuantityOfEachMaterial;
	}

	private Map<String, Integer> calculateTotalWasteMaterialForAnyDrink(List<Drinks> inputItemList,
			Map<String, Integer> quantityWastedForEachMaterial, Integer noOfDrinksToPrepare) {

			for (Drinks materialWithQuanity : inputItemList) {
				Integer wasteMaterialForEachContent = materialWithQuanity.getWasteAmount() * noOfDrinksToPrepare;
				if(quantityWastedForEachMaterial.containsKey(materialWithQuanity.getMaterial()))
				quantityWastedForEachMaterial.put(materialWithQuanity.getMaterial(), quantityWastedForEachMaterial.get(materialWithQuanity.getMaterial())+wasteMaterialForEachContent);
				else
					quantityWastedForEachMaterial.put(materialWithQuanity.getMaterial(), wasteMaterialForEachContent);
			}

	
		return quantityWastedForEachMaterial;
	}


}
