package threads;

import com.joker.microservice.paymentgateway.entity.PaymentOrder;
import com.joker.microservice.paymentgateway.exception.DataNotFoundException;
import com.joker.microservice.paymentgateway.exception.ServiceException;
import com.joker.microservice.paymentgateway.service.PaymentNotificationService;
import com.joker.microservice.paymentgateway.service.PaymentOrderService;
import com.joker.microservice.paymentgateway.service.impl.PaymentNotifucationServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by Administrator on 2017/5/9.
 */
public class NotificationThread extends Thread {

    private static Logger logger = LogManager.getLogger(NotificationThread.class);
    private PaymentOrder paymentOrder;
    private PaymentOrderService paymentOrderService;

    public NotificationThread(PaymentOrder paymentOrder,PaymentOrderService paymentOrderService) {
        this.paymentOrder = paymentOrder;
        this.paymentOrderService = paymentOrderService;
    }

    @Override
    public void run() {
        boolean notifyResult = false;

        while (notifyResult == false) {
            logger.info("start notify server with payment order :" + paymentOrder.toString());
            PaymentNotificationService paymentNotificationService = new PaymentNotifucationServiceImpl();
            notifyResult = paymentNotificationService.notification(paymentOrder);
            logger.info(" notify server result :" + notifyResult);
            if (notifyResult) {
                paymentOrder.setStatus(PaymentOrder.STATUS_COMPLETE);
                try {
                    paymentOrderService.update(paymentOrder);
                } catch (ServiceException e) {
                    e.printStackTrace();
                } catch (DataNotFoundException e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    logger.info(" after 5s try to notify again :" + paymentOrder.toString());
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }



    }
}
