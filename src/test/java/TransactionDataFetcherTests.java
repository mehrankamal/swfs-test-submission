import com.smallworld.data.TransactionJsonRepository;
import com.smallworld.data.TransactionRepository;
import com.smallworld.domain.TransactionDataFetcher;
import com.smallworld.domain.entities.Transaction;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TransactionDataFetcherTests {

    private final TransactionRepository transactionRepository = Mockito.mock(TransactionJsonRepository.class);
    private final TransactionDataFetcher transactionDataFetcher = new TransactionDataFetcher(transactionRepository);

    @Test
    public void TestGetTotalTransactionAmount() {
//        Mockito.when(transactionRepository.getAll()).thenReturn(new ArrayList<>());

        ArrayList<Transaction> transactions = new ArrayList<Transaction>();

        transactions.add(new Transaction(1, BigDecimal.ONE, "Mehran Kamal", 20, "Ali Khan", 20, null, false, null));
        transactions.add(new Transaction(1, BigDecimal.TEN, "Mehran Kamal", 20, "Ali Khan", 20, null, false, null));
        Mockito.when(transactionRepository.getAll()).thenReturn(transactions);

        BigDecimal totalAmount = transactionDataFetcher.getTotalTransactionAmount();

        Assert.assertEquals(0, totalAmount.compareTo(BigDecimal.valueOf(11)));
    }
}
