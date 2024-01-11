import com.smallworld.data.TransactionJsonRepository;
import com.smallworld.data.TransactionRepository;
import com.smallworld.domain.TransactionDataFetcher;
import com.smallworld.domain.entities.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.*;

public class TransactionDataFetcherTests {

    private final TransactionRepository transactionRepository = Mockito.mock(TransactionJsonRepository.class);
    private final TransactionDataFetcher transactionDataFetcher = new TransactionDataFetcher(transactionRepository);

    @Test
    public void testGetTotalTransactionAmount() {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();

        transactions.add(new Transaction(1, BigDecimal.ONE, "Mehran Kamal", 20, "Ali Khan", 20, null, false, null));
        transactions.add(new Transaction(12, BigDecimal.TEN, "Mehran Kamal", 20, "Ali Khan", 20, null, false, null));
        Mockito.when(transactionRepository.getAll()).thenReturn(transactions);

        BigDecimal totalAmount = transactionDataFetcher.getTotalTransactionAmount();

        Assert.assertEquals(0, totalAmount.compareTo(BigDecimal.valueOf(11)));
    }

    @Test
    public void testGetTotalSenderTransactionAmount_AllTransactionsFromOneSender() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1, BigDecimal.ONE, "Mehran Kamal", 20, "Ali Khan", 20, null, false, null));
        transactions.add(new Transaction(14, BigDecimal.TEN, "Mehran Kamal", 20, "Ali Khan", 20, null, false, null));
        Mockito.when(transactionRepository.getAll()).thenReturn(transactions);

        BigDecimal totalAmount = transactionDataFetcher.getTotalTransactionAmountSentBy("Mehran Kamal");
        Assert.assertEquals(0, totalAmount.compareTo(BigDecimal.valueOf(11)));

        BigDecimal totalAmountNonPresentSender = transactionDataFetcher.getTotalTransactionAmountSentBy("Ali Khan");
        Assert.assertEquals(0, totalAmountNonPresentSender.compareTo(BigDecimal.ZERO));
    }

    @Test
    public void testGetTotalSenderTransactionAmount_DifferentSenders() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1, BigDecimal.ONE, "Mehran Kamal", 20, "Ali Khan", 20, null, false, null));
        transactions.add(new Transaction(12, BigDecimal.TEN, "Ali Khan", 20, "Ali Khan", 20, null, false, null));
        Mockito.when(transactionRepository.getAll()).thenReturn(transactions);

        BigDecimal totalAmount = transactionDataFetcher.getTotalTransactionAmountSentBy("Mehran Kamal");
        Assert.assertEquals(0, totalAmount.compareTo(BigDecimal.ONE));

        BigDecimal totalAmountNonPresentSender = transactionDataFetcher.getTotalTransactionAmountSentBy("Ali Khan");
        Assert.assertEquals(0, totalAmountNonPresentSender.compareTo(BigDecimal.TEN));
    }

    @Test
    public void testGetMaxTransactionAmount_TransactionsPresent() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1, BigDecimal.ONE, "Mehran Kamal", 20, "Ali Khan", 20, null, false, null));
        transactions.add(new Transaction(13, BigDecimal.TEN, "Ali Khan", 20, "Ali Khan", 20, null, false, null));
        Mockito.when(transactionRepository.getAll()).thenReturn(transactions);

        BigDecimal maxTransactionAmount = transactionDataFetcher.getMaxTransactionAmount();
        Assert.assertEquals(0, maxTransactionAmount.compareTo(BigDecimal.TEN));
    }

    @Test
    public void testGetMaxTransactionAmount_NoTransactionsPresent() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        Mockito.when(transactionRepository.getAll()).thenReturn(transactions);

        Assert.assertThrows(NoSuchElementException.class, transactionDataFetcher::getMaxTransactionAmount);
    }

    @Test
    public void testCountUniqueClients_DifferentClients() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1, BigDecimal.ONE, "Mehran Kamal", 20, "Ali Khan", 20, null, false, null));
        transactions.add(new Transaction(14, BigDecimal.TEN, "Ali Khan", 20, "Ali Khan", 20, null, false, null));
        Mockito.when(transactionRepository.getAll()).thenReturn(transactions);

        Long totalClients = transactionDataFetcher.countUniqueClients();
        Assert.assertEquals(Long.valueOf(2), totalClients);
    }

    @Test
    public void testCountUniqueClients_SameClient() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1, BigDecimal.ONE, "Mehran Kamal", 20, "Mehran Kamal", 20, null, false, null));
        Mockito.when(transactionRepository.getAll()).thenReturn(transactions);

        Long totalClients = transactionDataFetcher.countUniqueClients();
        Assert.assertEquals(Long.valueOf(1), totalClients);
    }

    @Test
    public void testHasOpenComplianceIssues() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1, BigDecimal.ONE, "Mehran Kamal", 20, "Ali", 20, 32, false, "Looks like fraud"));
        transactions.add(new Transaction(12, BigDecimal.ONE, "Hamza", 20, "Mehran", 20, null, true, null));
        Mockito.when(transactionRepository.getAll()).thenReturn(transactions);

        Assert.assertTrue(transactionDataFetcher.hasOpenComplianceIssues("Mehran Kamal")); // Sender Case
        Assert.assertTrue(transactionDataFetcher.hasOpenComplianceIssues("Ali")); // Beneficiary Case
        Assert.assertFalse(transactionDataFetcher.hasOpenComplianceIssues("Hamza")); // No open issue client
    }

    @Test
    public void testGetTransactionsByBeneficiaryName() {
        ArrayList<Transaction> mehransTransactions = new ArrayList<>();
        mehransTransactions.add(new Transaction(1, BigDecimal.ONE, "Mehran", 20, "Mehran", 20, null, true, null));

        ArrayList<Transaction> alisTransactions = new ArrayList<>();
        alisTransactions.add(new Transaction(13, BigDecimal.ONE, "Mehran", 20, "Ali", 20, null, true, null));


        ArrayList<Transaction> allTransactions = new ArrayList<>();
        allTransactions.addAll(mehransTransactions);
        allTransactions.addAll(alisTransactions);

        Mockito.when(transactionRepository.getAll()).thenReturn(allTransactions);

        Map<String, List<Transaction>> result = transactionDataFetcher.getTransactionsByBeneficiaryName();

        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.containsKey("Mehran"));
        Assert.assertTrue(result.containsKey("Ali"));

        Assert.assertEquals(1, result.get("Mehran").size());
        Assert.assertEquals(1, result.get("Ali").size());

        Assert.assertEquals(mehransTransactions.get(0), result.get("Mehran").get(0));
        Assert.assertEquals(alisTransactions.get(0), result.get("Ali").get(0));
    }

    @Test
    public void testGetOpenComplianceIssueIds() {
        Integer unresolvedIssueId = 32;
        ArrayList<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1, BigDecimal.ONE, "Mehran Kamal", 20, "Ali", 20, unresolvedIssueId, false, "Looks like fraud"));
        transactions.add(new Transaction(3, BigDecimal.TEN, "Mehran Kamal", 20, "Ali", 20, 35, true, "Looks like fraud"));

        transactions.add(new Transaction(4, BigDecimal.ONE, "Hamza", 20, "Mehran", 20, null, true, null));
        Mockito.when(transactionRepository.getAll()).thenReturn(transactions);

        Set<Integer> result = transactionDataFetcher.getUnsolvedIssueIds();

        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.contains(unresolvedIssueId));
    }

    @Test
    public void testGetAllSolvedIssueMessages() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1, BigDecimal.ONE, "Mehran Kamal", 20, "Ali", 20, 32, true, "Legit transaction"));
        transactions.add(new Transaction(3, BigDecimal.TEN, "Mehran Kamal", 20, "Ali", 20, 35, false, "Looks like fraud"));
        transactions.add(new Transaction(4, BigDecimal.ONE, "Hamza", 20, "Mehran", 20, null, true, null));
        Mockito.when(transactionRepository.getAll()).thenReturn(transactions);

        List<String> issueSolvedMessages = transactionDataFetcher.getAllSolvedIssueMessages();

        Assert.assertEquals(1, issueSolvedMessages.size());
        Assert.assertEquals("Legit transaction", issueSolvedMessages.get(0));
    }

    @Test
    public void testGetTopSender() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1, BigDecimal.ONE, "Mehran Kamal", 20, "Ali", 20, 32, true, "Legit transaction"));
        transactions.add(new Transaction(3, BigDecimal.TEN, "Mehran Kamal", 20, "Ali", 20, 35, false, "Looks like fraud"));
        transactions.add(new Transaction(4, BigDecimal.TEN, "Hamza", 20, "Mehran", 20, null, true, null));
        Mockito.when(transactionRepository.getAll()).thenReturn(transactions);

        String topSender = transactionDataFetcher.getTopSender();

        Assert.assertTrue(topSender.equals("Mehran Kamal"));
    }

    @Test
    public void testGetTop3TransactionsByAmount() {
        ArrayList<Transaction> expectedTopTransactions = new ArrayList<>();
        expectedTopTransactions.add(new Transaction(6, BigDecimal.valueOf(4000), "Ben", 20, "Mehran", 20, null, true, null));
        expectedTopTransactions.add(new Transaction(5, BigDecimal.valueOf(3000), "Alex", 20, "Mehran", 20, null, true, null));
        expectedTopTransactions.add(new Transaction(4, BigDecimal.valueOf(5), "Hamza", 20, "Mehran", 20, null, true, null));


        ArrayList<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1, BigDecimal.ONE, "Mehran Kamal", 20, "Ali", 20, 32, true, "Legit transaction"));
        transactions.add(new Transaction(3, BigDecimal.valueOf(3), "Mehran Kamal", 20, "Ali", 20, 35, false, "Looks like fraud"));
        transactions.addAll(expectedTopTransactions);

        Mockito.when(transactionRepository.getAll()).thenReturn(transactions);

        List<Transaction> topTransactions = transactionDataFetcher.getTop3TransactionsByAmount();

        Assert.assertEquals(3, topTransactions.size());
        for (Integer i = 0; i<3; i++) {
            Assert.assertEquals(expectedTopTransactions.get(i), topTransactions.get(i));
        }
    }
}
