package com.tiscon.domain;

import java.io.Serializable;

public class Customer implements Serializable {

    private Integer customerId;

    private String oldPrefectureId;

    private String newPrefectureId;

    private String customerName;

    private String tel;

    private String email;

    private  String oldPostal;

    private String oldAddress;

    private String oldEtcAddress;

    private  String newPostal;

    private String newAddress;

    private String newEtcAddress;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getOldPrefectureId() {
        return oldPrefectureId;
    }

    public void setOldPrefectureId(String oldPrefectureId) {
        this.oldPrefectureId = oldPrefectureId;
    }

    public String getNewPrefectureId() {
        return newPrefectureId;
    }

    public void setNewPrefectureId(String newPrefectureId) {
        this.newPrefectureId = newPrefectureId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOldPostal() {
        return oldPostal;
    }

    public void setOldPostal(String oldPostal) {
        this.oldPostal = oldPostal;
    }

    public String getOldAddress() {
        return oldAddress;
    }

    public void setOldAddress(String oldAddress) {
        this.oldAddress = oldAddress;
    }

    public String getOldEtcAddress() {
        return oldEtcAddress;
    }

    public void setOldEtcAddress(String oldEtcAddress) {
        this.oldEtcAddress = oldEtcAddress;
    }

    public String getNewPostal() {
        return newPostal;
    }

    public void setNewPostal(String newPostal) {
        this.newPostal = newPostal;
    }

    public String getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(String newAddress) {
        this.newAddress = newAddress;
    }

    public String getNewEtcAddress() {
        return newEtcAddress;
    }

    public void setNewEtcAddress(String newEtcAddress) {
        this.newEtcAddress = newEtcAddress;
    }
}
