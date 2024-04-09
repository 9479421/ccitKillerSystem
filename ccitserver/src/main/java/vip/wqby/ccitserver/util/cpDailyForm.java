package vip.wqby.ccitserver.util;

public class cpDailyForm {
    private String title;
    private String value;
    private String isNeed;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIsNeed() {
        return isNeed;
    }

    public void setIsNeed(String isNeed) {
        this.isNeed = isNeed;
    }

    @Override
    public String toString() {
        return "cpDailyForm{" +
                "title='" + title + '\'' +
                ", value='" + value + '\'' +
                ", isNeed='" + isNeed + '\'' +
                '}';
    }
}
