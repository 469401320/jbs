package me.hao0.jbs.server;

import me.hao0.jbs.server.cluster.server.ServerCluster;
import me.hao0.jbs.server.cluster.server.ServerRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {
    "me.hao0.jbs.store",
    "me.hao0.jbs.server"
})
public class Bootstrap {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(Bootstrap.class, args);


        ServerRegister serverRegister = context.getBean(ServerRegister.class);
        serverRegister.start();


        ServerCluster serverCluster = context.getBean(ServerCluster.class);
        serverCluster.start();

    }
}
