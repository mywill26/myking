package com.mycx26.base.process.config;

import com.mycx26.base.service.impl.JdbcServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author mycx26
 * @date 2022/3/23
 */
@MapperScan({"com.mycx26.base.process.mapper"})
@Import({JdbcServiceImpl.class})
@ComponentScan(basePackages = {"com.mycx26.base.process"})
@Configuration
public class MykingConfig {
}
