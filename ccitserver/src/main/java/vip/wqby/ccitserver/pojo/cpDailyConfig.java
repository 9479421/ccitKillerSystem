package vip.wqby.ccitserver.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import vip.wqby.ccitserver.util.cpDailyForm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "cpdaily")
public class cpDailyConfig {
    private String lon;
    private String lat;
    private String address;
    private LinkedList<cpDailyForm> forms;

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LinkedList<cpDailyForm> getForms() {
        return forms;
    }

    public void setForms(LinkedList<cpDailyForm> forms) {
        this.forms = forms;
    }

    @Override
    public String toString() {
        return "cpDailyConfig{" +
                "lon='" + lon + '\'' +
                ", lat='" + lat + '\'' +
                ", address='" + address + '\'' +
                ", forms=" + forms +
                '}';
    }
}
