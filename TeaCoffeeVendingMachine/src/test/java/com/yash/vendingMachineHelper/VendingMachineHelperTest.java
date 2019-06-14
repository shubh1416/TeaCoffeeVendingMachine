package com.yash.vendingMachineHelper;

import static org.junit.Assert.assertEquals;

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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.yash.VendingMachineImpl.PerformOperation;
import com.yash.beans.BillOfOrder;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VendingMachineHelperTest {

	@InjectMocks
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
	public void shouldCalculateRequiredMaterialWhenTeaIsProvided() {

		Integer typeOfDrink = 1;
		Integer quantityOfDrink = 1;

		Map<String, Integer> expected = new HashMap<>();
		expected.put("Tea", 6);
		expected.put("Milk", 44);
		expected.put("Sugar", 17);
		expected.put("Water", 65);

		Map<String, Integer> actual = vendingMachineHelper.calculateRequiredMaterialsForDrinks(typeOfDrink,
				quantityOfDrink);
		assertEquals(expected, actual);
	}

	@Test
	public void shouldCalculateRequiredMaterialWhenBlackTeaIsProvided() {

		Integer typeOfDrink = 2;
		Integer quantityOfDrink = 1;

		Map<String, Integer> expected = new HashMap<>();
		expected.put("Tea", 3);
		expected.put("Sugar", 17);
		expected.put("Water", 112);

		Map<String, Integer> actual = vendingMachineHelper.calculateRequiredMaterialsForDrinks(typeOfDrink,
				quantityOfDrink);
		assertEquals(expected, actual);
	}

	@Test
	public void shouldCalculateRequiredMaterialWhenCoffeeIsProvided() {

		Integer typeOfDrink = 3;
		Integer quantityOfDrink = 1;

		Map<String, Integer> expected = new HashMap<>();
		expected.put("Coffee", 5);
		expected.put("Milk", 88);
		expected.put("Sugar", 17);
		expected.put("Water", 23);

		Map<String, Integer> actual = vendingMachineHelper.calculateRequiredMaterialsForDrinks(typeOfDrink,
				quantityOfDrink);
		assertEquals(expected, actual);
	}

	@Test
	public void shouldCalculateRequiredMaterialWhenBlackCoffeeIsProvided() {

		Integer typeOfDrink = 4;
		Integer quantityOfDrink = 1;

		Map<String, Integer> expected = new HashMap<>();
		expected.put("Coffee", 3);
		expected.put("Sugar", 17);
		expected.put("Water", 112);

		Map<String, Integer> actual = vendingMachineHelper.calculateRequiredMaterialsForDrinks(typeOfDrink,
				quantityOfDrink);
		assertEquals(expected, actual);
	}

	@Test
	public void shouldGiveInvalidChoiceWhenInvalidDrinkTypeIsProvidedInInput() {

		Integer typeOfDrink = 10;
		Integer quantityOfDrink = 1;

		vendingMachineHelper.calculateRequiredMaterialsForDrinks(typeOfDrink, quantityOfDrink);
		verify(appenderMock,Mockito.times(1)).doAppend((LoggingEvent) anyObject());
	}

	@Test
	public void shouldReturnAvailableMaterialsQuantityInStock() {

		Map<String, Integer> expectedStock = new HashMap<>();
		expectedStock.put("Water", 10000);
		expectedStock.put("Tea", 2000);
		expectedStock.put("Coffee", 2000);
		expectedStock.put("Sugar", 8000);
		expectedStock.put("Milk", 1000);

		when(manageContainerStockHelper.readRemainingMaterialStockFromJSON()).thenReturn(expectedStock);

		Map<String, Integer> actual = vendingMachineHelper.getAvailableMaterialQuantityFromStock();
		assertEquals(expectedStock, actual);
	}

	@Test
	public void shouldCalculateTotalBillOfATeaProvidedWithQuantityInInput() {

		Integer typeOfDrink = 1;
		Integer quantityOfDrink = 5;

		List<BillOfOrder> billingList = new ArrayList<>();
		Set<String> drinkContents=new HashSet<>();
		vendingMachineHelper.calculateTotalBillOfOrder(typeOfDrink, quantityOfDrink, billingList, drinkContents);
	
		verify(appenderMock,Mockito.times(2)).doAppend((LoggingEvent) anyObject());
	}

	@Test
	public void shouldCalculateTotalBillOfABlackTeaProvidedWithQuantityInInput() {

		Integer typeOfDrink = 2;
		Integer quantityOfDrink = 5;

		List<BillOfOrder> billingList = new ArrayList<>();

		Set<String> drinkContents=new HashSet<>();
		vendingMachineHelper.calculateTotalBillOfOrder(typeOfDrink, quantityOfDrink, billingList, drinkContents);
	
		verify(appenderMock,Mockito.times(2)).doAppend((LoggingEvent) anyObject());
	}

	@Test
	public void shouldCalculateTotalBillOfACoffeeProvidedWithQuantityInInput() {

		Integer typeOfDrink = 3;
		Integer quantityOfDrink = 5;
		List<BillOfOrder> billingList = new ArrayList<>();

		Set<String> drinkContents=new HashSet<>();
		vendingMachineHelper.calculateTotalBillOfOrder(typeOfDrink, quantityOfDrink, billingList, drinkContents);
		
		verify(appenderMock,Mockito.times(2)).doAppend((LoggingEvent) anyObject());
	}

	@Test
	public void shouldCalculateTotalBillOfABlackCoffeeProvidedWithQuantityInInput() {

		Integer typeOfDrink = 4;
		Integer quantityOfDrink = 5;
		List<BillOfOrder> billingList = new ArrayList<>();

		Set<String> drinkContents=new HashSet<>();
		vendingMachineHelper.calculateTotalBillOfOrder(typeOfDrink, quantityOfDrink, billingList, drinkContents);
	
		verify(appenderMock,Mockito.times(2)).doAppend((LoggingEvent) anyObject());
	}

	@Test
	public void shouldCalculateTotalWasteWhenTeaIsOrdered() {

		String typeOfDrink = "Tea";
		Integer quantityOfDrink = 6;
		Map<String, Integer> totalMaterialWasted=new HashMap<>();
		totalMaterialWasted.put("Water", 5);
		totalMaterialWasted.put("Tea", 1);
		totalMaterialWasted.put("Sugar", 2);
		totalMaterialWasted.put("Milk", 4);
		
		vendingMachineHelper.calculateTotalMaterialWasted(typeOfDrink, quantityOfDrink,totalMaterialWasted);
	}

	@Test
	public void shouldCalculateTotalWasteWhenBlackTeaIsOrdered() {

		String typeOfDrink = "Black Tea";
		Integer quantityOfDrink = 6;
		Map<String, Integer> totalMaterialWasted=new HashMap<>();
		vendingMachineHelper.calculateTotalMaterialWasted(typeOfDrink, quantityOfDrink,totalMaterialWasted);

	}

	@Test
	public void shouldCalculateTotalWasteWhenCoffeeIsOrdered() {

		String typeOfDrink = "Coffee";
		Integer quantityOfDrink = 6;
		Map<String, Integer> totalMaterialWasted=new HashMap<>();
		
		vendingMachineHelper.calculateTotalMaterialWasted(typeOfDrink, quantityOfDrink,totalMaterialWasted);

	}

	@Test
	public void shouldCalculateTotalWasteWhenBlackCoffeeIsOrdered() {

		String typeOfDrink = "Black Coffee";
		Integer quantityOfDrink = 6;
		Map<String, Integer> totalMaterialWasted=new HashMap<>();
		vendingMachineHelper.calculateTotalMaterialWasted(typeOfDrink, quantityOfDrink,totalMaterialWasted);

	}

	@Test
	public void shouldUpdateMaterialStockInContainerWhenRemainingQuanityOfStockIsPassedInInput() throws Exception {

		Map<String, Integer> quantityOfMaterials = new HashMap<>();
		quantityOfMaterials.put("Water", 10000);
		quantityOfMaterials.put("Tea", 2000);
		quantityOfMaterials.put("Coffee", 2000);
		quantityOfMaterials.put("Sugar", 8000);
		quantityOfMaterials.put("Milk", 1000);
		doNothing().when(manageContainerStockHelper).writeUpdatedContainerStockToJSON(quantityOfMaterials);

		vendingMachineHelper.updateMaterialStockInContainers(quantityOfMaterials);
		verify(manageContainerStockHelper).writeUpdatedContainerStockToJSON(quantityOfMaterials);
	}

	@Test
	public void shouldHandleExceptionWhenWriteUpdatedStockFails() throws Exception {
		Map<String, Integer> quantityOfMaterials = new HashMap<>();
		quantityOfMaterials.put("Water", 10000);
		quantityOfMaterials.put("Tea", 2000);
		quantityOfMaterials.put("Coffee", 2000);
		quantityOfMaterials.put("Sugar", 8000);
		quantityOfMaterials.put("Milk", 1000);
		doThrow(Exception.class).when(manageContainerStockHelper).writeUpdatedContainerStockToJSON(quantityOfMaterials);

		vendingMachineHelper.updateMaterialStockInContainers(quantityOfMaterials);
		verify(appenderMock,Mockito.times(1)).doAppend((LoggingEvent) anyObject());
		verify(manageContainerStockHelper).writeUpdatedContainerStockToJSON(quantityOfMaterials);
	}

}
