package com.huaqing.samplerecord.utlis;

import com.elvishew.xlog.LogItem;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestEncryptInterceptor implements Interceptor {
    private static final String FORM_NAME = "content";
    private static final String CHARSET = "UTF-8";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //获取请求体
        RequestBody body = request.body();
        if (body instanceof FormBody) {
            FormBody formBody = (FormBody) body;
            Map<String, String> formMap = new HashMap<>();
            // 从 formBody 中拿到请求参数，放入 formMap 中
            for (int i = 0; i < formBody.size(); i++) {
                formMap.put(formBody.name(i), formBody.value(i));
            }
            // 将 formMap通过Gson.toJson() 转化为 json 然后 AES 加密
            Gson gson = new Gson();
            String jsonParams = gson.toJson(formMap);
            byte[] bytes = jsonParams.getBytes(CHARSET);
            String str = new String(bytes,"UTF-8");
            byte[] encryptParams = AESTool.encrypt(str, "key");
            // 重新修改 body 的内容
            body = new FormBody.Builder().add(FORM_NAME, new String(encryptParams,"UTF-8")).build();
        }
        // 若请求体不为Null，重新构建post请求，并传入修改后的参数体
        if (body != null) {
            request = request.newBuilder().post(body).build();
        }
        return chain.proceed(request);
    }

}
