package com.project.air_quality;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

// código copiado de:
// https://gitlab.com/ico_gl/ua_tqs_gs20/-/blob/master/gs-employee-mngr/src/test/java/tqsdemo/employeemngr/employee/JsonUtil.java

class JsonUtil {
    static byte[] toJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}

