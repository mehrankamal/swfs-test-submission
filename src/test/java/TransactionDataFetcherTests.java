import com.smallworld.data.TransactionJsonRepository;
import com.smallworld.data.TransactionRepository;
import com.smallworld.domain.TransactionDataFetcher;
import com.smallworld.domain.entities.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class TransactionDataFetcherTests {

    private final TransactionRepository transactionRepository = Mockito.mock(TransactionJsonRepository.class);
    private final TransactionDataFetcher transactionDataFetcher = new TransactionDataFetcher(transactionRepository);

    @Test
    public void testGetTotalTransactionAmount() {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();

        transactions.add(new Transaction(1, BigDecimal.ONE, "Mehran Kamal", 20, "Ali Khan", 20, null, false, null));
        transactions.add(new Transaction(1, BigDecimal.TEN, "Mehran Kamal", 20, "Ali Khan", 20, null, false, null));
        Mockito.when(transactionRepository.getAll()).thenReturn(transactions);

        BigDecimal totalAmount = transactionDataFetcher.getTotalTransactionAmount();

        Assert.assertEquals(0, totalAmount.compareTo(BigDecimal.valueOf(11)));
    }

    @Test
    public void testGetTotalSenderTransactionAmount_AllTransactionsFromOneSender() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1, BigDecimal.ONE, "Mehran Kamal", 20, "Ali Khan", 20, null, false, null));
        transactions.add(new Transaction(1, BigDecimal.TEN, "Mehran Kamal", 20, "Ali Khan", 20, null, false, null));
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
        transactions.add(new Transaction(1, BigDecimal.TEN, "Ali Khan", 20, "Ali Khan", 20, null, false, null));
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
        transactions.add(new Transaction(1, BigDecimal.TEN, "Ali Khan", 20, "Ali Khan", 20, null, false, null));
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
}
