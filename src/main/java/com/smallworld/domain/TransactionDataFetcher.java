package com.smallworld.domain;

import com.smallworld.data.TransactionRepository;
import com.smallworld.domain.entities.Transaction;

import java.math.BigDecimal;
import java.util.*;
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
    public Map<String, ArrayList<Transaction>> getTransactionsByBeneficiaryName() {
        Map<String, ArrayList<Transaction>> beneficiaryToTransactionsMap = new HashMap<>();

        repository.getAll().forEach(
                transaction -> {
                    String beneficiaryName = transaction.getBeneficiaryFullName();
                    if(beneficiaryToTransactionsMap.containsKey(beneficiaryName)) {
                        beneficiaryToTransactionsMap.get(beneficiaryName).add(transaction);
                    } else {
                        ArrayList<Transaction> beneficiaryTransactions = new ArrayList<>();
                        beneficiaryTransactions.add(transaction);
                        beneficiaryToTransactionsMap.put(beneficiaryName, beneficiaryTransactions);
                    }
                }
        );

        return beneficiaryToTransactionsMap;
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
