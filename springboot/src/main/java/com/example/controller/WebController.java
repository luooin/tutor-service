package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import com.example.common.config.CaptureConfig;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.service.AdminService;
import com.example.service.TeacherService;
import com.example.service.UserService;
import com.google.code.kaptcha.Producer;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 基础前端接口
 */
@RestController
public class WebController {

    @Resource
    private AdminService adminService;

    @Autowired
    private Producer kaptchaProducer;

    @Resource
    private TeacherService teacherService;
    @Resource
    private UserService userService;

    @GetMapping("/")
    public Result hello() {
        return Result.success("访问成功");
    }


    @RequestMapping("/ver")
    public void captcha( HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 指定验证码的长宽以及字符的个数
        SpecCaptcha captcha = new SpecCaptcha(135, 33, 5);
        captcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);
        // 首先把验证码在后台保存一份，但是不能保存在session，可以存在redis，也可以存在后台的某个Map里面
//        CaptureConfig.CAPTURE_MAP.put(key, captcha.text().toLowerCase());
        CaptchaUtil.out(captcha, request, response);
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        if (ObjectUtil.isEmpty(account.getUsername()) || ObjectUtil.isEmpty(account.getPassword())
                || ObjectUtil.isEmpty(account.getRole())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        // 判断验证码对不对
//        if (!account.getVerCode().toLowerCase().equals(CaptureConfig.CAPTURE_MAP.get(key))) {
//            // 如果不相等，说明验证不通过
//            CaptchaUtil.clear(request);
//            return Result.error(ResultCodeEnum.VER_CODE_ERROR);
//        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            account = adminService.login(account);
        }
        if (RoleEnum.TEACHER.name().equals(account.getRole())) {
            account = teacherService.login(account);
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            account = userService.login(account);
        }
        return Result.success(account);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getRole())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.register(account);
        }
        if (RoleEnum.TEACHER.name().equals(account.getRole())) {
            teacherService.register(account);
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.register(account);
        }
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getNewPassword())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.updatePassword(account);
        }
        if (RoleEnum.TEACHER.name().equals(account.getRole())) {
            teacherService.updatePassword(account);
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.updatePassword(account);
        }
        return Result.success();
    }

//    //图片比较特别返回值为void 用Response返回
//    @RequestMapping(path="/kaptcha",method = RequestMethod.GET)
//    public void getKaptcha(HttpServletResponse response, HttpSession session){
//        //生成验证码
//        String text = kaptchaProducer.createText();
//        BufferedImage image = kaptchaProducer.createImage(text);
//
//        //将验证码存入session
//        //session.setAttribute("kaptcha",text);
//        //重构验证码的归属,随机生成的uuid为凭证
//        String kaptchaOwner = CommunityUtil.generateUUID();
//        Cookie cookie = new Cookie("kaptchaOwner",kaptchaOwner);
//        cookie.setMaxAge(60);
//        cookie.setPath(contextPath);//整个项目下都有效
//        response.addCookie(cookie);
//
//        //将验证码存入redis
//        String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
//        redisTemplate.opsForValue().set(redisKey,text,60, TimeUnit.SECONDS);
//
//        //将图片输出给浏览器
//        response.setContentType("image/png");
//        try {
//            ServletOutputStream os = response.getOutputStream();
//            ImageIO.write(image,"png",os);
//        } catch (IOException e) {
//            logger.error("响应验证码失败："+e.getMessage());
//        }
//    }

}
