package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.entity.UserCoupon;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatusVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.vo.ShopVO;
import com.sky.webSocket.WebSocketServer;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ShopCartMapper shopCartMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private UserCouponMapper userCouponMapper;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 用户提交订单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @Transactional
    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        Long userId = BaseContext.getCurrentId();
        Long shopId = ordersSubmitDTO.getShopId();
        //查询购物车
        ShoppingCart cart = new ShoppingCart();
        cart.setUserId(userId);
        List<ShoppingCart> shoppingCartList = shopCartMapper.list(cart);
        //向订单表中插入一条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(orders.UN_PAID);
        orders.setStatus(orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setUserId(userId);
        orderMapper.insert(orders);
        //向订单明细表中插入n条数据
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart shoppingCart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetails.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetails);
        //清空当前用户购物车数据
        shopCartMapper.deleteAll(userId, shopId);
        //如果使用优惠券那么用户对应优惠券减1,后续取消订单再加回来
        Long couponId = orders.getCouponId();
        if (couponId != null) {
            Long count = couponMapper.queryCount(userId, couponId);
            if (count > 1) {
                couponMapper.updateCount(userId, couponId);
            } else {
                couponMapper.delete(userId, couponId);
            }
        }
        //封装VO返回结果
        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setId(orders.getId());
        orderSubmitVO.setOrderTime(orders.getOrderTime());
        orderSubmitVO.setOrderNumber(orders.getNumber());
        orderSubmitVO.setOrderAmount(orders.getAmount());
        return orderSubmitVO;
    }

    /**
     * 取消订单进行字段更新
     *
     * @param ordersCancelDTO
     */
    @Transactional
    @Override
    public void cancelUpdate(OrdersCancelDTO ordersCancelDTO) {
        Long userId = BaseContext.getCurrentId();
        Long orderId = ordersCancelDTO.getId();
        //先查询该取消订单是否有使用优惠券
        Orders orders = orderMapper.queryById(orderId);
        Long couponId = orders.getCouponId();
        if (couponId != null) {
            //查询user_coupon表,看是否有对应项
            Long userCouponId = couponMapper.queryUserCouponId(userId, couponId);
            if (userCouponId != null) {
                //进行更新操作
                couponMapper.addCount(userId, couponId);
            } else {
                //进行插入操作
                UserCoupon userCoupon = new UserCoupon();
                userCoupon.setUserId(userId);
                userCoupon.setCouponId(couponId);
                userCoupon.setCount(1L);
                userCouponMapper.insert(userCoupon);
            }
        }
        //设置订单取消原因及其状态
        orderMapper.cancelUpdate(ordersCancelDTO);
    }

    /**
     * 支付成功后更新订单
     *
     * @param orderId
     */
    @Override
    public void payUpdate(Long orderId) {
        Orders orders = new Orders();
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setPayStatus(1);
        orders.setId(orderId);
        orderMapper.payUpdate(orders);
        //根据orderId找到订单
        Orders order = orderMapper.queryById(orderId);
        Long shopId = order.getShopId();
        HashMap map = new HashMap();
        map.put("orderNumber", order.getNumber());
        map.put("info", "待接单");
        String message = JSON.toJSONString(map);
        webSocketServer.sendToShop(message, shopId);
    }

    /**
     * 查询单个订单状态
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderStatusVO queryStatus(Long orderId) {
        OrderStatusVO orderStatusVO = orderMapper.queryStatus(orderId);
        return orderStatusVO;
    }

    /**
     * 用户查询近期订单
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResult pageQuery(int pageNum, int pageSize) {
        // 设置分页
        PageHelper.startPage(pageNum, pageSize);
        Long userId = BaseContext.getCurrentId();
        // 分页条件查询
        Page<Orders> page = orderMapper.pageQuery(userId);
        List<OrderVO> list = new ArrayList();
        // 查询出订单明细，并封装入OrderVO进行响应
        if (page != null && page.getTotal() > 0) {
            for (Orders orders : page) {
                OrderVO orderVO = new OrderVO();
                //订单id
                Long orderId = orders.getId();
                //计算待支付订单的倒计时值
                LocalDateTime now = LocalDateTime.now();
                //获取订单时间
                LocalDateTime orderTime = orders.getOrderTime();
                // 订单时间加上5分钟
                LocalDateTime deadline = orderTime.plusMinutes(5);
                // 计算当前时间与截止时间的差异
                Duration duration = Duration.between(now, deadline);
                // 得到剩余秒数，如果在当前时间之前则为负数
                long remainingSeconds = duration.getSeconds();
                // 判断如果已经超过截止时间，则剩余时间为0
                if (remainingSeconds < 0) {
                    remainingSeconds = 0;
                }
                orderVO.setCountDown(remainingSeconds);
                // 查询订单明细
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId, 3);
                List<OrderDetail> details = orderDetailMapper.getByOrderId(orderId, null);
                int size = details.size();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);
                orderVO.setDetailCount(size);
                //查询订单菜的总数
                Long dishesNumber = orderDetailMapper.count(orders.getId());
                orderVO.setDishesNumber(dishesNumber);
                //根据店铺id查询店铺信息
                Long shopId = orders.getShopId();
                ShopVO shopVO = shopMapper.queryById(shopId);
                orderVO.setShopName(shopVO.getName());
                orderVO.setShopAddress(shopVO.getAddress());
                list.add(orderVO);
            }
        }
        return new PageResult(page.getTotal(), page.getPages(), list);
    }

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    @Override
    public OrderVO details(Long id) {
        OrderVO orderVO = new OrderVO();
        // 根据id查询订单
        Orders orders = orderMapper.queryById(id);
        BeanUtils.copyProperties(orders, orderVO);
        // 查询该订单对应的菜品明细
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId(), null);
        orderVO.setOrderDetailList(orderDetailList);
        //计算待支付订单的倒计时值
        LocalDateTime now = LocalDateTime.now();
        //获取订单时间
        LocalDateTime orderTime = orders.getOrderTime();
        // 订单时间加上5分钟
        LocalDateTime deadline = orderTime.plusMinutes(5);
        // 计算当前时间与截止时间的差异
        Duration duration = Duration.between(now, deadline);
        // 得到剩余秒数，如果在当前时间之前则为负数
        long remainingSeconds = duration.getSeconds();
        // 判断如果已经超过截止时间，则剩余时间为0
        if (remainingSeconds < 0) {
            remainingSeconds = 0;
        }
        orderVO.setCountDown(remainingSeconds);
        //查询订单菜的总数
        Long dishesNumber = orderDetailMapper.count(orders.getId());
        orderVO.setDishesNumber(dishesNumber);
        //根据店铺id查询店铺信息
        Long shopId = orders.getShopId();
        ShopVO shopVO = shopMapper.queryById(shopId);
        orderVO.setShopName(shopVO.getName());
        orderVO.setShopAddress(shopVO.getAddress());
        return orderVO;
    }

    /**
     * 商家接单
     *
     * @param orderId
     */
    @Override
    public void receiveOrder(Long orderId) {
        int status = 3;
        Long pickupCode = orderId;
        orderMapper.updateOrderStatus(orderId, status, pickupCode);
        webSocketServer.sendToUser("更新", orderId);
    }

    /**
     * 商家完成订单
     *
     * @param orderId
     */
    @Override
    public void completeOrder(Long orderId) {
        int status = 4;
        orderMapper.updateOrderStatus(orderId, status, null);
        webSocketServer.sendToUser("更新", orderId);
    }

    /**
     * 商家退款
     *
     * @param orderId
     */
    @Transactional
    @Override
    public void rejectOrder(Long orderId, String rejectReason) {
        //先查询该退款订单是否有使用优惠券
        Orders orders = orderMapper.queryById(orderId);
        Long couponId = orders.getCouponId();
        Long userId = orders.getUserId();
        if (couponId != null) {
            //查询user_coupon表,看是否有对应项
            Long userCouponId = couponMapper.queryUserCouponId(userId, couponId);
            if (userCouponId != null) {
                //进行更新操作
                couponMapper.addCount(userId, couponId);
            } else {
                //进行插入操作
                UserCoupon userCoupon = new UserCoupon();
                userCoupon.setUserId(userId);
                userCoupon.setCouponId(couponId);
                userCoupon.setCount(1L);
                userCouponMapper.insert(userCoupon);
            }
        }
        //更新订单状态
        orderMapper.refundOrder(orderId, rejectReason, 6, 2);
    }

    /**
     * 订单搜索
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        Page<Orders> page = orderMapper.adminNewPageQuery(ordersPageQueryDTO);

        // 部分订单状态，需要额外返回订单菜品信息，将Orders转化为OrderVO
        List<OrderVO> list = new ArrayList();
        // 查询出订单明细，并封装入OrderVO进行响应
        if (page != null && page.getTotal() > 0) {
            for (Orders order : page) {
                // 使用 StringBuilder 来构建orderDishes
                StringBuilder orderDishes = new StringBuilder();
                OrderVO orderVO = new OrderVO();
                //根据支付方式int来设置字符串
                if (order.getPayMethod() == 1) {
                    orderVO.setPayMethodStr("微信支付");
                } else {
                    orderVO.setPayMethodStr("现金支付");
                }
                //订单id
                Long orderId = order.getId();
                //查询用户名
                String name = userMapper.getNameById(order.getUserId());
                if (name == null) {
                    orderVO.setName("前台点餐");
                } else {
                    orderVO.setName(name);
                }
                //计算待支付订单的倒计时值
                LocalDateTime now = LocalDateTime.now();
                //获取订单时间
                LocalDateTime orderTime = order.getOrderTime();
                // 订单时间加上5分钟
                LocalDateTime deadline = orderTime.plusMinutes(5);
                // 计算当前时间与截止时间的差异
                Duration duration = Duration.between(now, deadline);
                // 得到剩余秒数，如果在当前时间之前则为负数
                long remainingSeconds = duration.getSeconds();
                // 判断如果已经超过截止时间，则剩余时间为0
                if (remainingSeconds < 0) {
                    remainingSeconds = 0;
                }
                orderVO.setCountDown(remainingSeconds);
                // 查询订单明细
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId, 3);
                List<OrderDetail> details = orderDetailMapper.getByOrderId(orderId, null);
                //组装orderDishes字符串
                for (OrderDetail detail : details) {
                    String orderDish = String.format("%s(%s)x%d",
                            detail.getName(),
                            detail.getDishFlavor(),
                            detail.getNumber()
                    );
                    // 将字符串追加到 StringBuilder 中
                    if (orderDishes.length() > 0) {
                        orderDishes.append("; ");
                    }
                    orderDishes.append(orderDish);
                }
                orderVO.setOrderDishes(orderDishes.toString());
                int size = details.size();
                BeanUtils.copyProperties(order, orderVO);
                orderVO.setOrderDetailList(orderDetails);
                orderVO.setDetailCount(size);
                //查询订单菜的总数
                Long dishesNumber = orderDetailMapper.count(order.getId());
                orderVO.setDishesNumber(dishesNumber);
                list.add(orderVO);
            }
        }
        return new PageResult(page.getTotal(), page.getPages(), list);
    }

    @Override
    public OrderSubmitVO submitOrder_machine(OrdersSubmitDTO ordersSubmitDTO) {
        Long userId = BaseContext.getCurrentId();
        Long shopId = ordersSubmitDTO.getShopId();
        //查询购物车
        ShoppingCart cart = new ShoppingCart();
        cart.setUserId(userId);
        List<ShoppingCart> shoppingCartList = shopCartMapper.list(cart);
        //向订单表中插入一条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(orders.PAID);
        orders.setStatus(orders.MAKING);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setUserId(userId);
        orderMapper.insert(orders);
        //插入取餐码
        orderMapper.updateCode(orders.getId());
        //向订单明细表中插入n条数据
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart shoppingCart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetails.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetails);
        //清空当前用户购物车数据
        shopCartMapper.deleteAll(userId, shopId);
        //封装VO返回结果
        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setId(orders.getId());
        orderSubmitVO.setOrderTime(orders.getOrderTime());
        orderSubmitVO.setOrderNumber(orders.getNumber());
        orderSubmitVO.setOrderAmount(orders.getAmount());
        orderSubmitVO.setPickupCode(orders.getId());
        return orderSubmitVO;
    }

    private List<OrderVO> getOrderVOList(Page<Orders> page) {
        // 需要返回订单菜品信息，自定义OrderVO响应结果
        List<OrderVO> orderVOList = new ArrayList<>();

        List<Orders> ordersList = page.getResult();
        if (!CollectionUtils.isEmpty(ordersList)) {
            for (Orders orders : ordersList) {
                // 将共同字段复制到OrderVO
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                String orderDishes = getOrderDishesStr(orders);
                // 将订单菜品信息封装到orderVO中，并添加到orderVOList
                orderVO.setOrderDishes(orderDishes);
                orderVOList.add(orderVO);
            }
        }
        return orderVOList;
    }

    /**
     * 根据订单id获取菜品信息字符串
     *
     * @param orders
     * @return
     */
    private String getOrderDishesStr(Orders orders) {
        // 查询订单菜品详情信息（订单中的菜品和数量）
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId(), null);

        // 将每一条订单菜品信息拼接为字符串（格式：宫保鸡丁*3；）
        List<String> orderDishList = orderDetailList.stream().map(x -> {
            String orderDish = x.getName() + "*" + x.getNumber() + ";";
            return orderDish;
        }).collect(Collectors.toList());

        // 将该订单对应的所有菜品信息拼接在一起
        return String.join("", orderDishList);
    }
}
