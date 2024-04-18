import java.util.HashMap;
import java.util.Map;

public class ATM {
    private Map<Denomination, Integer> denominations; // Map to store available denominations

    public ATM(Map<Denomination, Integer> denominations) {
        this.denominations = denominations;
    }

    // Method to withdraw cash
    public synchronized Withdrawal withdraw(long accountId, int amount) {
        Withdrawal withdrawal = new Withdrawal();

        if (amount <= 0) {
            withdrawal.setStatus(Withdrawal.Status.INVALID_AMOUNT);
            return withdrawal;
        }

        if (!isAmountAvailable(amount)) {
            withdrawal.setStatus(Withdrawal.Status.INSUFFICIENT_FUNDS);
            return withdrawal;
        }

        Map<Denomination, Integer> withdrawalDenominations = getWithdrawalDenominations(amount);
        if (withdrawalDenominations == null) {
            withdrawal.setStatus(Withdrawal.Status.UNABLE_TO_DISPENSE_AMOUNT);
            return withdrawal;
        }

        updateDenominations(withdrawalDenominations);
        withdrawal.setStatus(Withdrawal.Status.SUCCESS);
        withdrawal.setDenominations(withdrawalDenominations);
        return withdrawal;
    }

    private boolean isAmountAvailable(int amount) {
        int totalAvailable = denominations.entrySet().stream()
                .mapToInt(entry -> entry.getKey().getValue() * entry.getValue()).sum();
        return totalAvailable >= amount;
    }

    private Map<Denomination, Integer> getWithdrawalDenominations(int amount) {
        Map<Denomination, Integer> withdrawalDenominations = new HashMap<>();
        for (Denomination denomination : denominations.keySet()) {
            int count = Math.min(amount / denomination.getValue(), denominations.get(denomination));
            if (count > 0) {
                withdrawalDenominations.put(denomination, count);
                amount -= denomination.getValue() * count;
            }
            if (amount == 0) {
                break;
            }
        }
        return (amount == 0) ? withdrawalDenominations : null;
    }

    private void updateDenominations(Map<Denomination, Integer> withdrawalDenominations) {
        for (Map.Entry<Denomination, Integer> entry : withdrawalDenominations.entrySet()) {
            Denomination denomination = entry.getKey();
            int count = entry.getValue();
            denominations.put(denomination, denominations.get(denomination) - count);
        }
    }

    private static void printDenomination( Withdrawal withdrawal){
        for (Map.Entry<Denomination, Integer> entry : withdrawal.getDenominations().entrySet()) {
            System.out.println("Denomination: " + entry.getKey().getValue() + ", Count: " + entry.getValue());
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        // Initialize ATM with denominations
        Map<Denomination, Integer> denominations = new HashMap<>();
        denominations.put(new Denomination(10), 100);
        denominations.put(new Denomination(20), 100);
        denominations.put(new Denomination(50), 100);
        denominations.put(new Denomination(100), 100);

        ATM atm = new ATM(denominations);

        // Test withdrawal
        Withdrawal withdrawal = atm.withdraw(123456, 150);
        System.out.println("Withdrawal Status: " + withdrawal.getStatus());
        System.out.println("Denominations Dispensed:");
        printDenomination(withdrawal);


        Withdrawal withdrawal1 = atm.withdraw(1256, 1500);
        System.out.println("Withdrawal Status: " + withdrawal1.getStatus());
        System.out.println("Denominations Dispensed:");
        printDenomination(withdrawal1);

        Withdrawal withdrawal3 = atm.withdraw(12546, 15000);
        System.out.println("Withdrawal Status: " + withdrawal3.getStatus());
        System.out.println("Denominations Dispensed:");
        printDenomination(withdrawal3);
    }

}