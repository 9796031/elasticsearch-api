package com.home.doc;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.bean.User;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.Id;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DocumentTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 添加文档
     * @throws IOException
     */
    @Test
    public void testAdd() throws IOException {
        User user = new User();
        user.setAge(11);
        user.setName("张三");
        List<String> tags = new ArrayList<String>();
        tags.add("javaScript");
        tags.add("mysql");
        user.setTags(tags);
        IndexRequest request = new IndexRequest("user");

        request.id("1");
        request.source(JSON.toJSONString(user), XContentType.JSON);
        IndexResponse index = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(index));
        // 查询是否插入成功
        assert index.status() == RestStatus.CREATED;
    }

    /**
     * 查看文档是否存在
     * @throws IOException
     */
    @Test
    public void testExist() throws IOException {
        GetRequest request = new GetRequest("user");
        request.id("1");
        // 不获取上下文
        request.fetchSourceContext(new FetchSourceContext(false));
        boolean exists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
        System.out.println("文档是否存在: " + exists);
    }

    @Test
    public void testGet() throws IOException {
        GetRequest request = new GetRequest("user");
        request.id("1");
        GetResponse documentFields = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        System.out.println(documentFields.getSource());
    }

    @Test
    public void testUpdate() throws IOException {
        UpdateRequest request = new UpdateRequest("user", "1");

        User user = new User();
        user.setName("李四");
        request.doc(JSON.toJSONString(user), XContentType.JSON);
        UpdateResponse update = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        System.out.println(update.status());
    }

    @Test
    public void testDelete() throws IOException {
        DeleteRequest request = new DeleteRequest("user");
        request.id("1");
        DeleteResponse delete = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        System.out.println(delete.status());
    }

    @Test
    public void testBulkPut() throws IOException {
        BulkRequest request = new BulkRequest("user");
        List<User> list = new ArrayList<>();
        User user1 = new User(2, "张三", 20, Arrays.asList("hadoop", "javaScript"));
        User user2 = new User(2, "王五", 20, Arrays.asList("elasticsearch", "python"));
        User user3 = new User(2, "赵柳", 20, Arrays.asList("mysql", "spring"));
        list.add(user1);
        list.add(user2);
        list.add(user3);
        list.forEach(user -> {
            request.add(new IndexRequest("user").id(user.getId() + "").source(JSON.toJSONString(user), XContentType.JSON));
        });
        BulkResponse bulk = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        System.out.println(bulk.hasFailures());
        System.out.println(bulk.buildFailureMessage());
    }

    @Test
    public void testSearch() throws IOException {
        SearchRequest request = new SearchRequest("user");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("name", "赵柳"));
        request.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        hits.forEach(h -> {
            System.out.println(JSON.toJSONString(h));
        });
    }
}
