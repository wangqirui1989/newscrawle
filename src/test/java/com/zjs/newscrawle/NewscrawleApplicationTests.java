package com.zjs.newscrawle;

import com.zjs.newscrawle.pojo.Page;
import com.zjs.newscrawle.pojo.TwoTuple;
import com.zjs.newscrawle.service.NewsCrawlerService;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NewscrawleApplicationTests {

    @Resource
    private NewsCrawlerService newsCrawlerService;

    @Test
    public void contextLoads() throws Exception {
        long startTime = System.currentTimeMillis();
        newsCrawlerService.setLinks();
        TwoTuple<Set<Element>> twoTuple = newsCrawlerService.generateHeadingAndDetails();
        printResult(newsCrawlerService.detailHandler(twoTuple.getSecondIndex()));
        long endTime = System.currentTimeMillis();

        System.out.println("TIME CONSUMING: " + (endTime - startTime) + "ms");

    }

    private void printResult(List<Page> resultList) {
        Iterator<Page> iterator = resultList.iterator();
        while (iterator.hasNext()) {
            Page page = iterator.next();
            System.out.println(page.toString());
        }
        System.out.println(resultList.size());
    }

}
