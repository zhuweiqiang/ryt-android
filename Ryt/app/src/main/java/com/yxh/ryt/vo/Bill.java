package com.yxh.ryt.vo;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/1/25.
 */
public class Bill implements Serializable {
    private String id;
    private String title;//标题
    private String detail;//账单详情
    private String status;  //0 废弃  1 可用
    private BigDecimal money;//账单金额
    private User author;
    private Date createDatetime;
    private String type;//账单类型  1 投资 2 拍卖订单  3 支付保证金 4 返还保证金 5 返利
    private String outOrIn;//1 进账 0 出账
    private String number;//支付订单唯一标示
    private String payWay;//支付方式 1 支付宝 2 微信
    private String flowAccount;//流水账号

    public Bill() {
    }

    public Bill(String id, String flowAccount, String payWay, String number, String type, String outOrIn, Date createDatetime, User author, BigDecimal money, String status, String title, String detail) {
        this.id = id;
        this.flowAccount = flowAccount;
        this.payWay = payWay;
        this.number = number;
        this.type = type;
        this.outOrIn = outOrIn;
        this.createDatetime = createDatetime;
        this.author = author;
        this.money = money;
        this.status = status;
        this.title = title;
        this.detail = detail;
    }

    public String getFlowAccount() {
        return flowAccount;
    }

    public void setFlowAccount(String flowAccount) {
        this.flowAccount = flowAccount;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOutOrIn() {
        return outOrIn;
    }

    public void setOutOrIn(String outOrIn) {
        this.outOrIn = outOrIn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
