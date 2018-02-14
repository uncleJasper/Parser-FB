package ru.uncleJasper.demologer;

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args){

        final String LOGINURL = "https://www.facebook.com/login/";
        final String GROUPURL = "https://www.facebook.com/groups/270527916322520/about/";
        final String USERAGENT = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36";
        final String EMAIL = "kostik.dulin@bk.ru";
        final String PASS = "LhKxL9Rm8a";
        final int COUNT = 0;

        try {
            //получаем страницу входа
            Connection connection1 = HttpConnection.connect(LOGINURL)
                    .ignoreHttpErrors(true)
                    .userAgent(USERAGENT);

            Connection.Response response1 = connection1.execute();

            //получаем авторизованную страницу
            Connection connection2 = connection1.url(LOGINURL)
                    .cookies(response1.cookies())       //обязательно нужно переносить куки из запроса в запрос!
                    .ignoreHttpErrors(true)
                    .data( "email", EMAIL)
                    .data( "pass", PASS)
                    .method(Connection.Method.POST)
                    .followRedirects(true);

            Connection.Response response2 = connection2.execute();
            //Document doc = response2.parse();

            //переходим на страницу группы
            Connection connection3 = connection2.url(GROUPURL)
                    .cookies(response2.cookies())
                    .ignoreHttpErrors(true);

            Connection.Response response3 = connection3.execute();
            //найдём кол-во народу в группе
            String count = response3.parse().html();
            int i = count.indexOf("<span class=\"_2iem _50f7\">Участники ");
            count = count.substring(i, i + 68);
            count = count.replace("<span class=\"_2iem _50f7\">Участники ", "");
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(count);
            String finCount = "";

            while (matcher.find()){
                finCount += count.substring(matcher.start(),matcher.end());
            }

            System.out.println("Кол-во участников группы: " + finCount);

        } catch (NullPointerException e){
            System.err.print("Ошибка! NullPointerException \n");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
