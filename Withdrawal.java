import java.util.Map;

class Withdrawal {
    public enum Status {
        SUCCESS,
        INVALID_AMOUNT,
        INSUFFICIENT_FUNDS,
        UNABLE_TO_DISPENSE_AMOUNT
    }

    private Status status;
    private Map<Denomination, Integer> denominations;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Map<Denomination, Integer> getDenominations() {
        return denominations;
    }

    public void setDenominations(Map<Denomination, Integer> denominations) {
        this.denominations = denominations;
    }
}