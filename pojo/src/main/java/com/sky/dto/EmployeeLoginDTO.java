package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeeLoginDTO implements Serializable {
    //用户名
    private String username;
    //密码
    private String password;

}
