import java.util.Date;

public class AvailablePharmacy {
    private long id;
    private int pharmacyId;
    private int workingHourId;
    private Date date;
    private int pulledVersion;

    public AvailablePharmacy() {

    }

    public AvailablePharmacy(int id, int pharmacyId, int workingHourId, Date date, int pulledVersion) {
        this.id = id;
        this.pharmacyId = pharmacyId;
        this.workingHourId = workingHourId;
        this.date = date;
        this.pulledVersion = pulledVersion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(int pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public int getWorkingHourId() {
        return workingHourId;
    }

    public void setWorkingHourId(int workingHourId) {
        this.workingHourId = workingHourId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPulledVersion() {
        return pulledVersion;
    }

    public void setPulledVersion(int pulledVersion) {
        this.pulledVersion = pulledVersion;
    }

    @Override
    public String toString() {
        return "AvailablePharmacy{" +
                "id=" + id +
                ", pharmacyId=" + pharmacyId +
                ", workingHourId=" + workingHourId +
                ", date=" + date +
                '}';
    }
}
