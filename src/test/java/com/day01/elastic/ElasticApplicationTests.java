package com.day01.elastic;

import com.day01.elastic.bean.Article;
import com.day01.elastic.bean.Book;
import com.day01.elastic.repository.BookRepository;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticApplicationTests {
    @Autowired
    JestClient jestClient;
    @Autowired
    BookRepository bookRepository;

    @Test
    public void test01(){
        Book book = new Book();
        book.setId(1);
        book.setTitle("西游记");
        book.setAuthor("吴承恩");
        bookRepository.index(book);
    }



    @Test
    public void contextLoads() {
        //给es创建一个索引
        Article article = new Article();
        article.setId(1);
        article.setTitle("hello world");
        article.setAuthor("zhangsan");
        article.setContent("你好");

        Index index = new Index.Builder(article).index("article").type("message").build();
        try {
            jestClient.execute(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void search(){
        String json="{\"query\":{\"match\":{\"content\":\"你好\"}}}";
        Search search = new Search.Builder(json).addIndex("article").addType("message").build();
        try {
            SearchResult result = jestClient.execute(search);
            System.out.println(result.getJsonString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
