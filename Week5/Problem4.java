class RaceEntry {

    protected String bibNumber;
    protected double entryFee;
    protected double amountPaid;

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
    }

    public void pay(double amount) {

        if (amount > 0) {
            amountPaid += amount;
        }
    }

    public double getBalanceDue() {
        return entryFee - amountPaid;
    }

    public void announce() {
        System.out.println(
            "Race Entry | Bib: " + bibNumber +
            " | Balance: " + getBalanceDue()
        );
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

    @Override
    public void announce() {

        System.out.println(
            "Runner Entry | Bib: " + bibNumber +
            " | Category: " + category +
            " | Balance: " + getBalanceDue()
        );
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

    @Override
    public void announce() {

        System.out.println(
            "Relay Team | Bib: " + bibNumber +
            " | Team Size: " + teamSize +
            " | Balance: " + getBalanceDue()
        );
    }
}


public class Problem4 {

    static String announceAll(RaceEntry[] entries) {

        StringBuilder report =
            new StringBuilder();

        for (RaceEntry entry : entries) {

            // Polymorphism
            if (entry instanceof RunnerEntry) {
                RunnerEntry runner =
                    (RunnerEntry) entry;

                report.append(
                    "Runner Entry | Bib: "
                )
                .append(runner.bibNumber)
                .append(" | Category: ")
                .append(runner.category)
                .append(" | Balance: ")
                .append(runner.getBalanceDue());
            }
            else {
                entry.announce();

                report.append(
                    "Relay Team | Bib: "
                )
                .append(entry.bibNumber)
                .append(" | Balance: ")
                .append(entry.getBalanceDue());
            }

            // Safe downcast for relay
            if (entry instanceof RelayTeamEntry) {

                RelayTeamEntry relay =
                    (RelayTeamEntry) entry;

                report.append(
                    " [Team size via downcast: "
                )
                .append(relay.getTeamSize())
                .append("]");
            }

            report.append(" | ");
        }

        return report.toString();
    }


    public static void main(String[] args) {

        RunnerEntry runner =
            new RunnerEntry(
                "BIB2001",
                80,
                "Open 10K"
            );

        runner.pay(30);

        RelayTeamEntry relay =
            new RelayTeamEntry(
                "BIB4001",
                300,
                4
            );

        RaceEntry[] fleet = {
            runner,
            relay
        };

        System.out.println(
            announceAll(fleet)
        );
    }
}
