package pro.network.freshcatch.app;

public class PreferenceBean {
    private static PreferenceBean _instance;

    boolean enableBooking;
    int shippingCost;



    public static PreferenceBean getInstance() {
        if (_instance == null) {
            _instance = new PreferenceBean();
        }
        return _instance;
    }

    public int getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(int shippingCost) {
        this.shippingCost = shippingCost;
    }

    public boolean isEnableBooking() {
        return enableBooking;
    }

    public void setEnableBooking(boolean enableBooking) {
        this.enableBooking = enableBooking;
    }


}
