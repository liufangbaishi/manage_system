package com.cheng.manage.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

import static com.google.code.kaptcha.Constants.*;

/**
 * 生成验证码
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name KaptchaConfig
 * @date 2021/8/1 19:14
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        properties.put(KAPTCHA_BORDER, "yes");
        properties.put(KAPTCHA_BACKGROUND_CLR_FROM, "100,100,100");
        properties.put(KAPTCHA_BACKGROUND_CLR_TO, "248,248,248");
        properties.put(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "WHITE");
        properties.put(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        properties.put(KAPTCHA_IMAGE_HEIGHT, "40");
        properties.put(KAPTCHA_IMAGE_WIDTH, "120");
        properties.put(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "30");
        // 验证码文本字体样式 默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");
        // 图片样式 水纹com.google.code.kaptcha.impl.WaterRipple 鱼眼com.google.code.kaptcha.impl.FishEyeGimpy 阴影com.google.code.kaptcha.impl.ShadowGimpy
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");

        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
