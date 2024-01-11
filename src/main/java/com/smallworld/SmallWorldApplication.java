package com.smallworld;

import com.smallworld.config.Config;
import com.smallworld.data.TransactionJsonRepository;
import com.smallworld.domain.TransactionDataFetcher;
import com.smallworld.domain.entities.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SmallWorldApplication {

    private static void printApplicationIntroduction(String transactionsSource) {
        System.out.println("--------Smallworld Transactions Fetcher Application--------");
        System.out.println("-----------------------------------------------------------");
        System.out.println("Transactions Source: " + transactionsSource);
        System.out.println("-----------------------------------------------------------");
    }

    private static void printApplicationExecution(TransactionDataFetcher transactionDataFetcher) {
        System.out.println("Total Sum of Transactions: " + transactionDataFetcher.getTotalTransactionAmount());
        System.out.println("Total Transaction Amount Sent by \"Tom Shelby\": " + transactionDataFetcher.getTotalTransactionAmountSentBy("Tom Shelby"));
        System.out.println("Max Transaction Amount: " + transactionDataFetcher.getMaxTransactionAmount());
        System.out.println("Number of Unique Clients: " + transactionDataFetcher.countUniqueClients());
        System.out.println("Does \"Aunt Polly\" have transactions with open compliance issues? " + transactionDataFetcher.hasOpenComplianceIssues("Aunt Polly"));
        printTransactionsByBeneficiaries(transactionDataFetcher);
        System.out.println("Unresolved issue Ids: " + String.join(",", transactionDataFetcher.getUnsolvedIssueIds().stream().map(Object::toString).toList()));
        printIssueSolvedMessages(transactionDataFetcher);
        printTop3Transactions(transactionDataFetcher);
        System.out.println("Sender with highest total sent amount: " + transactionDataFetcher.getTopSender());
    }

    private static void printTransactionsByBeneficiaries(TransactionDataFetcher transactionDataFetcher) {
        Map<String, List<Transaction>> allTransactions = transactionDataFetcher.getTransactionsByBeneficiaryName();
        System.out.println("All transactions indexed by beneficiary name: ");
        System.out.println("-----------------------------------------------------------");
        for (Map.Entry<String, List<Transaction>> entry : allTransactions.entrySet()) {
            System.out.println("Transactions by " + entry.getKey());
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            for(Transaction transaction : entry.getValue()) {
                System.out.println(transaction);
            }
        }
        System.out.println("-----------------------------------------------------------");
    }

    private static void printIssueSolvedMessages(TransactionDataFetcher transactionDataFetcher) {
        System.out.println("Solved issue messages: ");
        List<String> solvedIssueMessages = transactionDataFetcher.getAllSolvedIssueMessages();
        System.out.println("-----------------------------------------------------------");
        for (String message: solvedIssueMessages) {
            System.out.println(message);
        }
        System.out.println("-----------------------------------------------------------");
    }

    private static void printTop3Transactions(TransactionDataFetcher transactionDataFetcher) {
        System.out.println("Top 3 transactions by amount: ");
        List<Transaction> top3Transactions = transactionDataFetcher.getTop3TransactionsByAmount();
        System.out.println("-----------------------------------------------------------");
        for (Transaction transaction : top3Transactions) {
            System.out.println("Amount: " + transaction.getAmount() + ", Transaction: " + transaction);
        }
        System.out.println("-----------------------------------------------------------");
    }

    public static void main(String[] args) throws Exception {
        Properties properties = Config.loadConfig();
        String transactionsSource = (String) properties.get("smallworld.transactions-source");

        TransactionJsonRepository transactionJsonRepository = new TransactionJsonRepository(transactionsSource);
        TransactionDataFetcher transactionDataFetcher = new TransactionDataFetcher(transactionJsonRepository);

        printApplicationIntroduction(transactionsSource);
        printApplicationExecution(transactionDataFetcher);
    }
}
