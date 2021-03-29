package pro.network.spirtual;

import java.io.Serializable;

public class RegMainbean implements Serializable {
    String id;
    String name;
    String mobile;
    String password;


    public RegMainbean() {
    }

    public RegMainbean(String name, String mobile, String password) {
        this.name = name;
        this.mobile = mobile;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}