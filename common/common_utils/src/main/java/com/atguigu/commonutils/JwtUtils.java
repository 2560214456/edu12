package com.atguigu.commonutils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * jwt
 * @author helen
 * @since 2019/10/16
 */
public class JwtUtils {
    //token的过期时间 1天
    public static final long EXPIRE = 1000 * 60 * 60 * 24;
    //秘钥，加密和编码   秘钥随便写的
    public static final String APP_SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO";

    /**
     * 生成token字符串
     * @param id 用户id
     * @param nickname 用户name
     * @return
     */
    public static String getJwtToken(String id, String nickname){
        //Jwts.builder 构建jwt字符串
        String JwtToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256") // 设置jwt的头信息

                .setSubject("guli-user") // 分类，随便起的
                .setIssuedAt(new Date()) //得到当前时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE)) //如果超过EXPIRE 就过期了

                .claim("id", id)
                .claim("nickname", nickname) // 设置token的主体部分，可以添加多个

                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact(); // 签名哈希

        return JwtToken;
    }

    /**
     * 判断token是否存在与有效
     * @param jwtToken token令牌
     * @return
     */
    public static boolean checkToken(String jwtToken) {
        if(StringUtils.isEmpty(jwtToken)) return false; // 然后token 为空返回false
        try {
            // 根据秘钥来解析，能解析就是有效的
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断token是否存在与有效
     * @param request request一个请求
     * @return
     */
    public static boolean checkToken(HttpServletRequest request) {
        try {
            //获取请求头中的token
            String jwtToken = request.getHeader("token");
            //判断是否为空
            if(StringUtils.isEmpty(jwtToken)) return false;
            // 通过秘钥解码（能解码就是有效的token）
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据token获取会员id
     * @param request
     * @return
     */
    public static String getMemberIdByJwtToken(HttpServletRequest request) {
        // 在请求头中获取token
        String jwtToken = request.getHeader("token");
        // token为空返回 空字符串
        if(StringUtils.isEmpty(jwtToken)) return "";
        // 通过秘钥解密token
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        // 获取字符串主体部分
        Claims claims = claimsJws.getBody();
        return (String)claims.get("id");
    }

    /**
     * 根据token获取用户id
     * @param token
     * @return
     */
    public static String getUserInfo(String token){
        if (StringUtils.isEmpty(token))
            return "";
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return (String) body.get("id");
    }
}
