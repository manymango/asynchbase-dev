package org.hbase.async.tsdb;


import com.stumbleupon.async.Deferred;
import org.hbase.async.*;
import org.junit.Before;

import java.io.IOException;
import java.util.ArrayList;

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
        Deferred<Object> deferred = client.ensureTableExists("tsdb").addCallback(s->{
            System.out.println(s);
            return null;
        });
        deferred.joinUninterruptibly();
        System.out.println("xixi");
    }


    @org.junit.Test
    public void atomicIncrement() {
        client.atomicIncrement(new AtomicIncrementRequest("tsdb-uid","\\x00","id","metrics"));
    }

}
