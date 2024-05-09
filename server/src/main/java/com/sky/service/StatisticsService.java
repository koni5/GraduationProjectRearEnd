package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public interface StatisticsService {
    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /**
     * 导出店铺营业数据报表
     * @param response
     */
    void exportExcel(HttpServletResponse response);
}
