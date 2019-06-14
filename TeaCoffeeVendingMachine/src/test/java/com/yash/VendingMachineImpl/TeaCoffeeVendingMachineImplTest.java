
package com.yash.VendingMachineImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

import com.yash.beans.BillOfOrder;
import com.yash.vendingMachineHelper.ManageContainerStockHelper;
import com.yash.vendingMachineHelper.VendingMachineHelper;

@RunWith(MockitoJUnitRunner.class)
public class TeaCoffeeVendingMachineImplTest {

	@InjectMocks
	private TeaCoffeeVendingMachineImpl teaCoffeeVendingMachineImpl;

	@Mock
	private VendingMachineHelper vendingMachineHelper;

	@Mock
	private ManageContainerStockHelper manageContainerStockHelper;

	@Mock
	  private Appender appenderMock;
	    
	@Before
	    public void setupAppender() {
	        appenderMock = Mockito.mock(Appender.class);
	        Logger.getRootLogger().addAppender(appenderMock);
	    }
	  
	 @After
	    public void removeAppender() {
	        Logger.getRootLogger().removeAppender(appenderMock);
	    }
	 
	@Test
	public void shouldReturnTrueWhenRequiredQuantityOfDrinksIsLessThenAvailableStock() {

		Integer typeOfDrink = 1;
		Integer quantityOrdered = 5;
		Set<String> drinkContents=new HashSet <>(Arrays.asList("Water","Tea","Sugar","Milk"));
		Map<String, Integer> requiredQuantityOfMaterials = new HashMap<>();
		requiredQuantityOfMaterials.put("Water", 335);
		requiredQuantityOfMaterials.put("Tea", 9);
		requiredQuantityOfMaterials.put("Sugar", 85);
		requiredQuantityOfMaterials.put("Milk", 220);

		Map<String, Integer> availableMaterialInStock = new HashMap<>();
		availableMaterialInStock.put("Water", 10000);
		availableMaterialInStock.put("Tea", 2000);
		availableMaterialInStock.put("Coffee", 2000);
		availableMaterialInStock.put("Sugar", 8000);
		availableMaterialInStock.put("Milk", 15000);

		List<BillOfOrder> billingList = new ArrayList<>();

		when(vendingMachineHelper.calculateRequiredMaterialsForDrinks(typeOfDrink, quantityOrdered))
				.thenReturn(requiredQuantityOfMaterials);
		when(vendingMachineHelper.getAvailableMaterialQuantityFromStock()).thenReturn(availableMaterialInStock);

		doNothing().when(vendingMachineHelper).calculateTotalBillOfOrder(typeOfDrink, quantityOrdered, billingList, drinkContents);
		doNothing().when(vendingMachineHelper).updateMaterialStockInContainers(availableMaterialInStock);

		Boolean actual = teaCoffeeVendingMachineImpl.prepareDrink(typeOfDrink, quantityOrdered, billingList);
		
		verify(vendingMachineHelper).calculateTotalBillOfOrder(typeOfDrink, quantityOrdered, billingList,drinkContents);
		verify(vendingMachineHelper).updateMaterialStockInContainers(availableMaterialInStock);
		assertTrue(actual);
	}

	@Test
	public void shouldReturnFalseWhenRequiredQuantityForDrinksIsLessThenAvailableStock() {

		Integer typeOfDrink = 1;
		Integer quantityOrdered = 4;
		
		Map<String, Integer> requiredQuantityOfMaterials = new HashMap<>();
		requiredQuantityOfMaterials.put("Water", 335);
		requiredQuantityOfMaterials.put("Tea", 9);
		requiredQuantityOfMaterials.put("Coffee", 13);
		requiredQuantityOfMaterials.put("Sugar", 85);
		requiredQuantityOfMaterials.put("Milk", 220);

		Map<String, Integer> availableMaterialInStock = new HashMap<>();
		availableMaterialInStock.put("Water", 100);
		availableMaterialInStock.put("Tea", 2000);
		availableMaterialInStock.put("Coffee", 2);
		availableMaterialInStock.put("Sugar", 8000);
		availableMaterialInStock.put("Milk", 15000);

		List<BillOfOrder> billingList = new ArrayList<>();

		when(vendingMachineHelper.calculateRequiredMaterialsForDrinks(typeOfDrink, quantityOrdered)).thenReturn(requiredQuantityOfMaterials);
		when(vendingMachineHelper.getAvailableMaterialQuantityFromStock()).thenReturn(availableMaterialInStock);

		Boolean actual = teaCoffeeVendingMachineImpl.prepareDrink(typeOfDrink, quantityOrdered, billingList);
		
		verify(appenderMock,Mockito.times(3)).doAppend((LoggingEvent) anyObject());
		assertFalse(actual);
	}

