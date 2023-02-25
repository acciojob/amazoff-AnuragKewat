package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public class OrderRepository {
    private Map<String, Order> orderMap;
    private Map<String, DeliveryPartner> partnerMap;
    private Map<String, List<Order>> orderPartnerPairMap;
    private Map<String, Order> unassignedOrderMap;

    public OrderRepository() {
        orderMap = new HashMap<>();
        partnerMap = new HashMap<>();
        orderPartnerPairMap = new HashMap<>();
        unassignedOrderMap = new HashMap<>();
    }
    public void addOrder(Order order) {
        orderMap.put(order.getId(), order);
        unassignedOrderMap.put(order.getId(), order);
    }
    public void addPartner(String id) {
        DeliveryPartner deliveryPartner = new DeliveryPartner(id);
        partnerMap.put(deliveryPartner.getId(), deliveryPartner);
    }
    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(unassignedOrderMap.containsKey(orderId)) {
            Order currentOrder = unassignedOrderMap.get(orderId);
            unassignedOrderMap.remove(orderId);
            DeliveryPartner currentPartner = partnerMap.get(partnerId);
            currentPartner.setNumberOfOrders(currentPartner.getNumberOfOrders()+1);
            partnerMap.put(partnerId, currentPartner);
            List<Order> list = new ArrayList<>();

            if(orderPartnerPairMap.containsKey(partnerId)) {
               list = orderPartnerPairMap.get(partnerId);
            }

            list.add(currentOrder);
            orderPartnerPairMap.put(partnerId, list);
        }
    }
    public void deletePartnerById(String partnerId) {
        List<Order> orderList = orderPartnerPairMap.get(partnerId);
        partnerMap.remove(partnerId);
        orderPartnerPairMap.remove(partnerId);
        for(Order order: orderList) {
            unassignedOrderMap.put(order.getId(), order);
        }
    }
    public Order getOrderById(String orderId) {
        return orderMap.get(orderId);
    }
    public DeliveryPartner getPartnerById(String partnerId) {
        return partnerMap.get(partnerId);
    }
    public int getOrderCountByPartnerId(String partnerId) {
        return orderPartnerPairMap.get(partnerId).size();
    }
    public List<Order> getOrdersByPartnerId(String partnerId) {
        return orderPartnerPairMap.get(partnerId);
    }
    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        for(String id: orderMap.keySet()) {
            orderList.add(orderMap.get(id));
        }
        return orderList;
    }
    public int getCountOfUnassignedOrders() {
        return unassignedOrderMap.size();
    }
    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        String[] curTime = time.split(":");
        int currentTime = Integer.parseInt(curTime[0])*60 + Integer.parseInt(curTime[1]);
        int count=0;
        if(orderPartnerPairMap.containsKey(partnerId)) {
            for(Order order: orderPartnerPairMap.get(partnerId)) {
                if(order.getDeliveryTime()>currentTime) count++;
            }
        }
        return count;
    }
    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        List<Order> orderList = orderPartnerPairMap.get(partnerId);
        int max = 0;
        for(Order order: orderList) {
            max = Math.max(max,order.getDeliveryTime());
        }

        int HH = max/60;
        int MM = max%60;
        StringBuilder sb = new StringBuilder();
        sb.append(HH);
        sb.append(":");
        sb.append(MM);

        return sb.toString();
    }
    public void deleteOrderById(String orderId) {
        Order order = orderMap.get(orderId);
        orderMap.remove(orderId);
        unassignedOrderMap.remove(orderId);
        for(String partner: orderPartnerPairMap.keySet()) {
            List<Order> orderList = orderPartnerPairMap.get(partner);
            for(Order currentOrder: orderList) {
                if(currentOrder.equals(order)) {
                    orderList.remove(order);
                    DeliveryPartner del = partnerMap.get(partner);
                    del.setNumberOfOrders(del.getNumberOfOrders()-1);
                    partnerMap.put(partner, del);
                }
            }
        }
    }
}
