package com.group3sc2.cyactivity.constant;
import com.group3sc2.cyactivity.domain.ApiInfo;

public class SecurityConstant {

    public static final long EXPIRATION_TIME = 432_000_000; //5 days expressed in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String GET_ARRAYS_LLC = "Cy Activity 3";
    public static final String GET_ARRAYS_ADMINISTRATION = "Share your activity with other for fun";
    public static final String AUTHORITIES = "Authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";

    public static final String[] PUBLIC_URLS = {
            ApiInfo.API_V1+"/user/signin/",
            ApiInfo.API_V1+"/user/signup/",
            ApiInfo.API_V1+"/user/resetpassword/**",
            ApiInfo.API_V1+"/user/image/**",
            "/swagger-resources/**",
            "/swagger-ui/**"
    };
}