	@Test
	public void shouldReturnUpdatedRefillCountWhenRefillAmountIsPassedInInput() throws Exception {

		Integer containerRefillCount = 0;
		Integer expected=1;
		Map<String, Integer> containerStockToUpdate = new HashMap<>();
		containerStockToUpdate.put("Water", 100);
		containerStockToUpdate.put("Tea", 200);
		containerStockToUpdate.put("Coffee", 100);
		containerStockToUpdate.put("Sugar", 300);
		containerStockToUpdate.put("Milk", 100);

		Map<String, Integer> availableMaterialInStock = new HashMap<>();
		availableMaterialInStock.put("Water", 15000);
		availableMaterialInStock.put("Tea", 2000);
		availableMaterialInStock.put("Coffee", 2000);
		availableMaterialInStock.put("Sugar", 8000);
		availableMaterialInStock.put("Milk", 10000);

		Map<String, Integer> containerUpdatedStock = new HashMap<>();
		

		when(vendingMachineHelper.getAvailableMaterialQuantityFromStock()).thenReturn(availableMaterialInStock);
		doNothing().when(manageContainerStockHelper).writeUpdatedContainerStockToJSON(containerUpdatedStock);
		Integer actual=teaCoffeeVendingMachineImpl.refillContainer(containerStockToUpdate, containerRefillCount);

		verify(vendingMachineHelper).getAvailableMaterialQuantityFromStock();
		verify(appenderMock,Mockito.times(5)).doAppend((LoggingEvent) anyObject());
		assertEquals(expected, actual);
	}

	@Test
	public void shouldReturnOldContainerRefillCountWhenWriteDataToJsonThrowsException() throws Exception {

		Integer expected = 0;
		Map<String, Integer> containerStockToUpdate = new HashMap<>();
		containerStockToUpdate.put("Water", 100);
		containerStockToUpdate.put("Tea", 200);
		containerStockToUpdate.put("Coffee", 100);
		containerStockToUpdate.put("Sugar", 300);
		containerStockToUpdate.put("Milk", 100);

		Map<String, Integer> availableMaterialInStock = new HashMap<>();
		availableMaterialInStock.put("Water", 1000);
		availableMaterialInStock.put("Tea", 200);
		availableMaterialInStock.put("Coffee", 1000);
		availableMaterialInStock.put("Sugar", 3000);
		availableMaterialInStock.put("Milk", 1000);

		when(vendingMachineHelper.getAvailableMaterialQuantityFromStock()).thenReturn(availableMaterialInStock);

		doThrow(Exception.class).when(manageContainerStockHelper).writeUpdatedContainerStockToJSON(anyObject());

		Integer actual = teaCoffeeVendingMachineImpl.refillContainer(containerStockToUpdate, expected);
		verify(appenderMock,Mockito.times(5)).doAppend((LoggingEvent) anyObject());
		assertEquals(expected, actual);

	}

	@Test
	public void shouldCalculateTotalSalesDrinkWiseWhenReportTypeOneIsProvideInInput() {

		Integer reportType = 1;
		List<BillOfOrder> billingList = new ArrayList<>();
		Integer refillContainerCount = 0;

		BillOfOrder billOfOrder = new BillOfOrder();
		billOfOrder.setItemName("Tea");
		billOfOrder.setQuantityOrdered(2);
		billOfOrder.setTotal(10);
		billOfOrder.setUnitPrice(5);
		billingList.add(billOfOrder);

		teaCoffeeVendingMachineImpl.prepareReport(reportType, billingList, refillContainerCount);
		verify(appenderMock,Mockito.times(2)).doAppend((LoggingEvent) anyObject());

	}

