package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.StatisticsService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/admin/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("营业额统计:{},{}", begin, end);
        return Result.success(statisticsService.getTurnoverStatistics(begin, end));
    }

    /**
     * 订单统计
     *
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> ordersStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("订单数据统计:{},{}", begin, end);
        return Result.success(statisticsService.getOrderStatistics(begin, end));
    }

    /**
     * 导出店铺营业数据报表
     *
     * @param response
     */
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response) {
        statisticsService.exportExcel(response);
    }
}
