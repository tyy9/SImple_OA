package com.myoa.my_oa.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PageUtils {
    public static Page getPage(int pageNo, int limit, List list){
        Page page=new Page();
        int size=list.size();
        if(limit> size){
            limit= size;
        }
        //求出最大页数,防止pageNo越界
        int maxpage=size%limit==0?size/limit:size/limit+1;
        if(pageNo>maxpage){
            pageNo=maxpage;
        }
        //当前页的第一个数据下标
        log.info("pageno=>"+pageNo);
        int current=pageNo>1?(pageNo-1)*limit:0;
        log.info("current=>"+current);
        List pagelist=new ArrayList<>();
        for(int i=0;i<limit&&current+i<size;i++){
            pagelist.add(list.get(current + i));
        }
        return page.setRecords(pagelist).setTotal(size).setCurrent(pageNo).setSize(limit);
    }
}