	@Test
	public void shouldCalculateTotalSalesOfAllDrinksWhenReportTypeTwoIsProvideInInput() {

		Integer reportType = 2;
		List<BillOfOrder> billingList = new ArrayList<>();
		Integer refillContainerCount = 0;

		BillOfOrder billOfOrderTea = new BillOfOrder();
		billOfOrderTea.setItemName("Tea");
		billOfOrderTea.setQuantityOrdered(2);
		billOfOrderTea.setTotal(10);
		billOfOrderTea.setUnitPrice(5);

		BillOfOrder billOfOrderCoffee = new BillOfOrder();
		billOfOrderCoffee.setItemName("Coffee");
		billOfOrderCoffee.setQuantityOrdered(4);
		billOfOrderCoffee.setTotal(60);
		billOfOrderCoffee.setUnitPrice(15);

		billingList.add(billOfOrderTea);
		billingList.add(billOfOrderCoffee);

		teaCoffeeVendingMachineImpl.prepareReport(reportType, billingList, refillContainerCount);
		verify(appenderMock,Mockito.times(1)).doAppend((LoggingEvent) anyObject());

	}

	@Test
	public void shouldCalculateContainerRefillCountWhenReportTypeThreeIsProvideInInput() {

		Integer reportType = 3;
		List<BillOfOrder> billingList = new ArrayList<>();
		Integer refillContainerCount = 3;

		teaCoffeeVendingMachineImpl.prepareReport(reportType, billingList, refillContainerCount);
		verify(appenderMock,Mockito.times(1)).doAppend((LoggingEvent) anyObject());
	}

	@Test
	public void shouldCalculateTotalIngredientsWastedWherReportTypeFourIsProvidedInInput() {
		
		List<BillOfOrder> billingList = new ArrayList<>();
		BillOfOrder billOfOrderTea = new BillOfOrder();
		billOfOrderTea.setItemName("Tea");
		billOfOrderTea.setQuantityOrdered(2);
		billOfOrderTea.setTotal(10);
		billOfOrderTea.setUnitPrice(5);

		BillOfOrder billOfOrderCoffee = new BillOfOrder();
		billOfOrderCoffee.setItemName("Coffee");
		billOfOrderCoffee.setQuantityOrdered(4);
		billOfOrderCoffee.setTotal(60);
		billOfOrderCoffee.setUnitPrice(15);

		billingList.add(billOfOrderTea);
		billingList.add(billOfOrderCoffee);
		Integer reportType = 4;
		
		Integer refillContainerCount = 0;

		teaCoffeeVendingMachineImpl.prepareReport(reportType, billingList, refillContainerCount);
		verify(appenderMock,Mockito.times(1)).doAppend((LoggingEvent) anyObject());

	}
	@Test
	public void shouldGiveInvalidreportErrorWhenTypeInvalidTypeIsProvidedInInput() {

		Integer reportType = 5;
		List<BillOfOrder> billingList = new ArrayList<>();
		Integer refillContainerCount = 0;

		teaCoffeeVendingMachineImpl.prepareReport(reportType, billingList, refillContainerCount);
		verify(appenderMock,Mockito.times(1)).doAppend((LoggingEvent) anyObject());

	}

	@Test
	public void shouldCalculateContainerStock() {

		Map<String, Integer> containerStock = new HashMap<>();
		containerStock.put("Tea", 1000);
		containerStock.put("Coffee", 1500);
		containerStock.put("Milk", 1200);
		containerStock.put("Water", 5000);
		containerStock.put("Sugar", 1000);

		when(manageContainerStockHelper.readRemainingMaterialStockFromJSON()).thenReturn(containerStock);

		teaCoffeeVendingMachineImpl.getContainerStatus();
		verify(manageContainerStockHelper).readRemainingMaterialStockFromJSON();
		verify(appenderMock,Mockito.times(6)).doAppend((LoggingEvent) anyObject());

	}

	@Test
	public void shouldReturnTrueWhenContainerResetIsSuccessful() throws Exception {

		Map<String, Integer> containerUpdatedStock = new HashMap<>();

		doNothing().when(manageContainerStockHelper).writeUpdatedContainerStockToJSON(containerUpdatedStock);
		Boolean actual = teaCoffeeVendingMachineImpl.reset();
	
		verify(appenderMock,Mockito.times(0)).doAppend((LoggingEvent) anyObject());
		assertTrue(actual);
	}

	@Test
	public void shouldReturnFalseWhenAnyExceptionOccurInReset() throws Exception {

		doThrow(Exception.class).when(manageContainerStockHelper).writeUpdatedContainerStockToJSON(anyObject());

		Boolean actual = teaCoffeeVendingMachineImpl.reset();
		assertFalse(actual);
	}

}
