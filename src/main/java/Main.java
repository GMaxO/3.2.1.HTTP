import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;

public class Main {
    public static final String REMOTE_SERVICE_URI = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()  // Создаем HTTP клиента
                .setDefaultRequestConfig(RequestConfig.custom() // метод библиотеки apache
                        .setConnectTimeout(5000)    // время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // время ожидания получения данных
                        .setRedirectsEnabled(false) // отключаем переарисацию
                        .build())
                .build();

        HttpGet request = new HttpGet(REMOTE_SERVICE_URI); // создание запроса
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        CloseableHttpResponse response = httpClient.execute(request); // отправка запроса

//        Arrays.stream(response.getAllHeaders())
//        .forEach(System.out::println); // вывод заголовков
        // чтение тела ответа
//        String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
//        System.out.println(body); // вывод

        //Преобразование json в java объекты
        List<Massage> posts = mapper.readValue(response.getEntity().getContent(), new TypeReference<>() {});
        posts.stream()                                                   // создаем stream
                .filter(s -> s.getUpvotes() > 0)                         // промежуточная операция
                .forEach(System.out::println);                           // завершающая операция
    }
}
