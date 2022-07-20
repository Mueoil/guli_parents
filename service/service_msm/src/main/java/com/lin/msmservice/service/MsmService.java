package com.lin.msmservice.service;

import java.util.Map;

public interface MsmService {
    boolean send(Map<String, Object> params, String phone);

    boolean sendEmail(String code, String email);
}
