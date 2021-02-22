package com.example.myapplication.okhttp;

import com.example.myapplication.MyApplication;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

public class LoginLogic extends BaseLogic {

    private static LoginLogic _Instance = null;

    public static LoginLogic Instance() {
        if (_Instance == null)
            _Instance = new LoginLogic();
        return _Instance;
    }
    private LoginLogic() {
        this.context = MyApplication.getAppContext();//防止了内存泄漏
    }

    /**
     * get请求测试
     */
    public String getJsonApi(StringCallback callback)
            throws Exception {
        String result = "";
        OkHttpUtils
                .get()
                .url(getSpcyUrl(ServerApi.GET_URL))
                .id(100)
                .tag(context)
                .build()
                .execute(callback);
        return result;
    }

}
