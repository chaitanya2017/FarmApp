package monitor;

/*
* This class manages farm and their monitor points
*/
public class Farm {

    //Fields
    private Float latitude;
    private Float longitude;
    private Integer farmid;
    private String farmname;
    private Integer fieldid;
    private String fieldname;
    private String monitorpointname;
    private Integer monitorpointid;
    private String sreferenceimagepath;

    // Constructors
    public Farm() {
    }

    public Farm(Float latitude, Float longitude, String fieldname, String farmname, Integer fieldid, Integer farmid) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.fieldname = fieldname;
        this.farmname = farmname;
        this.fieldid = fieldid;
        this.farmid = farmid;
    }

    //Property accessors
    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public String getFarmname() {
        return farmname;
    }

    public void setFarmname(String farmname) {
        this.farmname = farmname;
    }

    public Float getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Integer getFieldid() {
        return fieldid;
    }

    public void setFieldid(Integer farmid) {
        this.fieldid = farmid;
    }

    public Integer getFarmid() {
        return farmid;
    }

    public void setFarmid(Integer farmid) {
        this.farmid = farmid;
    }

    public String getMonitorpointname() {
        return monitorpointname;
    }

    public void setMonitorpointname(String monitorpointname) {
        this.monitorpointname = monitorpointname;
    }

    public Integer getMonitorpointid() {
        return monitorpointid;
    }

    public void setMonitorpointid(Integer monitorpointid) {
        this.monitorpointid = monitorpointid;
    }

    
    public String getSreferenceimagepath() {
        return sreferenceimagepath;
    }

    public void setSreferenceimagepath(String sreferenceimagepath) {
        this.sreferenceimagepath = sreferenceimagepath;
    }
}
