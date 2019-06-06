package com.caimao.hq.njs.server.service;

import com.caimao.hq.njs.api.entity.Holiday;
import com.caimao.hq.njs.api.entity.TradeTime;
import com.caimao.hq.njs.server.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by Administrator on 2015/10/29.
 */
public   class HolidayHander {


    private   String   tradeDate="";//交易时间
    private   List<String>  tradeTimeList=new ArrayList();//交易时间,日内



    private Logger logger = Logger.getLogger(HolidayHander.class);


    public  HolidayHander(Holiday holiday){

        if(null!=holiday){

            initTradeDate(holiday.getHoliday());
            initTradeTime(holiday.getTradeTime());

        }else{

           throw new RuntimeException("节假日设置错误：节假日为空,没有设置ZS,"+ DateUtils.getNoTime("yyyy")+"的节假日和交易时间");

        }

    }

    private void initTradeDate(String holiday){

        if(!StringUtils.isBlank(holiday)&&holiday.length()==8){
                tradeDate=holiday;
        }else{
            throw new RuntimeException("交易时间格式错误，必须为yyyyMMdd");
        }

    }

    public Boolean isTrade(Date nowTime){
        Boolean isTradeTime=false;
        if(checkTradeDate(nowTime)){
            if(checkTradeTime(nowTime)){
                isTradeTime=true;
            }
        }
        return isTradeTime;
    }

    private Boolean checkTradeDate(Date nowTime){

        Boolean isTradeTime=false;
        if(DateUtils.getDate(nowTime, "yyyyMMdd").equals(tradeDate)){
            isTradeTime=true;
        }
        return isTradeTime;
    }
    /*
     * 返回true表示为交易时间,false为停盘时间
     *
     * */
    private Boolean checkTradeTime(Date nowTime){

        Boolean isTradeTime=false;
        if(isInTradeTimeList(nowTime,"hhmm")){//是否在交易时间段内
            isTradeTime=true;
        }
        return isTradeTime;
    }



    private List<TradeTime>  convertTradeTime(Date nowDate,List<String> tradeTimeList){
        List<TradeTime> list=new ArrayList();
        if(null!=tradeTimeList){
            for(String str:tradeTimeList){

                if(!StringUtils.isBlank(str)&&str.length()==9){
                    TradeTime tradeTime=new TradeTime();
                    tradeTime.setOpentime(DateUtils.getDate(nowDate, "yyyyMMdd")+str.split("-")[0]);
                    tradeTime.setClosetime(DateUtils.getDate(nowDate, "yyyyMMdd")+str.split("-")[1]);
                    list.add(tradeTime);
                }
            }
        }
        return list;
    }

    public Boolean isInTradeTimeList(Date nowTime,String formate){

        Boolean isInTradeTime=false;
        if(null!=tradeTimeList&&tradeTimeList.size()>0){
            List<TradeTime> list=convertTradeTime(nowTime ,tradeTimeList);
            if(null!=list&&list.size()>0){
                for(TradeTime tradeTime:list){
                    if(DateUtils.isInterval(tradeTime.getOpentime(),tradeTime.getClosetime(),DateUtils.getDate(nowTime, "yyyyMMddHHmm"),formate)){
                        isInTradeTime=true;
                        break;
                    };
                }
            }
        }
        return isInTradeTime;
    }

    public TradeTime getTradeTime(Date nowTime,String formate){

        TradeTime  temp=null;
        Boolean isInTradeTime=false;
        if(null!=tradeTimeList&&tradeTimeList.size()>0){

            List<TradeTime> list=convertTradeTime(nowTime ,tradeTimeList);
            if(null!=list&&list.size()>0){
                for(TradeTime tradeTime:list){
                    if(DateUtils.isInterval(tradeTime.getOpentime(),tradeTime.getClosetime(),DateUtils.getDate(nowTime, "yyyyMMddHHmm"),formate)){
                        temp=tradeTime;
                        break;
                    };
                }
            }

        }
        return temp;
    }



   /**
    * 是否在同一个交易时间段
    *
    * */
    public Boolean isInTradeTimeCommon(Date date1,Date date2,String formate){

        Boolean isInTradeTimeCommon=false;
        TradeTime tradeTime1=getTradeTime(date1, formate);
        TradeTime tradeTime2=getTradeTime(date2, formate);
        if(null!=tradeTime1&&null!=tradeTime2) {

            if(tradeTime1.getOpentime().equalsIgnoreCase(tradeTime2.getOpentime())){
                isInTradeTimeCommon=true;
            }
        }
        return isInTradeTimeCommon;
    }

    public List<String>   getTradeTimeList(Date beginTime,Date endTime,String formate){

        Boolean isInTradeTimeCommon=isInTradeTimeCommon(beginTime,endTime,formate);
        List  listTradeTime=null;
        if(isInTradeTimeCommon){
            listTradeTime=inTradeTimeCommon(beginTime,endTime);
        }else{
            listTradeTime=notInTradeTimeCommon(beginTime, endTime,formate);
        }
        return listTradeTime;
    }
    private List<String> inTradeTimeCommon(Date begintime,Date endtime){

        List inTradeTimeCommon=new ArrayList();
        int length=0;
        length=DateUtils.getDifferMinue(begintime,endtime,"yyyyMMddHHmm");
       return  getTimeByList(begintime,length);
    }

    public List<String>  getTimeByList(Date begintime,int length){
        List inTradeTimeList=new ArrayList();
        if(length>0){
            for(int i=0;i<length;i++){

                inTradeTimeList.add(DateUtils.addMinue(begintime, 1));

            }
        }
        return  inTradeTimeList;
    }
    private List notInTradeTimeCommon(Date begintime,Date endtime,String formate){
        List notInTradeTimeCommon=new ArrayList();
        try{
            int beginLength=0;
            int endLength=0;
            TradeTime tradeTime1=getTradeTime(begintime, formate);
            TradeTime tradeTime2=getTradeTime(endtime, formate);
            if(null!=tradeTime1){
                beginLength= DateUtils.getDifferMinue(begintime,DateUtils.parse(tradeTime1.getClosetime(),"yyyyMMddHHmm"),"yyyyMMddHHmm");
                notInTradeTimeCommon.add(getTimeByList(begintime,beginLength));
            }

            if(null!=tradeTime2){
                endLength= DateUtils.getDifferMinue(DateUtils.parse(tradeTime2.getOpentime(), "yyyyMMddHHmm"),endtime,"yyyyMMddHHmm");
                notInTradeTimeCommon.add(getTimeByList(DateUtils.parse(tradeTime2.getOpentime(), "yyyyMMddHHmm"),endLength));
            }
        }catch (Exception x){
               x.printStackTrace();
        }


        return  notInTradeTimeCommon;
    }

    private void initTradeTime(String tradeTime){

        //交易时间格式：0900-1100,1300-1500
        if(!StringUtils.isBlank(tradeTime)){
            int length=0;
            String[] allArray=tradeTime.split(",");
            for(String str:allArray){
                if(str.length()==9&&str.contains("-")){
                    tradeTimeList.add(str);
                }else{
                    throw new RuntimeException("交易时间格式设置错误：正确格式为0900-1100,1300-1500当前格式为："+tradeTime);
                }
            }
        }
    }




}
