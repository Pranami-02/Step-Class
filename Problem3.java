class RaceEntry {

    protected String bibNumber;
    protected double entryFee;
    protected double amountPaid;

    private double[] lateFeeHistory;
    private int feeCount;

    public RaceEntry(String bibNumber, double entryFee) {

        if (bibNumber == null || bibNumber.trim().length() < 4) {
            throw new IllegalArgumentException("Invalid bib number");
        }

        if (entryFee <= 0) {
            throw new IllegalArgumentException("Entry fee must be positive");
        }

        this.bibNumber = bibNumber;
        this.entryFee = entryFee;
        this.amountPaid = 0;

        lateFeeHistory = new double[10];
        feeCount = 0;
    }

    public void pay(double amount) {

        if (amount > 0) {
            amountPaid += amount;
        }
    }

    public double getBalanceDue() {
        return entryFee - amountPaid;
    }

    protected void applyLateFee(double amount) {

        amountPaid -= amount;

        if (feeCount < lateFeeHistory.length) {
            lateFeeHistory[feeCount] = amount;
            feeCount++;
        }
    }

    public double[] getLateFeeHistory() {

        double[] result = new double[feeCount];

        for (int i = 0; i < feeCount; i++) {
            result[i] = lateFeeHistory[i];
        }

        return result;
    }
}


class RunnerEntry extends RaceEntry {

    private String category;

    public RunnerEntry(
        String bibNumber,
        double entryFee,
        String category
    ) {
        super(bibNumber, entryFee);
        this.category = category;
    }

    @Override
    protected void applyLateFee(double amount) {

        // Double the penalty and use parent's logic
        super.applyLateFee(amount * 2);
    }
}


public class Problem3 {

    public static void main(String[] args) {

        RunnerEntry r =
            new RunnerEntry(
                "BIB2001",
                80,
                "Open 10K"
            );

        r.pay(30);

        r.applyLateFee(20);

        System.out.println(
            "Balance Due: " +
            r.getBalanceDue()
        );

        double[] history =
            r.getLateFeeHistory();

        System.out.println(
            "Late Fee History: "
        );

        for (double fee : history) {
            System.out.println(fee);
        }

        // Testing defensive copy
        history[0] = 999;

        System.out.println(
            "After changing returned array:"
        );

        double[] actualHistory =
            r.getLateFeeHistory();

        for (double fee : actualHistory) {
            System.out.println(fee);
        }
    }
}