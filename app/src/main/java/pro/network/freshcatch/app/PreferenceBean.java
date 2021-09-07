package pro.network.freshcatch.app;

public class PreferenceBean {
    private static PreferenceBean _instance;

    boolean enableBooking;



    public static PreferenceBean getInstance() {
        if (_instance == null) {
            _instance = new PreferenceBean();
        }
        return _instance;
    }


    public boolean isEnableBooking() {
        return enableBooking;
    }

    public void setEnableBooking(boolean enableBooking) {
        this.enableBooking = enableBooking;
    }


}
