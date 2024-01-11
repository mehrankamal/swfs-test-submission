package com.smallworld.domain;

import com.smallworld.data.TransactionRepository;
import com.smallworld.domain.entities.Transaction;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
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
    public BigDecimal getMaxTransactionAmount() throws NoSuchElementException {
        return repository
                .getAll()
                .stream()
                .map(Transaction::getAmount)
                .max(BigDecimal::compareTo)
                .orElseThrow();
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public Long countUniqueClients() {
        Set<String> clientNames = new HashSet<>();

        repository.getAll().forEach(
                transaction -> {
                    clientNames.add(transaction.getSenderFullName());
                    clientNames.add(transaction.getBeneficiaryFullName());
                }
        );

        return (long) clientNames.size();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public Boolean hasOpenComplianceIssues(String clientFullName) {
        return repository.getAll().stream()
                .anyMatch(transaction ->
                                !transaction.getIssueSolved() &&
                                (transaction.getBeneficiaryFullName().equals(clientFullName) || transaction.getSenderFullName().equals(clientFullName)));
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, List<Transaction>> getTransactionsByBeneficiaryName() {
        return repository.getAll().stream()
                .collect(Collectors.groupingBy(Transaction::getBeneficiaryFullName));
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        Set<Integer> openComplianceIssueIds = new HashSet<>();

        repository.getAll().stream()
                .filter(transaction -> !transaction.getIssueSolved())
                .forEach(transaction -> openComplianceIssueIds.add(transaction.getIssueId()));

        return openComplianceIssueIds;
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        return repository.getAll().stream()
                .filter(transaction -> transaction.getIssueId() != null && transaction.getIssueSolved())
                .map(Transaction::getIssueMessage)
                .collect(Collectors.toList());
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
    public String getTopSender() {
        Map<String, List<Transaction>> senderTransactionsMap = repository.getAll().stream()
                .collect(Collectors.groupingBy(Transaction::getSenderFullName));

        Map<String, BigDecimal> senderTotalAmountMap = senderTransactionsMap.entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                                .stream()
                                .map(Transaction::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                )
        );

        return senderTotalAmountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue()).get().getKey();
    }

}
