package com.globalroam.microservice.paymentgateway.api;

import com.globalroam.microservice.paymentgateway.entity.Card;
import com.globalroam.microservice.paymentgateway.enums.CardType;
import com.globalroam.microservice.paymentgateway.mapper.CardMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author zhangjian
 * @Date 2017/3/25
 * @Copyright:
 * @Describe:
 */
public class MybatisMapperTest extends WalletServiceApplicationTests {


    @Autowired
    CardMapper mapper;

    @Test
    public void selectAllCard() {
        List<Card> cardList = mapper.selectAll();
        System.out.println(cardList.get(0).getName());
        System.out.println(cardList.get(0).getType());
        System.out.println(cardList.get(0).getId());
        System.out.println(cardList.get(0).getValue());
    }

    @Test
    public void addCard() {
       Card card = new Card("会员卡aaa","a12342",100, CardType.VIP);
        mapper.insert(card);
    }

}
