package com.cheng.manage.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.cheng.manage.common.consts.AuthConsts;
import com.cheng.manage.common.consts.Result;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name KaptchaController
 * @date 2022/5/29 17:13
 */
@RestController
@RequestMapping("auth")
@Slf4j
public class KaptchaController {

    @Autowired
    private Producer producer;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @ApiOperation(value = "获取验证码")
    @RequestMapping(value = "getCaptcha", method = RequestMethod.GET)
    public Result getCaptcha() {
        Result result;
        try {
            // 需要生产一个uuid和验证码，一同返回给前端；登录时通过uuid判断验证码
            String uuid = UUID.randomUUID().toString();
            // 生成验证码文字
            String code = producer.createText();
            log.info("生成的验证码{}", code);
            // 生成图片验证码转换为Base64
            BufferedImage imageCode = producer.createImage(code);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(imageCode, "jpg", outputStream);
            // 写入redis中 120秒
            redisTemplate.opsForValue().set(AuthConsts.CAPTCHA_KEY+uuid, code, 120, TimeUnit.SECONDS);
            result = Result.success(
                    MapUtil.builder()
                            .put("key", uuid)
                            .put("code", Base64.encode(outputStream.toByteArray()))
                            .build()
            );
        } catch (Exception e) {
            result = Result.fail("生成验证码失败");
        }
        return result;
    }
}
