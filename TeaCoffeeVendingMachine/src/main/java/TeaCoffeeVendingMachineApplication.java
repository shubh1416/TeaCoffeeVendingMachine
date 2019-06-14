
import org.apache.log4j.Logger;

import com.yash.VendingMachineImpl.PerformOperation;
import com.yash.VendingMachineImpl.TeaCoffeeVendingMachineImpl;
import com.yash.util.MyScanner;
import com.yash.vendingMachine.TeaCoffeeVendingMachine;

public class TeaCoffeeVendingMachineApplication {
	static Logger log = Logger.getLogger(TeaCoffeeVendingMachineApplication.class);
	static PerformOperation performOperation = new PerformOperation();
	public static void main(String[] args) {
		 MyScanner scanner = new MyScanner();
		
		TeaCoffeeVendingMachine teaCoffeeVendingMachine = new TeaCoffeeVendingMachineImpl();
		teaCoffeeVendingMachine.reset();
	
		
		log.info("::::::::::::::::::::  Welcome To Tea Coffee Vending Machine  ::::::::::::::::::::");
		Integer input = 1;
		while (input == 1) {
			log.info("-------------------------------------------------------------------------------------------------------------------");
			System.out.println();
			log.info("Kindly choose below actions to perform----->");
			System.out.println();
			log.info("1. make a drink(Tea/Coffee)    2. Refill Container    3. Check Total Sale");
			log.info("4. Check Container status    5. Reset Container    6. Exit TCVM");

			Integer selectedOption = scanner.nextInt();
			input = performOperation.performVendingMachinOperations(selectedOption);
		}
		log.info("Operation Completed");

	}
}
