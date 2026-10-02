import java.util.Arrays;

// Common type for all receipts
interface Receipt {
    String[] getBookIds();
}


// LoanReceipt must be final
final class LoanReceipt implements Receipt {

    private final String memberId;
    private final String[] bookIds;

    // Constructor
    public LoanReceipt(String memberId, String[] bookIds) {

        if (bookIds == null) {
            throw new IllegalArgumentException(
                "Book IDs cannot be null"
            );
        }

        // Defensive copy
        this.bookIds = Arrays.copyOf(bookIds, bookIds.length);

        // Validate every book ID
        for (String id : this.bookIds) {

            if (id == null || !id.matches("BK-\\d{3}")) {
                throw new IllegalArgumentException(
                    "Invalid book ID: " + id
                );
            }
        }

        this.memberId = memberId;
    }

    // Getter with defensive copy
    @Override
    public String[] getBookIds() {
        return Arrays.copyOf(bookIds, bookIds.length);
    }

    public String getMemberId() {
        return memberId;
    }

    // With-style method
    public LoanReceipt withCorrectedBookId(
            int index, String newId) {

        if (index < 0 || index >= bookIds.length) {
            throw new IndexOutOfBoundsException(
                "Invalid book ID index"
            );
        }

        if (newId == null || !newId.matches("BK-\\d{3}")) {
            throw new IllegalArgumentException(
                "Invalid book ID: " + newId
            );
        }

        // Make a new array
        String[] newBookIds =
            Arrays.copyOf(bookIds, bookIds.length);

        newBookIds[index] = newId;

        // Return a completely new object
        return new LoanReceipt(memberId, newBookIds);
    }
}


// Reference-only receipt
class ReferenceOnlyLoanReceipt implements Receipt {

    private final String memberId;
    private final String[] bookIds;
    private final String roomNumber;

    public ReferenceOnlyLoanReceipt(
            String memberId,
            String[] bookIds,
            String roomNumber) {

        if (bookIds == null) {
            throw new IllegalArgumentException(
                "Book IDs cannot be null"
            );
        }

        this.memberId = memberId;
        this.roomNumber = roomNumber;

        // Defensive copy
        this.bookIds =
            Arrays.copyOf(bookIds, bookIds.length);

        // Validate book IDs
        for (String id : this.bookIds) {

            if (id == null || !id.matches("BK-\\d{3}")) {
                throw new IllegalArgumentException(
                    "Invalid book ID: " + id
                );
            }
        }
    }

    @Override
    public String[] getBookIds() {
        return Arrays.copyOf(bookIds, bookIds.length);
    }

    public String getMemberId() {
        return memberId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
}


public class Problem5 {

    // Nightly circulation processor
    static String processNightlyCirculation(
            Receipt[] receipts) {

        int processed = 0;
        int nullSkipped = 0;
        int referenceOnly = 0;
        int regular = 0;

        for (Receipt receipt : receipts) {

            // Skip null entries
            if (receipt == null) {
                nullSkipped++;
                continue;
            }

            processed++;

            // instanceof check
            if (receipt instanceof ReferenceOnlyLoanReceipt) {
                referenceOnly++;
            } else if (receipt instanceof LoanReceipt) {
                regular++;
            }
        }

        return processed + " processed | "
             + nullSkipped + " null skipped | "
             + referenceOnly + " reference-only | "
             + regular + " regular";
    }


    public static void main(String[] args) {

        // -----------------------------------------
        // TEST 1: Invalid book ID
        // -----------------------------------------

        try {

            LoanReceipt r1 =
                new LoanReceipt(
                    "LIB-8841",
                    new String[]{"BK-100", "bad"}
                );

        } catch (IllegalArgumentException e) {

            System.out.println(
                "construction rejected"
            );
        }


        // -----------------------------------------
        // TEST 2: Defensive copying
        // -----------------------------------------

        LoanReceipt r2 =
            new LoanReceipt(
                "LIB-8841",
                new String[]{"BK-100", "BK-101"}
            );

        String[] ids = r2.getBookIds();

        // Try to modify returned array
        ids[0] = "HACKED";

        // Original object remains unchanged
        System.out.println(
            r2.getBookIds()[0]
        );


        // -----------------------------------------
        // TEST 3: Nightly circulation
        // -----------------------------------------

        Receipt[] receipts = {

            new ReferenceOnlyLoanReceipt(
                "LIB-001",
                new String[]{"BK-200"},
                "Reading Room 3"
            ),

            null,

            new LoanReceipt(
                "LIB-002",
                new String[]{"BK-201"}
            )
        };

        System.out.println(
            processNightlyCirculation(receipts)
        );


        // -----------------------------------------
        // TEST 4: Corrected receipt
        // -----------------------------------------

        LoanReceipt corrected =
            r2.withCorrectedBookId(
                0,
                "BK-999"
            );

        System.out.println(
            corrected.getBookIds()[0]
        );

        // Original receipt is unchanged
        System.out.println(
            r2.getBookIds()[0]
        );
    }
}
