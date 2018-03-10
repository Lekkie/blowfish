package com.avantir.blowfish.config;

import com.lambdaworks.redis.ReadFrom;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.codec.Utf8StringCodec;
import com.lambdaworks.redis.masterslave.MasterSlave;
import com.lambdaworks.redis.masterslave.StatefulRedisMasterSlaveConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheManagerProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.*;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lekanomotayo on 06/02/2018.
 */

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

    String[] cacheStrings = new String[]{"default", "terminalparameter", "terminalparameters",
            "endpoint", "endpoints", "acquirer", "acquirers", "key", "keys",
            "acquirerterminalparameter", "acquirerterminalparameters", "merchantterminalparameter",
            "merchantterminalparameters", "terminalterminalparameter", "terminalterminalparameters",
            "merchant", "merchants", "merchantTerminal", "merchantTerminals"};

    //@Value("${blowfish.redis.defaultexpiration}")
    private int redisExpiration = 300;
    @Value("${spring.redis.host}")
    private String redisHost = "127.0.0.1";
    @Value("${spring.redis.port}")
    private int redisPort = 6379;
    @Value("${spring.redis.cluster-mode}")
    private boolean clusterMode = false;

    //@Value("${spring.redis.master-host}")
    //private String masterHost = "redis1-001.4un0o7.0001.euw2.cache.amazonaws.com";
    //@Value("${spring.redis.master-port}")
    //private int masterPort = 6379;

    //@Value("${spring.redis.sentinel-host1}")
    //private String sentinelHost1 = "redis1-002.4un0o7.0001.euw2.cache.amazonaws.com";
    //@Value("${spring.redis.sentinel-port1}")
    //private int sentinelPort1 = 6379;
    //@Value("${spring.redis.sentinel-host2}")
    //private String sentinelHost2 = "redis1-003.4un0o7.0001.euw2.cache.amazonaws.com";
    //@Value("${spring.redis.sentinel-port2}")
    //private int sentinelPort2 = 6379;


    @Resource
    ConfigurableEnvironment environment;



    @Bean
    public PropertySource propertySource() {
        MutablePropertySources mutablePropertySources = environment.getPropertySources();
        List<PropertySource> propertySourceList = new ArrayList<>();
        for(PropertySource propertySource : mutablePropertySources){
            String propertyName = propertySource.getName();
            if(propertyName.contains("application.yml"))
                propertySourceList.add(propertySource);
            System.out.println(propertyName);
        }

        PropertySource propertySource = propertySourceList.get(0);
        if(propertySourceList.size() > 1) {
            for (PropertySource propertySourceTmp : propertySourceList) {
                if (!propertySourceTmp.getName().contains("applicationConfig: [classpath:/application.yml]"))
                    propertySource = propertySourceTmp;
            }
        }

        System.out.println("Selected propertySource file: " + propertySource.getName());
        //MapPropertySource propertiesPropertySource =(MapPropertySource) mutablePropertySources.get("applicationConfig: [classpath:/application.yml]");
        return propertySource;
    }



    @Bean
    public RedisSentinelConfiguration sentinelConfiguration() {

        //LettuceClientConfiguration
        PropertySource propertySource = propertySource();
        return new RedisSentinelConfiguration(propertySource);
    }


    @Bean
    public RedisClusterConfiguration clusterConfiguration() {
        //RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration().master(masterHost)
        //        .sentinel("127.0.0.1", 26379)
        //        .sentinel("127.0.0.1", 26380);
        PropertySource propertySource = propertySource();
        return new RedisClusterConfiguration(propertySource);
    }


    /*
    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();

        // Defaults
        redisConnectionFactory.setHostName(redisHost);
        redisConnectionFactory.setPort(redisPort);
        return redisConnectionFactory;
    }
    */

    @Bean
    public RedisConnectionFactory connectionFactory() {

        if(clusterMode) {

            /*
            RedisClient redisClient = RedisClient.create();
            List<RedisURI> nodes = Arrays.asList(RedisURI.create("redis://host1"),
                    RedisURI.create("redis://host2"),
                    RedisURI.create("redis://host3"));
            StatefulRedisMasterSlaveConnection<String, String> connection = MasterSlave
                    .connect(redisClient, new Utf8StringCodec(), nodes);
            connection.setReadFrom(ReadFrom.MASTER_PREFERRED);
            */

            return new LettuceConnectionFactory(clusterConfiguration());
        }

        // For Amazon cloud, point to master only, amazon will failover for you
        //https://stackoverflow.com/questions/41048313/redis-client-lettuce-master-slave-configuration-for-aws-elasticache
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(cf);
        return redisTemplate;
    }


    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = getRedisCacheManager(redisTemplate);
        cacheManager.setTransactionAware(true);
        // manually call initialize the caches as our SimpleCacheManager is not declared as a bean
        cacheManager.initializeCaches();
        return new TransactionAwareCacheManagerProxy(cacheManager);
    }



    @Bean
    public KeyGenerator keyGenerator() {

    //return new DefaultKeyGenerator();
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                // This will generate a unique key of the class name, the method name,
                // and all method parameters appended.
                StringBuilder sb = new StringBuilder();
                sb.append(o.getClass().getName());
                sb.append(method.getName());
                for (Object obj : objects) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }


    private RedisCacheManager getRedisCacheManager(RedisTemplate redisTemplate){
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        List<String> cacheNameList = Arrays.asList(cacheStrings);
        /*
        List<String> cacheNameList = new ArrayList<String>();
        cacheNameList.add("default");
        cacheNameList.add("termParam");
        cacheNameList.add("termParams");
        cacheNameList.add("endpoint");
        cacheNameList.add("endpoints");
        cacheNameList.add("acquirer");
        cacheNameList.add("acquirers");
        cacheNameList.add("key");
        cacheNameList.add("keys");
        cacheNameList.add("acqTermParam");
        cacheNameList.add("acqTermParams");
        cacheNameList.add("merchTermParam");
        cacheNameList.add("merchTermParams");
        cacheNameList.add("termTermParam");
        cacheNameList.add("termTermParams");
        cacheNameList.add("merchant");
        cacheNameList.add("merchants");
        cacheNameList.add("terminal");
        cacheNameList.add("terminals");
        cacheNameList.add("bin");
        cacheNameList.add("bins");
        cacheNameList.add("domain");
        cacheNameList.add("domains");
        cacheNameList.add("trantype");
        cacheNameList.add("trantypes");
        cacheManager.setCacheNames(cacheNameList);
        */
        return cacheManager;
    }


    private SimpleCacheManager getSimpleCacheManager(){
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        ConcurrentMapCache defaultCache = new ConcurrentMapCache("default");
        ConcurrentMapCache termParamCache = new ConcurrentMapCache("terminalparameter");
        ConcurrentMapCache termParamsCache = new ConcurrentMapCache("terminalparameters");
        ConcurrentMapCache endpointCache = new ConcurrentMapCache("endpoint");
        ConcurrentMapCache endpointsCache = new ConcurrentMapCache("endpoints");
        ConcurrentMapCache acquirerCache = new ConcurrentMapCache("acquirer");
        ConcurrentMapCache acquirersCache = new ConcurrentMapCache("acquirers");
        ConcurrentMapCache keyCache = new ConcurrentMapCache("key");
        ConcurrentMapCache keysCache = new ConcurrentMapCache("keys");
        ConcurrentMapCache acqTermParamCache = new ConcurrentMapCache("acquirerterminalparameter");
        ConcurrentMapCache acqTermParamsCache = new ConcurrentMapCache("acquirerterminalparameters");
        ConcurrentMapCache mercTermParamCache = new ConcurrentMapCache("merchantterminalparameter");
        ConcurrentMapCache mercTermParamsCache = new ConcurrentMapCache("merchantterminalparameters");
        ConcurrentMapCache termTermParamCache = new ConcurrentMapCache("terminalterminalparameter");
        ConcurrentMapCache termTermParamsCache = new ConcurrentMapCache("terminalterminalparameters");
        ConcurrentMapCache merchantCache = new ConcurrentMapCache("merchant");
        ConcurrentMapCache merchantsCache = new ConcurrentMapCache("merchants");

        List<ConcurrentMapCache> concurrentMapCacheList = new ArrayList<ConcurrentMapCache>();
        concurrentMapCacheList.add(defaultCache);
        concurrentMapCacheList.add(termParamCache);
        concurrentMapCacheList.add(termParamsCache);
        concurrentMapCacheList.add(endpointCache);
        concurrentMapCacheList.add(endpointsCache);
        concurrentMapCacheList.add(acquirerCache);
        concurrentMapCacheList.add(acquirersCache);
        concurrentMapCacheList.add(keyCache);
        concurrentMapCacheList.add(keysCache);
        concurrentMapCacheList.add(acqTermParamCache);
        concurrentMapCacheList.add(acqTermParamsCache);
        concurrentMapCacheList.add(mercTermParamCache);
        concurrentMapCacheList.add(mercTermParamsCache);
        concurrentMapCacheList.add(termTermParamCache);
        concurrentMapCacheList.add(termTermParamsCache);
        concurrentMapCacheList.add(merchantCache);
        concurrentMapCacheList.add(merchantsCache);

        //cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("default")));
        cacheManager.setCaches(concurrentMapCacheList);
        return cacheManager;
    }
     /*
    @Bean
    public CacheManager cacheManager() {
        GuavaCacheManager cacheManager = new GuavaCacheManager();
        return cacheManager;
    }
    */

}
