package com.joker.microservice.paymentgateway.entity;

import com.joker.microservice.paymentgateway.enums.CardType;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @Author zhangjian
 * @Date 2017/3/21
 * @Copyright:
 * @Describe:
 */
//@Table(name = "ws_card")
public class Card {

    /**
     * 唯一主见UUID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "UUID")
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 编号
     */
    private String number;

    /**
     * 值-为0.00*100
     */
    private Integer value;

    /**
     * 类型
     */
    private CardType type;


    public Card(String name, String number, Integer value, CardType type) {
        this.name = name;
        this.number = number;
        this.value = value;
        this.type = type;
    }

    public Card() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }
}
