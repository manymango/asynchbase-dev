package org.hbase.async.tsdb;


import com.stumbleupon.async.Deferred;
import org.hbase.async.*;
import org.junit.Assert;
import org.junit.Before;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class Test {
    private HBaseClient client;

    @Before
    public void init() throws IOException {
        final String zkQuorum = "izuf6goyl5x6opx4hvd8l5z";
        Config config = new Config();
        config.overrideConfig("hbase.zookeeper.quorum", zkQuorum);
        config.overrideConfig("hbase.zookeeper.znode.parent","/hbase");
        this.client = new HBaseClient(config);
    }

    @org.junit.Test
    public void test() throws Exception {
        byte[] bytes = {0};
        final GetRequest  getRequest = new GetRequest("tsdb-uid".getBytes(), bytes);
        getRequest.family("id").qualifier("metrics");
        Deferred o = client.get(getRequest).addCallback(s -> {
            System.out.println("haha");
            System.out.println(s);
           return s.get(0).value();
        });
        o.joinUninterruptibly();
    }


    @org.junit.Test
    public void atomicIncrement() {
        client.atomicIncrement(new AtomicIncrementRequest("tsdb-uid","\\x00","id","metrics"));
    }

    @org.junit.Test
    public void testMap(){
        Map<String, String> map = new HashMap<>();
        String a = map.put("1", "abc");
        Assert.assertNull(a);
        String b = map.put("1", "def");
        Assert.assertEquals("abc", b);
    }

}
