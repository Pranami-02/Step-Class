class RaceEntry {

    protected String bibNumber;
    protected double entryFee;
    protected double amountPaid;

    private final int entryCode;

    private static int bibCounter = 0;

    public RaceEntry(String bibNumber, double entryFee) {

        if (bibNumber == null || bibNumber.trim().length() < 4) {
            throw new IllegalArgumentException(
                "Invalid bib number"
            );
        }

        if (entryFee <= 0) {
            throw new IllegalArgumentException(
                "Entry fee must be positive"
            );
        }

        this.bibNumber = bibNumber;
        this.entryFee = entryFee;
        this.amountPaid = 0;

        // Increment only after validation succeeds
        bibCounter++;

        // final value cannot be changed later
        this.entryCode = bibCounter;
    }


    public void pay(double amount) {

        if (amount > 0) {
            amountPaid += amount;
        }
    }


    public void pay(double amount, String mode) {

        // Reuse the first pay() method
        pay(amount);

        System.out.println(
            "Paying via " + mode
        );
    }


    public double getBalanceDue() {

        return entryFee - amountPaid;
    }


    public static boolean isValidDiscountCode(
        String code
    ) {

        if (code == null || code.length() != 5) {
            return false;
        }

        // First character must be M
        if (code.charAt(0) != 'M') {
            return false;
        }

        // Characters 1, 2, 3 must be digits
        if (!Character.isDigit(code.charAt(1))) {
            return false;
        }

        if (!Character.isDigit(code.charAt(2))) {
            return false;
        }

        if (!Character.isDigit(code.charAt(3))) {
            return false;
        }

        // Last character must be uppercase
        if (!Character.isUpperCase(code.charAt(4))) {
            return false;
        }

        return true;
    }


    public static int getBibCounter() {

        return bibCounter;
    }


    public static String settleNight(
        RaceEntry[] entries
    ) {

        int processed = 0;
        int nullSkipped = 0;
        int relay = 0;
        int individual = 0;

        for (RaceEntry entry : entries) {

            // Handle null safely
            if (entry == null) {
                nullSkipped++;
                continue;
            }

            processed++;

            if (entry instanceof RelayTeamEntry) {
                relay++;
            }
            else {
                individual++;
            }
        }

        return processed +
            " processed | " +
            nullSkipped +
            " null skipped | " +
            relay +
            " relay | " +
            individual +
            " individual";
    }
}


class RunnerEntry extends RaceEntry {

    protected String category;

    public RunnerEntry(
        String bibNumber,
        double entryFee,
        String category
    ) {
        super(bibNumber, entryFee);
        this.category = category;
    }
}


class RelayTeamEntry extends RaceEntry {

    private int teamSize;

    public RelayTeamEntry(
        String bibNumber,
        double entryFee,
        int teamSize
    ) {

        super(bibNumber, entryFee);

        if (teamSize <= 0) {
            throw new IllegalArgumentException(
                "Team size must be positive"
            );
        }

        this.teamSize = teamSize;
    }

    public int getTeamSize() {
        return teamSize;
    }
}


class EliteRunnerEntry extends RunnerEntry {

    private double sponsorBonus;

    public EliteRunnerEntry(
        String bibNumber,
        double entryFee,
        String category,
        double sponsorBonus
    ) {

        super(bibNumber, entryFee, category);
        this.sponsorBonus = sponsorBonus;
    }
}


public class Problem5 {

    public static void main(String[] args) {

        // Test discount code
        System.out.println(
            RaceEntry.isValidDiscountCode("M123A")
        );

        System.out.println(
            RaceEntry.isValidDiscountCode("M12A")
        );

        System.out.println(
            RaceEntry.isValidDiscountCode("X123A")
        );


        System.out.println();


        // Create entries
        EliteRunnerEntry elite =
            new EliteRunnerEntry(
                "BIB3001",
                150,
                "Elite Full Marathon",
                500
            );

        RelayTeamEntry relay =
            new RelayTeamEntry(
                "BIB4001",
                300,
                4
            );


        // Overloaded pay()
        elite.pay(10, "UPI");


        System.out.println();


        // Night settlement
        RaceEntry[] entries = {
            elite,
            null,
            relay
        };

        System.out.println(
            RaceEntry.settleNight(entries)
        );


        System.out.println();


        // Counter
        System.out.println(
            "Bib Counter: " +
            RaceEntry.getBibCounter()
        );
    }
}
