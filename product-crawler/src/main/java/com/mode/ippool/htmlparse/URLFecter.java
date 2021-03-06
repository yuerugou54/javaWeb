package com.mode.ippool.htmlparse;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mode.ippool.httpbowser.MyHttpResponse;
import com.mode.ippool.ipmodel.IPMessage;

public class URLFecter {
    // 使用代理进行爬取
    public static boolean urlParseNN(String url, String ip, String port,
            List<IPMessage> ipMessages1) {
        // 调用一个类使其返回html源码
        String html = MyHttpResponse.getHtml(url, ip, port);

        if (html != null) {
            // 将html解析成DOM结构
            Document document = Jsoup.parse(html);

            // 提取所需要的数据
            Elements trs = document.select("table[id=ip_list]").select("tbody").select("tr");

            for (int i = 1; i < trs.size(); i++) {
                IPMessage ipMessage = new IPMessage();
                String ipAddress = trs.get(i).select("td").get(1).text();
                String ipPort = trs.get(i).select("td").get(2).text();
                String ipType = trs.get(i).select("td").get(5).text();
                String ipSpeed = trs.get(i).select("td").get(6).select("div[class=bar]")
                        .attr("title");

                ipMessage.setIPAddress(ipAddress);
                ipMessage.setIPPort(Integer.valueOf(ipPort));
                ipMessage.setIPType(ipType);
                ipMessage.setIPSpeed(ipSpeed);

                ipMessages1.add(ipMessage);
            }

            return true;
        } else {
            out.println(ip + ": " + port + " 代理不可用");

            return false;
        }
    }

    // 使用本机IP爬取xici代理网站的第一页
    public static List<IPMessage> urlParseNN(List<IPMessage> ipMessagesList, String xcUrl) {
        // String url = "http://www.xicidaili.com/wn/1";
        String html = MyHttpResponse.getHtml(xcUrl);

        // 将html解析成DOM结构
        Document document = Jsoup.parse(html);

        // 提取所需要的数据
        Elements trs = document.select("table[id=ip_list]").select("tbody").select("tr");
        // TODO 也可以设置判断第一个list值与爬取出来的第一个值对比，如果不一致就更新list
        for (int i = 1; i < trs.size(); i++) {
            IPMessage ipMessage = new IPMessage();
            String ipAddress = trs.get(i).select("td").get(1).text();
            String ipPort = trs.get(i).select("td").get(2).text();
            String ipType = trs.get(i).select("td").get(5).text();
            String ipSpeed = trs.get(i).select("td").get(6).select("div[class=bar]").attr("title");

            ipMessage.setIPAddress(ipAddress);
            ipMessage.setIPPort(Integer.valueOf(ipPort));
            ipMessage.setIPType(ipType);
            ipMessage.setIPSpeed(ipSpeed);

            ipMessagesList.add(ipMessage);
        }
        return ipMessagesList;
    }

    public static void main(String[] args) {
        List<IPMessage> ipMessages = new ArrayList<>();

        URLFecter.urlParseNN(ipMessages, "http://www.xicidaili.com/nn/1");
        for (IPMessage ipMessage : ipMessages) {
            System.out.println(ipMessage.getIPAddress());
        }

    }
}
