package vasilis.vasilis.business.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import vasilis.vasilis.general.CustomDateDeserializer;
import vasilis.vasilis.general.CustomDateSerializer;
import vasilis.vasilis.invoice.DTO.InvoiceDTO;

import java.util.Date;
import java.util.List;

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
    private Double costs;
    private Double advancePayment;
    private Double remainingMoney;
    private Boolean payout;
    private Boolean filesCompleted;
    private Boolean filesDelivered;
    private String comments;
    private Double totalIncome;
    private Long totalRecords;
    private List<InvoiceDTO> invoices;

    public BusinessDTO() {
    }

    public BusinessDTO(Integer id, String type, String who, String area,
                       String details, Date date, String comments,
                       Double fee, Double advancePayment, Double remainingMoney,
                       Boolean payout, Boolean filesCompleted, Boolean filesDelivered, Double costs) {
        this.id = id;
        this.type = type;
        this.who = who;
        this.area = area;
        this.details = details;
        this.date = date;
        this.comments = comments;
        this.fee = fee;
        this.advancePayment = advancePayment;
        this.remainingMoney = remainingMoney;
        this.payout = payout;
        this.filesCompleted = filesCompleted;
        this.filesDelivered = filesDelivered;
        this.costs=costs;
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


    public Boolean getFilesCompleted() {
        return filesCompleted;
    }

    public void setFilesCompleted(Boolean filesCompleted) {
        this.filesCompleted = filesCompleted;
    }

    public Boolean getFilesDelivered() {
        return filesDelivered;
    }

    public Double getCosts() {
        return costs;
    }

    public void setCosts(Double costs) {
        this.costs = costs;
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

    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public Boolean getPayout() {
        return payout;
    }

    public void setPayout(Boolean payout) {
        this.payout = payout;
    }

    public Long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<InvoiceDTO> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<InvoiceDTO> invoices) {
        this.invoices = invoices;
    }
}
