package com.driver;

//import javax.annotation.Generated;
public class Order {


    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {
        String   stringHours1 = String.valueOf(deliveryTime.charAt(0) );
        String stringHours2 =String.valueOf(deliveryTime.charAt(1));
        String ans1=        stringHours1+stringHours2;
        int hours = Integer.parseInt(ans1);

        hours=hours*60;
        String  stringMinutes1 = String.valueOf(deliveryTime.charAt(3));
        String stringMinutes2=String.valueOf(deliveryTime.charAt(4));
        int minutes= Integer.parseInt(stringMinutes1+stringMinutes2);
        int ans =hours+minutes;


        this.id = id ;
        this.deliveryTime = ans;
//HH:MM;
        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
    }

    public String getId() {
        return id;
    }



    public static int getDeliveryTimeAsInt(String deliveryTime){
        return  (Integer.parseInt(deliveryTime.substring(0,2))*60)+ Integer.parseInt(deliveryTime.substring(3));
    }

    public static String getDeliveryTimeAsString(int deliveryTime){
        int hours = deliveryTime/60;
        int min = deliveryTime%60;

        String hrStr="";
        String minStr="";
        if(hours<10){
            hrStr="0"+hours;
        }
        else{
            hrStr=""+hours;
        }

        if(min<10){
            minStr="0"+min;
        }
        else{
            minStr=""+min;
        }

        return hrStr+":"+minStr;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
