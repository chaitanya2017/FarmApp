package monitor;

/*
* This class is represents monitor point
*/
public class Monitorpoint implements java.io.Serializable {
    // Fields
    private Integer monitorPointid;
    private String monitorPointName;

    /**
     * default constructor
     */
    public Monitorpoint() {
    }

    //Property accessors
    public Integer getMonitorPointid() {
        return monitorPointid;
    }

    public void setMonitorPointid(Integer monitorPointid) {
        this.monitorPointid = monitorPointid;
    }

    public String getMonitorPointName() {
        return this.monitorPointName;
    }

    public void setMonitorPointName(String monitorPointName) {
        this.monitorPointName = monitorPointName;
    }
}
