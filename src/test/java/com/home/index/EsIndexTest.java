package com.home.index;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author liqingdong
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EsIndexTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 创建索引
     *
     * @throws IOException
     */
    @Test
    public void testCreate() throws IOException {
        CreateIndexRequest idx = new CreateIndexRequest("user");
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(idx, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);
    }

    /**
     * 查看索引是否存在
     *
     * @throws IOException
     */
    @Test
    public void testIndexExist() throws IOException {
        GetIndexRequest idx = new GetIndexRequest("user");
        boolean flag = restHighLevelClient.indices().exists(idx, RequestOptions.DEFAULT);
        System.out.println(flag);
    }

    /**
     * 删除索引
     *
     * @throws IOException
     */
    @Test
    public void testIndexDelete() throws IOException {
        DeleteIndexRequest idx = new DeleteIndexRequest("user");
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(idx, RequestOptions.DEFAULT);
        System.out.println(delete);
    }
}
