package com.yash.VendingMachineImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yash.beans.BillOfOrder;
import com.yash.util.MyScanner;
import com.yash.vendingMachine.TeaCoffeeVendingMachine;

public class PerformOperation {
	static Logger log = Logger.getLogger(PerformOperation.class);
	static List<BillOfOrder> billingList = new ArrayList<>();
	static Integer countOfContainerRefill = 0;
	

	 MyScanner scanner = new MyScanner();
	TeaCoffeeVendingMachine teaCoffeeVendingMachine = new TeaCoffeeVendingMachineImpl();

	public Integer performVendingMachinOperations(Integer selectedOption) {

		Integer input = 1;
		switch (selectedOption) {
		
		case 1:
			log.info("Please select your drink from below list");
			System.out.println();
			log.info("1. Tea     2. Black Tea     3. Coffee	   4. Black Coffee");
			Integer typeOfDrink = scanner.nextInt();
			if (typeOfDrink < 1 || typeOfDrink > 4) {
				log.info("Invalid choice of drink......");
				return 1;
			}
			log.info("Enter the quantity");
			Integer quantityOrdered = scanner.nextInt();

			teaCoffeeVendingMachine.prepareDrink(typeOfDrink, quantityOrdered, billingList);
			break;
		
		case 2:
			teaCoffeeVendingMachine.getContainerStatus();
			Map<String, Integer> containerStockToUpdate = new HashMap<>();
			System.out.println();
			log.info("Please enter amount to materials to refill containers");
			log.info("Enter amount of Tea in gm");
			containerStockToUpdate.put("Tea", scanner.nextInt());
			log.info("Enter amount of Coffee in gm");
			containerStockToUpdate.put("Coffee", scanner.nextInt());
			log.info("Enter amount of Sugar in gm");
			containerStockToUpdate.put("Sugar", scanner.nextInt());
			log.info("Enter amount of Water in ml");
			containerStockToUpdate.put("Water", scanner.nextInt());
			log.info("Enter amount of milk in ml");
			containerStockToUpdate.put("Milk", scanner.nextInt());

			countOfContainerRefill=teaCoffeeVendingMachine.refillContainer(containerStockToUpdate, countOfContainerRefill);
			
		
			break;
		
		case 3:
			log.info("Please select type of report from below Options ");
			log.info("1. Total  Sales Report Drink Wise");
			log.info("2. Total Sales Report");
			log.info("3. Refilling count of container");
			log.info("4. Total Material wasted");
			Integer reportOption = scanner.nextInt();
			
			teaCoffeeVendingMachine.prepareReport(reportOption, billingList, countOfContainerRefill);
			break;
		
		case 4:
			
			teaCoffeeVendingMachine.getContainerStatus();
			break;
		
		case 5:	
			Boolean isResetSuccessful = teaCoffeeVendingMachine.reset();
			
			if (isResetSuccessful)
				log.info("Container reset successful::::");
			else
				log.info("Some error occured:: Kindly restart the system");
			break;
		
		case 6:	
			teaCoffeeVendingMachine.reset();
			input = 2;
			break;
	
		default:	
			log.info("Invalid choice....Kindly choose correct Action");
			input = 1;
			break;
		}

		return input;

	}
}
