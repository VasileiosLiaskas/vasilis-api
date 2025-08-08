package vasilis.vasilis.business;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import vasilis.vasilis.invoice.Invoice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity

@Table(name = "business")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name ="date")
    private Date date;

    @Column(name = "type")
    private String type;

    @Column(name = "who")
        private String who;

    @Column (name = "area")
    private String area;

    @Column (name ="details")
    private String details;

    @Column (name = "costs")
    private Double costs;

    @Column (name = "fee")
    private Double fee;

    @Column (name = "advance_payment")
    private Double advancePayment;

    @Column (name ="remaining_money")
    private Double remainingMoney;

    @Column (name="payout")
    private Boolean payout;

    @Column (name="files_completed")
    private Boolean filesCompleted;

    @Column (name = "files_delivered")
    private Boolean filesDelivered;

    @Column (name = "comments")
    private String comments;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> invoices = new ArrayList<>();


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

    public Double getCosts() {
        return costs;
    }

    public void setCosts(Double costs) {
        this.costs = costs;
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

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }
}
