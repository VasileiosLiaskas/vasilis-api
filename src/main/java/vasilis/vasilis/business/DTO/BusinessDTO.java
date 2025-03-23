package vasilis.vasilis.business.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import vasilis.vasilis.general.CustomDateDeserializer;
import vasilis.vasilis.general.CustomDateSerializer;

import java.util.Date;

public class BusinessDTO {

    private Integer id;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date date;
    private String type;
    private String who;
    private String area;
    private String details;
    private Double fee;
    private Double advancePayment;
    private Double remainingMoney;
    private Boolean payout;
    private Boolean filesCompleted;
    private Boolean filesDelivered;
    private String comments;

    public BusinessDTO() {
    }

    public BusinessDTO(Integer id, String type, String who, String area, String details,Date date, String comments) {
        this.id = id;
        this.type = type;
        this.who = who;
        this.area = area;
        this.details = details;
        this.date = date;
        this.comments = comments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Double getAdvancePayment() {
        return advancePayment;
    }

    public void setAdvancePayment(Double advancePayment) {
        this.advancePayment = advancePayment;
    }

    public Double getRemainingMoney() {
        return remainingMoney;
    }

    public void setRemainingMoney(Double remainingMoney) {
        this.remainingMoney = remainingMoney;
    }

    public Boolean getPayout() {
        return payout;
    }

    public void setPayout(Boolean payout) {
        this.payout = payout;
    }

    public Boolean getFilesCompleted() {
        return filesCompleted;
    }

    public void setFilesCompleted(Boolean filesCompleted) {
        this.filesCompleted = filesCompleted;
    }

    public Boolean getFilesDelivered() {
        return filesDelivered;
    }

    public void setFilesDelivered(Boolean filesDelivered) {
        this.filesDelivered = filesDelivered;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
