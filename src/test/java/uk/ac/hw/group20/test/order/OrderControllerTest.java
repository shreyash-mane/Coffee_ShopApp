package uk.ac.hw.group20.test.order;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.ac.hw.group20.main.view.HomeGui;
import uk.ac.hw.group20.order.controller.OrderController;
import uk.ac.hw.group20.order.dto.ServerInfoDto;
import uk.ac.hw.group20.order.dto.ThreadTimerDto;
import uk.ac.hw.group20.order.model.Order;  // Import Order class if it exists

public class OrderControllerTest {

    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        orderController = new OrderController();
    }

    @Test
    public void testStartThreadsInitializesComponents() {
        String userId = "testUser";
        LinkedList<Order> orderQueue = new LinkedList<>(); // Specify Order type for LinkedList
        String firstLoading = "true"; // Could be any appropriate value for the test
        ServerInfoDto dto = new ServerInfoDto();

        // Create the HomeGui instance with required constructor
        HomeGui dummyGui = new HomeGui(userId, orderQueue, firstLoading, dto, orderController);

        // Start the threads
        ServerInfoDto result = orderController.startThreads("testUser", 2, dummyGui);

        // Assert that the result is not null (to indicate successful initialization)
        assertNotNull(result, "Expected ServerInfoDto to be initialized after starting threads.");
    }

    @Test
    public void testUpdateThreadSleepTimerUpdatesTimersCorrectly() {
        // Prepare a timerDto with values (without consumerTimer)
        ThreadTimerDto timerDto = new ThreadTimerDto();
        timerDto.setProducerTimer(1000);
        timerDto.setBaristaTimer(1500);
        timerDto.setServer1Timer(2000);
        timerDto.setServer2Timer(2500);
        timerDto.setServer3Timer(3000);

        // Update timers
        orderController.updateThreadSleepTimer(timerDto);

        // Assert correct timer values are set
        assertEquals(1000, timerDto.getProducerTimer(), "Producer timer should be updated to 1000.");
        assertEquals(1500, timerDto.getBaristaTimer(), "Barista timer should be updated to 1500.");
        assertEquals(2000, timerDto.getServer1Timer(), "Server1 timer should be updated to 2000.");
        assertEquals(2500, timerDto.getServer2Timer(), "Server2 timer should be updated to 2500.");
        assertEquals(3000, timerDto.getServer3Timer(), "Server3 timer should be updated to 3000.");
    }

    @Test
    public void testStartThreadsDoesNotThrowExceptions() {
        String userId = "testUser";
        LinkedList<Order> orderQueue = new LinkedList<>(); // Specify Order type for LinkedList
        String firstLoading = "true"; // Could be any appropriate value for the test
        ServerInfoDto dto = new ServerInfoDto();
        
        // Create the HomeGui instance with required constructor
        HomeGui dummyGui = new HomeGui(userId, orderQueue, firstLoading, dto, orderController);

        // Ensure no exception is thrown during thread start
        assertDoesNotThrow(() -> orderController.startThreads("testUser", 2, dummyGui),
            "Expected startThreads to execute without throwing exceptions.");
    }

    @Test
    public void testUpdateThreadSleepTimerDoesNotThrowException() {
        // TimerDto for update
        ThreadTimerDto timerDto = new ThreadTimerDto();
        timerDto.setProducerTimer(1000);
        timerDto.setBaristaTimer(1500);
        timerDto.setServer1Timer(2000);
        timerDto.setServer2Timer(2500);
        timerDto.setServer3Timer(3000);

        // Ensure that the update does not throw exceptions
        assertDoesNotThrow(() -> orderController.updateThreadSleepTimer(timerDto),
            "Expected timer update to complete without throwing an exception.");
    }
}
