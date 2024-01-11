# Implementation Notes

## Problem Statement:
To implement the `com.smallworld.domain.TransactionDataFetcher` class correctly, following the correct and efficient coding practices.

## Approach:

I follow a TDD approach to implement the specification defined in the given class following a layered approach.

## Architecture:

I follow a layered architecture as follows.

- **Presentation Layer**: This contains the main class to display the computation in console for the given dataset.
- **Domain Layer**: This layer contains the core logic of the implementation and the entities related to the domain i.e. `Transaction` class in this case. The `com.smallworld.domain.TransactionDataFetcher` is also a part of this layer
- **Data Layer**: This layer is responsible for loading the data and making it available for the Domain layer to perform relevant computation(s).

## Testing:

Testing is done for the Domain layer, as it is the center of our application and contains the business rules for computing.

# Welcome to our coding test!

Your solution to this coding test will be evaluated based on its:
 * Adherence to best coding practices
 * Correctness
 * Efficiency

Take your time to fully understand the problem and formulate a plan before starting to code, and don't hesitate to ask any questions if you have doubts.

# Objective

Since we are a money transfer company this test will revolve around a (very) simplified transaction model. Our aim is to implement the methods listed in `com.smallworld.domain.TransactionDataFetcher`, a component that allows us to get some insight into the transactions our system has.

A battery of test transactions is stored in `transactions.json` that is going to be used as a datasource for all our data mapping needs.

Each entry in `transactions.json` consists of:
 * mtn: unique identifier of the transaction
 * amount
 * senderFullName, senderAge: sender information
 * beneficiaryFullName, beneficiaryAge: beneficiary information
 * issueId, issueSolved, issueMessage: issue information. Transactions can:
   * Contain no issues: in this case, issueId = null.
   * Contain a list of issues: in this case, the transaction information will be repeated in different entries in `transactions.json` changing the issue related information.

Each method to be implemented includes a brief description of what's expected of it.

The parameters and return types of each method can be modified to fit the model that contains the transaction information

Have fun!