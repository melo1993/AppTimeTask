package com.hrforce;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ActiveMQ  {

    public static void sendJobMessage(String msg,String jobid){
        String brokerURL= SystemProperties.get("activemq.brokerURL");
        System.out.println(brokerURL);
        String userName=SystemProperties.get("activemq.userName");
        System.out.println(userName);
        String password=SystemProperties.get("activemq.password");
        System.out.println(password);
        String queuename=SystemProperties.get("activemq.jobchange.queuename");
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName,password,brokerURL);
        Connection connection=null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(queuename);
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            TextMessage txtMsg = session.createTextMessage();
            txtMsg.setText(msg+":"+jobid);
            producer.send(txtMsg);
            session.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (null != connection)
                    connection.close();
            }
            catch (Throwable ignore) {

            }
        }
    }

}

