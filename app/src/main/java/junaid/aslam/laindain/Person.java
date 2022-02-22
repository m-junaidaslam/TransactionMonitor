package junaid.aslam.laindain;

/**
 * Created by M.Junaid Aslam on 02-01-2015.
 */
public class Person {

    private String name;
    private String amount;
    private String status;
    private String date;

    public String getDate() { return date; }

    public void setDate(String date) {this.date = date; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Person() {
        name = "";
        amount = "";
        status = "Take";
        date = "1-1-15";
    }

    public Person(String name, String amount, String status, String date) {
        this.name = name;
        this.amount = amount;
        this.status = status;
        this.date = date;
    }
}

