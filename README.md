# ScroogeCoin
A Centralized Cryptocurrency

There is a central authority Scrooge that has the sole ability to create coins and he receives transaction from users, which he validates before publishing a list of validated transactions. The ledger is implemented using blockchain. Although coins are immutable in this system, it has all the flexibility of a system that didn’t have immutable coins.
Care is taken in the implementation to withstand double spending attacks.

In ScroogeCoin, there are two kinds of transactions: CreateCoins and PayCoins.

In CreateCoins, Scrooge can create multiple coins at a time by simply signing a statement that he’s making a new coin, each with a unique coin ID.

The second kind of transaction is PayCoins. It consumes some coins, that is, destroys them, and creates new coins of the same total value. The new coins might belong to different people (public keys). This transaction has to be signed by everyone who’s paying in a coin. So if you’re the owner of one of the coins that’s going to be consumed in this transaction, then you need to digitally sign the transaction to say that you’re really okay with spending this coin.

The rules of ScroogeCoin say that PayCoins transaction is valid if four things are true:
● The consumed coins are valid, that is, they really were created in previous transactions.
● The consumed coins were not already consumed in some previous transaction. That is, that
this is not a double‐spend.
● The total value of the coins that come out of this transaction is equal to the total value of the
coins that went in. That is, only Scrooge can create new value.
● The transaction is validly signed by the owners of all of the consumed coins.

If all of those conditions are met, then this PayCoins transaction is valid and Scrooge will accept it. He’ll write it into the history by appending it to the block chain, after which everyone can see that this transaction has happened.

