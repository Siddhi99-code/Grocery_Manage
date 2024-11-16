package com.project.grocery.Entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Data
@Entity

public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    @Temporal(TemporalType.DATE)
    private Date billDate;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;
    private Double billAmount;
    
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private List<BillProduct> billProducts;

}