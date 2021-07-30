package lk.cafemambo.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "EVENTBOOKINGDETAILS")
public class EventBookingDetailsEntity {

    @Id
    @Column(length = 50)
    private String id;

    @Column(length = 10)
    private String status;

    @Column(length = 50)
    private String createBy;

    private Date createDate;

    @Column(length = 50)
    private String updateBy;

    private Date updateDate;

    @ManyToOne
    @JoinColumn(name = "eventbooking_id")
    private EventBookingEntity eventBookingEntity;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private PackageEntity packageEntity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public EventBookingEntity getEventBookingEntity() {
        return eventBookingEntity;
    }

    public void setEventBookingEntity(EventBookingEntity eventBookingEntity) {
        this.eventBookingEntity = eventBookingEntity;
    }

    public PackageEntity getPackageEntity() {
        return packageEntity;
    }

    public void setPackageEntity(PackageEntity packageEntity) {
        this.packageEntity = packageEntity;
    }
}
