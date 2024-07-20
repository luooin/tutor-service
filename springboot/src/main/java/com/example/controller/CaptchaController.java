package com.example.controller;

import com.example.common.config.CaptureConfig;
import com.example.utils.CommunityUtil;
import com.google.code.kaptcha.Producer;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@CrossOrigin
@RestController
@RequestMapping
public class CaptchaController {

    private static final Logger logger =  LoggerFactory.getLogger(CaptchaController.class);

    @Autowired
    private Producer kaptchaProducer;
    @Autowired
    private RedisTemplate redisTemplate;



    //图片比较特别返回值为void 用Response返回
    @RequestMapping(path="/kaptcha",method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        //生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        //将验证码存入session
        //session.setAttribute("kaptcha",text);
        //重构验证码的归属,随机生成的uuid为凭证
        String kaptchaOwner = CommunityUtil.generateUUID();
        Cookie cookie = new Cookie("kaptchaOwner",kaptchaOwner);
        cookie.setMaxAge(60);
        cookie.setPath("http://localhost:8080");//整个项目下都有效
        response.addCookie(cookie);

        //将验证码存入redis
//        String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
//        redisTemplate.opsForValue().set(redisKey,text,60, TimeUnit.SECONDS);

        //将图片输出给浏览器
        response.setContentType("image/png");
        try {
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
            logger.error("响应验证码失败："+e.getMessage());
        }
    }

    @RequestMapping("/captcha1")
    public void captcha( HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 指定验证码的长宽以及字符的个数
        SpecCaptcha captcha = new SpecCaptcha(135, 33, 5);
        captcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);
        // 首先把验证码在后台保存一份，但是不能保存在session，可以存在redis，也可以存在后台的某个Map里面
//        CaptureConfig.CAPTURE_MAP.put(key, captcha.text().toLowerCase());
        CaptchaUtil.out(captcha, request, response);
    }
        // 算术类型
//        ArithmeticCaptcha captcha = new ArithmeticCaptcha(135, 33);
//        captcha.setLen(4);  // 几位数运算，默认是两位
//        captcha.getArithmeticString();  // 获取运算的公式：3+2=?
//        captcha.text();  // 获取运算的结果：5
//        CaptureConfig.CAPTURE_MAP.put(key, captcha.text().toLowerCase());
//        CaptchaUtil.out(captcha, request, response);


}