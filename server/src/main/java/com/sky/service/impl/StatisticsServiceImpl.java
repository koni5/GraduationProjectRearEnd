package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.OverviewService;
import com.sky.service.StatisticsService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OverviewService overviewService;

    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        ArrayList<LocalDate> dateArrayList = new ArrayList<>();
        dateArrayList.add(begin);
        while (!begin.equals(end) && end.isAfter(begin)) {
            begin = begin.plusDays(1L);
            dateArrayList.add(begin);
        }
        ArrayList<Double> turnoverList = new ArrayList<>();
        for (LocalDate localDate : dateArrayList) {
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            HashMap<String, Object> map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.DONE);
            Double turnover = orderMapper.sumByMap(map);
            //double如果没查到返回是null,list返回是空而不是null
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }
        TurnoverReportVO turnoverReportVO = TurnoverReportVO.builder().dateList(StringUtils.join(dateArrayList, ",")).turnoverList(StringUtils.join(turnoverList, ",")).build();
        return turnoverReportVO;
    }

    /**
     * 订单统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        ArrayList<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end) && end.isAfter(begin)) {
            begin = begin.plusDays(1L);
            dateList.add(begin);
        }
        //存放每天的订单总数
        List<Integer> orderCountList = new ArrayList<>();
        //存放每天的有效订单数
        List<Integer> validCountList = new ArrayList<>();
        //遍历dateList集合,查询每天的有效订单数和订单总数
        for (LocalDate date : dateList) {
            //查询每天的订单总数
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Integer orderCount = getOrderCount(beginTime, endTime, null);
            //查询每天的有效订单数
            Integer validOrderCount = getOrderCount(beginTime, endTime, Orders.DONE);

            orderCountList.add(orderCount);
            validCountList.add(validOrderCount);
        }
        //计算范围内的订单总数
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        //计算范围内的有效订单数量
        Integer vaildOrderCount = validCountList.stream().reduce(Integer::sum).get();
        //计算订单完成率
        Double orderCompletionRate = totalOrderCount == 0 ? 0.0 : vaildOrderCount.doubleValue() / totalOrderCount;
        OrderReportVO orderReportVO = OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(vaildOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
        return orderReportVO;
    }

    /**
     * 导出店铺营业数据报表
     *
     * @param response
     */
    @Override
    public void exportExcel(HttpServletResponse response) {
        //查询数据库,获取营业数据(最近30天)
        LocalDate dateBegin = LocalDate.now().minusDays(30);
        LocalDate dateEnd = LocalDate.now().minusDays(1);
        //查询概览数据
        BusinessDataVO businessDataVO = overviewService.getBusinessData(LocalDateTime.of(dateBegin, LocalTime.MIN), LocalDateTime.of(dateEnd, LocalTime.MAX));
        //通过POI将数据写入到Excel文件中
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/operationTemplate.xlsx");
        try {
            //基于excel模板文件生成一个新的excel
            XSSFWorkbook excel = new XSSFWorkbook(inputStream);
            //获取第二行第二格填充时间数据
            XSSFSheet sheet = excel.getSheet("Sheet1");
            XSSFRow row2 = sheet.getRow(1);
            XSSFCell cell = row2.getCell(1);
            cell.setCellValue("时间:" + dateBegin + "至" + dateEnd);
            //填充其余总览数据
            XSSFRow row4 = sheet.getRow(3);
            row4.getCell(2).setCellValue(businessDataVO.getTurnover());
            row4.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            XSSFRow row5 = sheet.getRow(4);
            row5.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            row5.getCell(4).setCellValue(businessDataVO.getUnitPrice());
            //填充明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = dateBegin.plusDays(i);
                BusinessDataVO businessData = overviewService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
                XSSFRow row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
            }
            //通过输出流将Excel文件下载到客户端浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            excel.write(outputStream);
            //关闭资源
            outputStream.close();
            excel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Integer getOrderCount(LocalDateTime begin, LocalDateTime end, Integer status) {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        map.put("status", status);
        return orderMapper.countByMap(map);
    }
}
