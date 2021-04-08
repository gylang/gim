package com.gylang.gim.api.domain.common;

/**
 * @author gylang
 * data 2021/3/6
 */
public class ResponseMessage extends MessageWrap {

    public static ResponseMessage copy(MessageWrap messageWrap) {
        ResponseMessage copyObj = new ResponseMessage();
        copyObj.setCmd(messageWrap.getCmd());
        copyObj.setSender(messageWrap.getSender());
        copyObj.setType(messageWrap.getType());
        copyObj.setContent(messageWrap.getContent());
        copyObj.setContentType(messageWrap.getContentType());
        copyObj.setCode(messageWrap.getCode());
        copyObj.setReceive(messageWrap.getReceive());
        copyObj.setReceiverType(messageWrap.getReceiverType());
        copyObj.setClientMsgId(messageWrap.getClientMsgId());
        copyObj.setMsgId(messageWrap.getMsgId());
        copyObj.setRetryNum(messageWrap.getRetryNum());
        copyObj.setQos(messageWrap.isQos());
        copyObj.setStore(messageWrap.isStore());
        copyObj.setOfflineMsgEvent(messageWrap.isOfflineMsgEvent());
        copyObj.setTimeStamp(messageWrap.getTimeStamp());
        return copyObj;
    }
}
