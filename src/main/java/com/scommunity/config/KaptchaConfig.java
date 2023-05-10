package com.scommunity.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/10 12:44
 */
//生成验证码的配置类
@Configuration
public class KaptchaConfig {
    /**
     声明一个Bean，Bean将被Spring容器管理装配
    装配的肯定是核心的对象，这个核心的对象是接口Producer
    接口中两个方法：创建验证码、创建图片
     这个接口有个实现类：DefaultKaptcha
    */
    @Bean
    public Producer kaptchaProducer(){
        //作为Config的参数
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width","100");
        properties.setProperty("kaptcha.image.height","40");
        properties.setProperty("kaptcha.textproducer.font.size","32");
        properties.setProperty("kaptcha.textproducer.font.color","0,0,0");
        properties.setProperty("kaptcha.textproducer.char.string","0123456789QWERTYUIOPASDFGHJKLZXCVBNM");
        properties.setProperty("kaptcha.textproducer.char.length","4");
        properties.setProperty("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");


        /**
        实例化的是它的实现类
        */
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        /**
         *我们需要给DefaultKaptcha传入参数，我们把参数放在config对象里
         * Config的参数是Properties对象
         */
        Config config = new Config(properties);
        /**
         *把config  set给Kaptcha，并返回
         */
        kaptcha.setConfig(config);
        return  kaptcha;



    }


}
