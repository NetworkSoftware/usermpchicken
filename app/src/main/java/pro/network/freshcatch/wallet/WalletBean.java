package pro.network.freshcatch.wallet;

import java.io.Serializable;

/**
 * Created by ravi on 16/11/17.
 */

public class WalletBean implements Serializable {
    String id;
    String paymentId;
    String type;
    String amount;
    String createdon;
    String operation;

    public WalletBean() {
    }

    public WalletBean(String id, String paymentId, String type, String amount) {
        this.id = id;
        this.paymentId = paymentId;
        this.type = type;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}