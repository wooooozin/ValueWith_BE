package com.valuewith.tweaver;

import com.valuewith.tweaver.config.AppPropertiesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(
		exclude = {
				org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
				org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration.class,
				org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration.class
		}
)
@EnableJpaAuditing
@EnableFeignClients
@EnableScheduling
@EnableConfigurationProperties({AppPropertiesConfig.class})
public class TweaverApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweaverApplication.class, args);
	}

}
