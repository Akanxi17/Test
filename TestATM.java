import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

    public class TestATM {

        @Test
        public void testWithdrawalSuccess() {
            Map<Denomination, Integer> denominations = new HashMap<>();
            denominations.put(new Denomination(10), 100);
            denominations.put(new Denomination(20), 100);
            denominations.put(new Denomination(50), 100);
            denominations.put(new Denomination(100), 100);

            ATM atm = new ATM(denominations);

            // Perform withdrawal
            Withdrawal withdrawal = atm.withdraw(123456, 150);

            // Assert withdrawal success
            assertEquals(Withdrawal.Status.SUCCESS, withdrawal.getStatus());
            // Assert denominations dispensed
            Map<Denomination, Integer> expectedDenominations = new HashMap<>();
            expectedDenominations.put(new Denomination(100), 1);
            expectedDenominations.put(new Denomination(50), 1);
            assertEquals(expectedDenominations, withdrawal.getDenominations());
        }

        @Test
        public void testInvalidWithdrawalAmount() {
            Map<Denomination, Integer> denominations = new HashMap<>();
            denominations.put(new Denomination(10), 100);
            denominations.put(new Denomination(20), 100);
            denominations.put(new Denomination(50), 100);
            denominations.put(new Denomination(100), 100);

            ATM atm = new ATM(denominations);

            // Perform withdrawal with invalid amount
            Withdrawal withdrawal = atm.withdraw(123456, -50);

            // Assert withdrawal failure due to invalid amount
            assertEquals(Withdrawal.Status.INVALID_AMOUNT, withdrawal.getStatus());
        }

        @Test
        public void testInsufficientFunds() {
            Map<Denomination, Integer> denominations = new HashMap<>();
            denominations.put(new Denomination(10), 1);
            denominations.put(new Denomination(20), 1);
            denominations.put(new Denomination(50), 1);
            denominations.put(new Denomination(100), 1);

            ATM atm = new ATM(denominations);

            // Perform withdrawal with amount exceeding available funds
            Withdrawal withdrawal = atm.withdraw(123456, 5000);

            // Assert withdrawal failure due to insufficient funds
            assertEquals(Withdrawal.Status.INSUFFICIENT_FUNDS, withdrawal.getStatus());
        }

        @Test
        public void testConcurrentWithdrawals() throws InterruptedException {
            // Initialize ATM with denominations
            Map<Denomination, Integer> denominations = new HashMap<>();
            denominations.put(new Denomination(10), 100);
            denominations.put(new Denomination(20), 100);
            denominations.put(new Denomination(50), 100);
            denominations.put(new Denomination(100), 100);

            ATM atm = new ATM(denominations);

            // Number of withdrawal transactions to simulate concurrently
            int numTransactions = 5;

            // Executor service to manage threads
            ExecutorService executor = Executors.newFixedThreadPool(numTransactions);

            // Runnable task for withdrawal transaction
            Runnable withdrawalTask = () -> {
                // Simulate withdrawal operation
                Withdrawal withdrawal = atm.withdraw(Thread.currentThread().getId(), 150);
                // Assert the status of withdrawal (can assert other aspects as well)
                assertEquals(Withdrawal.Status.SUCCESS, withdrawal.getStatus());
            };

            // Submit withdrawal tasks to executor
            for (int i = 0; i < numTransactions; i++) {
                executor.submit(withdrawalTask);
            }

            // Shutdown executor and wait for all tasks to complete
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);
        }

        // Add more test cases for other scenarios...
    }



