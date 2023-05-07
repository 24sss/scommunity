package com.scommunity.entity;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/7 9:43
 */
/**
 * 封装分页的信息
* */
public class Page {
    //当前页码(页面传入，服务器接收)
    private int current = 1;
    //每一页显示上限(页面传入，服务器接收)
    private int limit = 10;
    //数据总数（服务端查出来的，用于计算总的页数）
    private int rows;
    //查询路径（服务端设置，页面点击 1 2 3 首页 末页 的时候）
    //复用分页的链接
    private String path;

    public int getCurrent() {
            return current;

    }

    public void setCurrent(int current) {
        //页面传过来的页码，防止是负数和0
        if(current>=1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        //对每页显示的数量有限制
        if(limit>=1&&limit<=100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        //数据总数
        if(rows>=0){
            this.rows = rows;
        }

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    //获取当前页的起始行
    public int getOffset(){
        return current*limit-limit;
    }

    //获取总的页数
    public int getTotal(){
        //rows/limit[+1]
        if(rows%limit==0){
            return rows/limit;
        }else {
            return rows/limit+1;
        }
    }

    //从第几页显示到第几页
    public int getFrom(){
       int from = current-2;
       return from<1?1:from;
    }

    //从第几页显示到第几页
    public int getTo(){
        int to = current+2;
        int total = getTotal();
        return to>total?total:to;

    }
}

