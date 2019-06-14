package com.yash.VendingMachineImpl;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import com.yash.beans.BillOfOrder;
import com.yash.util.MyScanner;
import com.yash.vendingMachine.TeaCoffeeVendingMachine;

@RunWith(MockitoJUnitRunner.class)
public class PerformOperationTest {

	@InjectMocks
	private PerformOperation performOperation;

	@Mock
	private TeaCoffeeVendingMachine teaCoffeeVendingMachine;

	@Mock
	private MyScanner scanner;
	

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
	public void shouldPerformMakeDrinkWhenFirstCaseIsProvidedAndTypeOfDrinkIsValid() {
		
		Integer typeOfDrink = 1;
		Integer quantityOrdered = 5;
		Integer expected = 1;
		List<BillOfOrder> billOfOrderList = new ArrayList<>();
		
		when(scanner.nextInt()).thenReturn(typeOfDrink);
		when(teaCoffeeVendingMachine.prepareDrink(typeOfDrink, quantityOrdered, billOfOrderList)).thenReturn(true);		
	
		Integer actual = performOperation.performVendingMachinOperations(expected);
		verify(appenderMock,Mockito.times(3)).doAppend((LoggingEvent) anyObject());
		assertEquals(expected, actual);
		

	}

	@Test
	public void shouldPrintErrorWhenFirstCaseIsProvidedAndTypeOfDrinkIsInValid() {
		
		Integer typeOfDrink = 6;
		Integer expected = 1;

		when(scanner.nextInt()).thenReturn(typeOfDrink);
		
		Integer actual = performOperation.performVendingMachinOperations(expected);
		verify(appenderMock,Mockito.times(3)).doAppend((LoggingEvent) anyObject());
		assertEquals(expected, actual);

	}

	@Test
	public void shouldRefillContainerAndIncreaseRefillCountWhenContainerRefillIsSuccessful() {

		Integer selectedOption = 2;
		Integer expected = 1;
		Integer containerRefillCount = 1;
		Map<String, Integer> containerStockToUpdate = new HashMap<>();
		
		when(teaCoffeeVendingMachine.refillContainer(containerStockToUpdate, containerRefillCount)).thenReturn(1);
		when(scanner.nextInt()).thenReturn(100);
		doNothing().when(teaCoffeeVendingMachine).getContainerStatus();
		Integer actual = performOperation.performVendingMachinOperations(selectedOption);

		verify(appenderMock,Mockito.times(6)).doAppend((LoggingEvent) anyObject());
		verify(teaCoffeeVendingMachine).getContainerStatus();
		assertEquals(expected, actual);

	}

	@Test
	public void shouldNotIncreaseRefillCountWhenContainerRefillFails() {

		Integer selectedOption = 2;
		Integer expected = 1;
		Integer containerRefillCount = 1;

		Map<String, Integer> containerStockToUpdate = new HashMap<>();

		when(teaCoffeeVendingMachine.refillContainer(containerStockToUpdate, containerRefillCount)).thenReturn(1);
		when(scanner.nextInt()).thenReturn(100);
		doNothing().when(teaCoffeeVendingMachine).getContainerStatus();

		Integer actual = performOperation.performVendingMachinOperations(selectedOption);
		
		verify(appenderMock,Mockito.times(6)).doAppend((LoggingEvent) anyObject());
		verify(teaCoffeeVendingMachine).getContainerStatus();
		assertEquals(expected, actual);

	}

	@Test
	public void shouldGenerateReportWhenThirdCaseIsGivenInAction() {

		Integer selectedOption = 3;
		Integer expected = 1;
		Integer reportOption = 1;
		List<BillOfOrder> billingList = new ArrayList<>();
		Integer countOfContainerRefill = 0;

		when(scanner.nextInt()).thenReturn(reportOption);
		doNothing().when(teaCoffeeVendingMachine).prepareReport(reportOption, billingList, countOfContainerRefill);

		Integer actual = performOperation.performVendingMachinOperations(selectedOption);

		verify(appenderMock,Mockito.times(5)).doAppend((LoggingEvent) anyObject());
		verify(teaCoffeeVendingMachine).prepareReport(reportOption, billingList, countOfContainerRefill);
		assertEquals(expected, actual);
	}

	@Test
	public void shouldReturnContainerStatusWhenFourthCaseIsProvidedInAction() {

		Integer selectedOption = 4;
		Integer expected = 1;

		doNothing().when(teaCoffeeVendingMachine).getContainerStatus();

		Integer actual = performOperation.performVendingMachinOperations(selectedOption);

		verify(teaCoffeeVendingMachine).getContainerStatus();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldResetContainerWhenCaseFiveIsProvidedAndResetOperationIsSuccessful() {

		Integer selectedOption = 5;
		Integer expected = 1;
		when(teaCoffeeVendingMachine.reset()).thenReturn(true);
		Integer actual = performOperation.performVendingMachinOperations(selectedOption);

		verify(appenderMock,Mockito.times(1)).doAppend((LoggingEvent) anyObject());
		verify(teaCoffeeVendingMachine).reset();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldNotResetContainerWhenCaseFiveIsProvidedAndResetOperationIsUnsuccessful() {

		Integer selectedOption = 5;
		Integer expected = 1;

		when(teaCoffeeVendingMachine.reset()).thenReturn(false);
		Integer actual = performOperation.performVendingMachinOperations(selectedOption);

		verify(appenderMock,Mockito.times(1)).doAppend((LoggingEvent) anyObject());
		verify(teaCoffeeVendingMachine).reset();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldResetContainerAndExitWhenCaseSixIsGivenInAction() {

		Integer selectedOption = 6;
		Integer expected = 2;
		when(teaCoffeeVendingMachine.reset()).thenReturn(true);
		Integer actual = performOperation.performVendingMachinOperations(selectedOption);

		verify(teaCoffeeVendingMachine).reset();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldGiveInvalidChoiceErrorWhenDefaultCaseIsPassedInAction() {

		Integer selectedOption = 8;
		Integer expected = 1;
		
		Integer actual = performOperation.performVendingMachinOperations(selectedOption);
		verify(appenderMock,Mockito.times(1)).doAppend((LoggingEvent) anyObject());
		assertEquals(expected, actual);
	}

}
