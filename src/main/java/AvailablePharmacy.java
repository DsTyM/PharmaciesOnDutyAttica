import java.util.Date;

public class AvailablePharmacy {
    private int id;
    private int pharmacyId;
    private int workingHourId;
    private Date pulledDateTime;

    public AvailablePharmacy() {

    }

    public AvailablePharmacy(int id, int pharmacyId, int workingHourId, Date pulledDateTime) {
        this.id = id;
        this.pharmacyId = pharmacyId;
        this.workingHourId = workingHourId;
        this.pulledDateTime = pulledDateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Date getPulledDateTime() {
        return pulledDateTime;
    }

    public void setPulledDateTime(Date pulledDateTime) {
        this.pulledDateTime = pulledDateTime;
    }

    @Override
    public String toString() {
        return "AvailablePharmacy{" +
                "id=" + id +
                ", pharmacyId=" + pharmacyId +
                ", workingHourId=" + workingHourId +
                ", pulledDateTime=" + pulledDateTime +
                '}';
    }
}
