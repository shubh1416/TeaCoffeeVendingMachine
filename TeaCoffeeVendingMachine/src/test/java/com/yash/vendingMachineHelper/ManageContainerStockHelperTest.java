package com.yash.vendingMachineHelper;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yash.VendingMachineImpl.PerformOperation;

@RunWith(MockitoJUnitRunner.class)
public class ManageContainerStockHelperTest {

	@InjectMocks
	ManageContainerStockHelper manageContainerStockHelper;

	@Mock
	ObjectMapper mapper;

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
	public void shouldReturnMapOfContainerStock() throws IOException {

		Map<String, Integer> expected = new HashMap<>();
		expected.put("Water", 15000);
		expected.put("Tea", 2000);
		expected.put("Coffee", 2000);
		expected.put("Sugar", 8000);
		expected.put("Milk", 10000);

		when(mapper.readValue(any(File.class), any(TypeReference.class))).thenReturn(expected);

		Map<String, Integer> actual = manageContainerStockHelper.readRemainingMaterialStockFromJSON();
		assertEquals(expected, actual);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldThrowExceptionWhenReadFromFileIsFailed() throws IOException {

		Map<String, Integer> expected = new HashMap<>();
		expected.put("Water", 15000);
		expected.put("Tea", 2000);
		expected.put("Coffee", 2000);
		expected.put("Sugar", 8000);
		expected.put("Milk", 10000);

		when(mapper.readValue(any(File.class), any(TypeReference.class))).thenThrow(IOException.class);

		manageContainerStockHelper.readRemainingMaterialStockFromJSON();
		verify(appenderMock,Mockito.times(1)).doAppend((LoggingEvent) anyObject());
		verify(mapper).readValue(any(File.class), any(TypeReference.class));
	}

	@Test
	public void shouldWriteContainerStockToJson() throws Exception {
		Map<String, Integer> containerUpdatedStock = new HashMap<>();
		containerUpdatedStock.put("Water", 15000);
		containerUpdatedStock.put("Tea", 2000);
		containerUpdatedStock.put("Coffee", 2000);
		containerUpdatedStock.put("Sugar", 8000);
		containerUpdatedStock.put("Milk", 10000);

		doNothing().when(mapper).writeValue(any(File.class), any(Map.class));

		manageContainerStockHelper.writeUpdatedContainerStockToJSON(containerUpdatedStock);
		verify(mapper).writeValue(any(File.class), any(Map.class));
	}
}
