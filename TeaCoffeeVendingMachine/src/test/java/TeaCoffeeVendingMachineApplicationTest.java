import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
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
import com.yash.VendingMachineImpl.PerformOperation;

@RunWith(MockitoJUnitRunner.class)
public class TeaCoffeeVendingMachineApplicationTest {

		@InjectMocks
		TeaCoffeeVendingMachineApplication teaCoffeeVendingMachineApplication;
		
		@Mock
		PerformOperation performOperation;
		
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
	public void testTeaCoffeeVendingMachineApplicationMainMethod() {
		String input="6";
	    System.setIn(new ByteArrayInputStream(input.getBytes()));
	    
	    Mockito.when(performOperation.performVendingMachinOperations(Integer.parseInt(input))).thenReturn(2);  
	    TeaCoffeeVendingMachineApplication.main(new String [] {});
		verify(appenderMock,Mockito.times(6)).doAppend((LoggingEvent) anyObject());

	    
	}
}
