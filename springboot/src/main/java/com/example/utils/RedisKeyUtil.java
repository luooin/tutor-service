package com.example.utils;

public class RedisKeyUtil {

    private static final String SPLIT = ":";
//    private static final String PREFIX_ENTITY_LIKE = "like:entity";
//    private static final String PREFIX_USER_LIKE = "like:entity";
//    private static final String PREFIX_FOLLOWEE = "followee";
//    private static final String PREFIX_FOLLOWER = "follower";
//    private static final String PREFIX_TICKET = "ticket";
//    private static final String PREFIX_KAPTCHA = "kaptcha";
    private static final String PREFIX_USER_LIKE = "user";
    private static final String PREFIX_READ_COUNT = "read";


    public static String getReadCountKey(int id) {
        return PREFIX_READ_COUNT + SPLIT + id;
    }

    // 某个实体的赞
    // like:entity:entityType:entityId -> set(userId)方便知道谁给我点赞了，适应各种需求变化，体现拥抱需求的思想
//    public static String getEntityLikeKey(int entityType, int entityId) {
//        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
//    }

    // 某个用户的赞
    // like:user:userType:userId -> int
//    public static String getUserLikeKey(int userId) {
//        return PREFIX_USER_LIKE + SPLIT + userId;
//    }

    //某个实体拥有的粉丝
    //followee:entityType:entityId -> zset(userId,now) 以时间排序
//    public static String getFollowerKey(int entityType,int entityId) {
//        return PREFIX_FOLLOWER + SPLIT +entityType + SPLIT + entityId ;
//    }


    //某个用户关注的实体
    //followee:userId:entityType -> zset(entityId,now) 以时间排序
//    public static String getFolloweeKey(int userId,int entityType) {
//        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT +entityType;
//    }
//
//    //登录验证码
//    public static String getKaptchaKey(String owner){
//        return PREFIX_KAPTCHA + SPLIT + owner;
//    }
//
//    //登录凭证
//    public static String getTicketKey(String ticket){
//        return PREFIX_TICKET + SPLIT + ticket;
//    }
//
//    //用户
//    public static String getUserKey(int userId){
//        return PREFIX_USER + SPLIT + userId;
//    }
}
