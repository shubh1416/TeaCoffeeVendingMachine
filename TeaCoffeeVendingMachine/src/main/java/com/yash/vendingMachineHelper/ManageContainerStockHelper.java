package com.yash.vendingMachineHelper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ManageContainerStockHelper {
	ObjectMapper mapper = new ObjectMapper();
	Map<String, Integer> containerMaterialStock = new HashMap<>();
	static Logger log = Logger.getLogger(ManageContainerStockHelper.class);
	
	public Map<String, Integer> readRemainingMaterialStockFromJSON() {

		try {
			containerMaterialStock = mapper.readValue(
					new File("containerStock.json"),
					new TypeReference<Map<String, Integer>>() {
					});
		} catch (IOException exception) {
			log.error(exception);
		}

		return containerMaterialStock;

	}

	public void writeUpdatedContainerStockToJSON(Map<String, Integer> containerUpdatedStock) throws Exception {

		mapper.writeValue(new File("containerStock.json"),
				containerUpdatedStock);
		

	}
}
