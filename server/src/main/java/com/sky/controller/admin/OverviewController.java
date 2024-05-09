package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.OverviewService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.OrderOverViewVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 首页今日数据总览
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/overview")
@Slf4j
public class OverviewController {

    @Autowired
    private OverviewService overviewService;

    /**
     * dashBoard今日数据查询
     *
     * @return
     */
    @GetMapping("/businessData")
    public Result<BusinessDataVO> businessData() {
        //获得当天的开始时间
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        //获得当天的结束时间
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
        BusinessDataVO businessDataVO = overviewService.getBusinessData(begin, end);
        return Result.success(businessDataVO);
    }

}
