package com.smallworld.domain;

import com.smallworld.data.TransactionRepository;
import com.smallworld.domain.entities.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class TransactionDataFetcher {

    private final TransactionRepository repository;

    public TransactionDataFetcher(TransactionRepository repository){
        this.repository = repository;
    }

    private BigDecimal sumTransactionAmount(Stream<Transaction> transactionStream) {
        return transactionStream
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Returns the sum of the amounts of all transactions
     */
    public BigDecimal getTotalTransactionAmount() {
        return sumTransactionAmount(repository.getAll().stream());
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public BigDecimal getTotalTransactionAmountSentBy(String senderFullName) {
        Stream<Transaction> senderTransactionsStream = repository
                .getAll()
                .stream()
                .filter(transaction -> transaction.getSenderFullName().equals(senderFullName));

        return sumTransactionAmount(senderTransactionsStream);
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        throw new UnsupportedOperationException();
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, Object> getTransactionsByBeneficiaryName() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    public List<Object> getTop3TransactionsByAmount() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the sender with the most total sent amount
     */
    public Optional<Object> getTopSender() {
        throw new UnsupportedOperationException();
    }

}
