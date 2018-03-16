package com.knoldus.twitterproducer.impl.twitter;

import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterConfiguration {

    private static ConfigurationBuilder cb = new ConfigurationBuilder();
    private final static Configuration conf;

    static {
        conf = cb.setDebugEnabled(true)
                .setOAuthConsumerKey(System.getenv("TWITTER_CONSUMER_KEY"))
                .setOAuthConsumerSecret(System.getenv("TWITTER_CONSUMER_SECRET"))
                .setOAuthAccessToken(System.getenv("TWITTER_ACCESS_TOKEN"))
                .setOAuthAccessTokenSecret(System.getenv("TWITTER_ACCESS_TOKEN_SECRET"))
                .build();
    }

    public static Configuration getTwitterConf() {
        return conf;
    }

    public static TwitterFactory twitterFactory = new TwitterFactory(getTwitterConf());

}
