import java.util.ArrayList;

public class TxHandler {
    private UTXOPool utxoPool;
    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        // IMPLEMENT THIS
        this.utxoPool = new UTXOPool(utxoPool);
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        // IMPLEMENT THIS
        double sumInput = 0;
        double sumOutput = 0;
        ArrayList<UTXO> usedUTXO = new ArrayList<>();

        for (int i=0;i<tx.numInputs();i++) {
            Transaction.Input input = tx.getInput(i);
            int outputIndex = input.outputIndex;
            byte[] prevTxHash = input.prevTxHash;
            byte[] signature = input.signature;

            UTXO utxo1 = new UTXO(prevTxHash, outputIndex);

            //check rule (1): all outputs claimed by tx are in current UTXO pool
            if (!utxoPool.contains(utxo1)) {
                return false;
            }
            //check rule (2): the signatures on each input of tx are valid
            Transaction.Output output = utxoPool.getTxOutput(utxo1);
            byte[] message = tx.getRawDataToSign(i);
            if (!Crypto.verifySignature(output.address,message,signature)) {
                return false;
            }
            //check rule (3): no UTXO is claimed multiple times by tx
            if (usedUTXO.contains(utxo1)) {
                return false;
            }
            usedUTXO.add(utxo1);
            sumInput += output.value;
        }

        //check rule (4): all of tx output values are non-negative
        for (int i=0;i<tx.numOutputs();i++) {
            Transaction.Output output1 = tx.getOutput(i);
            if (output1.value < 0) {
                return false;
            }
            sumOutput += output1.value;
        }
        //check rule (5): the sum of tx input values is greater than or equal to the sum of its output values
        if (sumInput < sumOutput) {
            return false;
        }
        return true;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        // IMPLEMENT THIS
        ArrayList<Transaction> validTxs = new ArrayList<>();
        for (Transaction t : possibleTxs) {
            if (isValidTx(t)) {
                validTxs.add(t);

                //remove utxo
                for (Transaction.Input input : t.getInputs()) {
                    int outputIndex = input.outputIndex;
                    byte[] prevTxHash = input.prevTxHash;
                    UTXO utxo = new UTXO(prevTxHash, outputIndex);
                    utxoPool.removeUTXO(utxo);
                }
                //add new utxo
                byte[] hash = t.getHash(); //hash code of the transaction
                for (int i=0;i<t.numOutputs();i++) {
                    UTXO utxo = new UTXO(hash, i);
                    utxoPool.addUTXO(utxo, t.getOutput(i));
                }
            }
        }
        Transaction[] validTxsArr = new Transaction[validTxs.size()];
        validTxsArr = validTxs.toArray(validTxsArr);
        return validTxsArr;
    }

}
