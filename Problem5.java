class DeliveryAccount {

    protected String studentId;
    protected double orderValue;

    // One-time class-level state
    protected static double surgeRate;

    // Static block
    static {
        surgeRate = 1.0;   // 1% per delayed minute
    }

    // Full constructor
    public DeliveryAccount(String studentId, double orderValue) {
        this.studentId = studentId;
        this.orderValue = orderValue;
    }

    // Provisional constructor
    // Constructor chaining
    public DeliveryAccount(String studentId) {
        this(studentId, 0.0);
    }

    // Final method - cannot be overridden
    public final double calculateSurgeFee(int delayMinutes) {

        if (delayMinutes < 0) {
            throw new IllegalArgumentException(
                "Delay minutes cannot be negative"
            );
        }

        if (delayMinutes == 0) {
            return 0.0;
        }

        // Flat-rate calculation
        return orderValue
             * surgeRate
             / 100.0
             * delayMinutes;
    }

    // Process one account
    public void processAccount(
            DeliveryAccount account,
            double amount,
            int delayMinutes) {

        this.orderValue = amount;

        double fee = calculateSurgeFee(delayMinutes);

        System.out.println(
            "Student: " + studentId
        );

        System.out.println(
            "Order value: Rs " + amount
        );

        System.out.println(
            "Delay: " + delayMinutes + " minutes"
        );

        System.out.println(
            "Surge fee: Rs " + fee
        );
    }
}


// Premium account
class PremiumAccount extends DeliveryAccount {

    public PremiumAccount(
            String studentId,
            double orderValue) {

        super(studentId, orderValue);
    }
}


public class Problem5 {

    static void processBatch(
            DeliveryAccount[] accounts,
            double[] amounts,
            int[] delayMinutesArray) {

        int processed = 0;
        int nullSkipped = 0;
        int premium = 0;
        int regular = 0;

        double grandTotal = 0.0;

        /*
         * Use the smallest array length.
         * This prevents an ArrayIndexOutOfBoundsException
         * if the three arrays have different lengths.
         */
        int length = Math.min(
            accounts.length,
            Math.min(
                amounts.length,
                delayMinutesArray.length
            )
        );

        for (int i = 0; i < length; i++) {

            // Check for null account
            if (accounts[i] == null) {
                nullSkipped++;
                continue;
            }

            try {

                DeliveryAccount account =
                    accounts[i];

                // Set the actual order amount
                account.orderValue = amounts[i];

                // Calculate surge fee
                double fee =
                    account.calculateSurgeFee(
                        delayMinutesArray[i]
                    );

                grandTotal += fee;

                processed++;

                // instanceof decides account type
                if (account instanceof PremiumAccount) {
                    premium++;
                } else {
                    regular++;
                }

            } catch (Exception e) {

                System.out.println(
                    "Error processing account at index "
                    + i
                );
            }
        }

        System.out.println();
        System.out.println(
            processed + " processed | "
            + nullSkipped + " null skipped | "
            + premium + " premium | "
            + regular + " regular"
        );

        System.out.println(
            "grand total surge fees = Rs "
            + grandTotal
        );
    }


    public static void main(String[] args) {

        // Accounts
        DeliveryAccount[] accounts = {

            new PremiumAccount(
                "STU001",
                500
            ),

            null,

            new DeliveryAccount(
                "STU002",
                300
            )
        };

        // Amounts
        double[] amounts = {
            500,
            400,
            300
        };

        // Delay times
        int[] delayMinutesArray = {
            10,
            5,
            0
        };

        // Process the batch
        processBatch(
            accounts,
            amounts,
            delayMinutesArray
        );
    }
}