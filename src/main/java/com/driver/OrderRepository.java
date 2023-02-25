package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public class OrderRepository {

    HashMap<String, Order> orders;
    HashMap<String, DeliveryPartner> deliveryPartners;
    HashMap<String, String> orderPartnerPair;
    HashMap<String, List<String>> partnerOrders;

    /*
    orders is map of orderId to order objects
    deliveryPartners is map of partnerId to DeliveryPartner objects
    orderPartnerPair is map of orderId to PartnerID
    partnerOrders is map of partnerID to list of orderIds assigned to him
     */

    public OrderRepository(){
        orders=new HashMap<>();
        deliveryPartners=new HashMap<>();
        orderPartnerPair=new HashMap<>();
        partnerOrders=new HashMap<>();
    }

    //1
    public void addOrder(Order order) {
        String orderId=order.getId();
        orders.put(orderId, order);
    }

    //2
    public void addPartner(String partnerId) {
        DeliveryPartner deliveryPartner=new DeliveryPartner(partnerId);
        deliveryPartners.put(partnerId, deliveryPartner);
    }

    //3
    public void addOrderPartnerPair(String orderId, String partnerId) {
        //get orderlist of partner and update with current order
        List<String> orderlist=partnerOrders.getOrDefault(partnerId, new ArrayList<>());
        orderlist.add(orderId);
        partnerOrders.put(partnerId,orderlist);

        //update orderpartner pair
        orderPartnerPair.put(orderId, partnerId);

        //update the number of orders in partner object
        DeliveryPartner deliveryPartner=deliveryPartners.get(partnerId);
        deliveryPartner.setNumberOfOrders(orderlist.size());
    }

    //4
    public Order getOrderById(String orderId) {
        return orders.get(orderId);
    }

    //5
    public DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartners.get(partnerId);
    }

    //6
    public Integer getOrderCountByPartnerId(String partnerId) {
        int countOfOrders;
        countOfOrders=deliveryPartners.get(partnerId).getNumberOfOrders();
        return countOfOrders;
    }

    //7
    public List<String> getOrdersByPartnerId(String partnerId) {
        return partnerOrders.get(partnerId);
    }

    //8
    public List<String> getAllOrders() {
        return new ArrayList<>(orders.keySet());
    }

    //9
    public Integer getCountOfUnassignedOrders() {
        // total number of orders =orders size and count of assigned orders= orderpartnerpair size
        return orders.size()-orderPartnerPair.size();
    }

    //10
    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        int countOfOrdersLeft=0;
        int timeInt=Order.getDeliveryTimeAsInt(time);
        List<String> orderList=partnerOrders.get(partnerId);

        for(String orderId:orderList){
            if(orders.get(orderId).getDeliveryTime()>timeInt){
                countOfOrdersLeft++;
            }
        }

        return countOfOrdersLeft;
    }

    //11
    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        int lastOrderTime=0;
        List<String> orderList=partnerOrders.get(partnerId);

        for(String orderId:orderList){
            lastOrderTime=Math.max(lastOrderTime, orders.get(orderId).getDeliveryTime());
        }

        return Order.getDeliveryTimeAsString(lastOrderTime);
    }

    //12
    public void deletePartnerById(String partnerId) {
        //get list of orders for partner
        List<String> orderList=partnerOrders.get(partnerId);
        //unassigned the orders managed by him by removing the pairings in order partner hashmap
        for(String orderID:orderList){
            orderPartnerPair.remove(orderID);
        }
        //removed from partner order list db
        partnerOrders.remove(partnerId);
        //remove partner from partners list db
        deliveryPartners.remove(partnerId);
    }

    //13
    public void deleteOrderById(String orderId) {
        //remove from orders hashmap
        orders.remove(orderId);

        //if it is assigned order get partner id and remove pairing from order-partner-pair db and from partner-orders list
        if(orderPartnerPair.containsKey(orderId)){
            String partnerId=orderPartnerPair.get(orderId);

            //removing from order assigning pair
            orderPartnerPair.remove(orderId);

            //removing from list or orders to be delivered by partner
            partnerOrders.get(partnerId).remove(orderId);

            deliveryPartners.get(partnerId).setNumberOfOrders( partnerOrders.get(partnerId).size());
        }

    }


}
