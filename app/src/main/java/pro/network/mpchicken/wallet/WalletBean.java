package pro.network.mpchicken.wallet;

import java.io.Serializable;

/**
 * Created by ravi on 16/11/17.
 */

public class WalletBean implements Serializable {
    String id;
    String amt;
    String createdon;
    String operation;

    public WalletBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
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