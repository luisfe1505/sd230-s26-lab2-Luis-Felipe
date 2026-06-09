package csd230.s26.lab1.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@DiscriminatorValue("MAGAZINE")
public class MagazineEntity extends PublicationEntity {
    private int orderQty;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime currentIssue;

    public MagazineEntity() {
        super();
        this.currentIssue = LocalDateTime.now();
    }

    public MagazineEntity(int orderQty, LocalDateTime currentIssue, String title, double price, int copies) {
        super(title, price, copies);
        this.orderQty = orderQty;
        this.currentIssue = currentIssue;
    }

    public int getOrderQty() { return orderQty; }
    public void setOrderQty(int orderQty) { this.orderQty = orderQty; }

    public LocalDateTime getCurrentIssue() { return currentIssue; }
    public void setCurrentIssue(LocalDateTime currentIssue) { this.currentIssue = currentIssue; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MagazineEntity that = (MagazineEntity) o;
        return orderQty == that.orderQty && Objects.equals(currentIssue, that.currentIssue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), orderQty, currentIssue);
    }

    @Override
    public String toString() {
        return "MagazineEntity{" +
                "orderQty=" + orderQty +
                ", currentIssue=" + currentIssue +
                "} " + super.toString();
    }

    @Override
    public void sellItem() {
        System.out.println("Selling Magazine");
    }
}