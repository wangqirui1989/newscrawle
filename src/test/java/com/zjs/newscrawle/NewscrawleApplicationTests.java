package com.zjs.newscrawle;

import com.zjs.newscrawle.service.NewsCrawlerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NewscrawleApplicationTests {

    @Resource
    private NewsCrawlerService newsCrawlerService;

    @Test
    public void contextLoads() throws Exception {

    }

}
