package com.lyh.onlineShop.cartAndOrder.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyh.onlineShop.cartAndOrder.dto.OrderAddDto;
import com.lyh.onlineShop.cartAndOrder.dto.OrderCancelDto;
import com.lyh.onlineShop.cartAndOrder.dto.OrderPageAdminDto;
import com.lyh.onlineShop.cartAndOrder.dto.OrderPageUserDto;
import com.lyh.onlineShop.cartAndOrder.feign.AddressFeignClient;
import com.lyh.onlineShop.cartAndOrder.feign.ProductFeignClient;
import com.lyh.onlineShop.cartAndOrder.mapper.OrderDetailMapper;
import com.lyh.onlineShop.cartAndOrder.mapper.OrderMapper;
import com.lyh.onlineShop.cartAndOrder.service.CartService;
import com.lyh.onlineShop.cartAndOrder.service.OrderService;
import com.lyh.onlineShop.cartAndOrder.utils.OrderNumUtil;
import com.lyh.onlineShop.cartAndOrder.vo.CartVo;
import com.lyh.onlineShop.cartAndOrder.vo.OrderDetailVo;
import com.lyh.onlineShop.cartAndOrder.vo.OrderVo;
import com.lyh.onlineShop.common.entity.*;
import com.lyh.onlineShop.common.enumeration.ExceptionEnum;
import com.lyh.onlineShop.common.enumeration.OrderStatusEnum;
import com.lyh.onlineShop.common.enumeration.PayMethodEnum;
import com.lyh.onlineShop.common.exception.BaseException;
import com.lyh.onlineShop.common.utils.Result;
import com.lyh.onlineShop.constant.FlagConstant;
import com.lyh.onlineShop.constant.OrderByConstant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lyh
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private CartService cartService;

    @Autowired
    private AddressFeignClient  addressFeignClient; // 用于调用地址簿服务

    private final OrderNumUtil orderNumUtil;
    // 使用构造函数注入
    @Autowired
    public OrderServiceImpl(OrderNumUtil orderNumUtil) {
        this.orderNumUtil = orderNumUtil;
    }

    @Autowired
    private ProductFeignClient productFeignClient;

    /**
     *  分页查询订单信息——管理端
     * @param orderPageAdminDto
     * @return
     */
    @Override
    public PageInfo pageQueryForAdmin(OrderPageAdminDto orderPageAdminDto) {
        PageHelper.startPage(orderPageAdminDto.getPageNum(), orderPageAdminDto.getPageSize(), OrderByConstant.ORDER_BY_CREATE_TIME_DESC);
        // 先查询出订单的基本信息
        List<Order> orderList = orderMapper.selectByCondition(orderPageAdminDto);

        // 封装为vo
        List<OrderVo> orderVoList = new ArrayList<>();
        for(Order order : orderList) {
            OrderVo orderVo = orderToOrderVo(order); // 将order转为orderVo
            orderVoList.add(orderVo);
        }

        // 封装为分页对象返回
        return new PageInfo(orderVoList);
    }

    /**
     *  发货——管理端
     * @param orderNum
     */
    @Override
    public void deliveryOrder(String orderNum) {
        Order order = orderMapper.selectByOrderNum(orderNum);
        if(order == null) {
            throw new BaseException(ExceptionEnum.ORDER_NOT_EXIST);
        }

        // 只有订单状态为已付款的订单才能发货
        if(!order.getStatus().equals(OrderStatusEnum.ORDER_STATUS_PAID.getCode())) {
            throw new BaseException(ExceptionEnum.ORDER_STATUS_ERROR);
        }

        // 更新订单状态为已发货
        order.setStatus(OrderStatusEnum.ORDER_STATUS_DELIVERED.getCode());
        // todo 使用AOP等实现时间字段自动填充
        order.setDeliveryTime(LocalDateTime.now());

        int updateResult = orderMapper.updateOrderStatus(order);
        if(updateResult == 0) {
            throw new BaseException(ExceptionEnum.DB_UPDATE_FAILED);
        }
    }

    /**
     * 创建订单——用户端
     *
     * @param orderAddDto
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class) // todo 使用 Seata 实现分布式事务
    @Override
    public String addOrder(OrderAddDto orderAddDto, Integer userId) {
        // 1. 获取用户的购物车信息
        // 查询用户购物车中的添加的所有商品
        List<CartVo> cartVoList = cartService.getCart(userId);
        if(CollectionUtils.isEmpty(cartVoList)) {
            throw new BaseException(ExceptionEnum.CART_IS_EMPTY);
        }
        // 查询被勾选的商品（筛选出被勾选的商品）
        List<CartVo> selectedCartVoList = cartVoList.stream().filter(cartVo -> cartVo.getSelected().equals(FlagConstant.FLAG_TRUE)).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(selectedCartVoList)) { // 判断购物车中是否有商品被勾选
            throw new BaseException(ExceptionEnum.CART_NOT_PRODUCT_SELECTED);
        }

        // 2.根据地址簿id查询用户选择的收货地址详情信息
        Result<AddressBook> addressResult = addressFeignClient.getAddressBook(orderAddDto.getAddressBookId());
        isRPCSuccess(addressResult);
        // 获取AddressBook对象
        AddressBook addressBook = addressResult.getData();

        // 3.生成订单号
        String orderNum = orderNumUtil.generateOrderNum();

        List<OrderDetail> orderDetailList = new ArrayList<>(); // 定义用于记录订单详情的集合
        for(CartVo cartVo : selectedCartVoList) {
            // 4.查询商品信息，检查商品是否在售、库存是否充足
            Result<Product> productResult = productFeignClient.getProductDetail(cartVo.getProductId());
            isRPCSuccess(productResult); // 判断RPC调用是否成功
            Product product = productResult.getData();
            checkProduct(product, cartVo.getQuantity()); // 检查商品是否在售、库存是否充足

            // 5.更新商品表中对应商品的库存（扣库存）
            product.setStock(product.getStock() - cartVo.getQuantity());
            productFeignClient.updateProduct(product);

            // 6.生成对应的订单详情，加入记录订单详情的集合orderDetailList中
            OrderDetail orderDetail = OrderDetail.builder()
                    .orderNum(orderNum)
                    .productId(product.getId())
                    .productName(product.getName())
                    .productImage(product.getImage())
                    .unitPrice(product.getPrice())
                    .quantity(cartVo.getQuantity())
                    .totalPrice(cartVo.getTotalPrice())
                    .build();
            orderDetailList.add(orderDetail); // 加入订单详情集合

            // 7.删除购物车中已下单的商品（删除该条购物车信息）
            cartService.deleteCart(cartVo.getProductId(), userId);
        }

        // 8.生成订单（向订单表中添加订单信息）
        Order order = Order.builder()
                .orderNum(orderNum)
                .userId(userId)
                .totalPrice(orderDetailList.stream().map(OrderDetail::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add))
                .consignee(addressBook.getConsignee())
                .phone(addressBook.getPhone())
                .address(addressBook.getAddress())
                .build();
        // 拷贝其他属性
        BeanUtils.copyProperties(orderAddDto, order);
        orderMapper.addOrder(order); // 添加订单信息

        // 9.批量添加订单详情
        orderDetailMapper.batchAddOrderDetail(orderDetailList);

        // 返回订单编号
        return orderNum;
    }

    /**
     *  获取订单详情——用户端
     * @param orderNum
     * @param userId
     * @return
     */
    @Override
    public OrderVo getOrderDetailForUser(String orderNum, Integer userId) {
        // 检查订单是否存在，以及是否属于当前用户
        Order order = checkOrder(orderNum, userId);

        // 获取订单详情
        List<OrderDetailVo> orderDetailVoList = orderDetailMapper.selectByOrderNum(orderNum);

        // 封装为Vo
        OrderVo orderVo = OrderVo.builder()
                .orderDetailVoList(orderDetailVoList)
                .orderStatusStr(OrderStatusEnum.codeOf(order.getStatus()).getValue()) // 映射订单状态为对应的str
//                .payMethodStr(PayMethodEnum.codeOf(order.getPayMethod()).getValue()) // 映射支付方式为对应的str
                .build();
        // 设置其他属性
        if(order.getPayMethod() !=  null) { // 若订单已支付，则映射支付方式为对应的str
            orderVo.setPayMethodStr(PayMethodEnum.codeOf(order.getPayMethod()).getValue());
        }
        BeanUtils.copyProperties(order, orderVo);
        return orderVo;
    }

    /**
     * 取消订单——用户端
     * @param orderCancelDto
     * @param userId
     */
    @Override
    public void cancelOrder(OrderCancelDto orderCancelDto, Integer userId) {
        // 检查订单是否存在，以及是否属于当前用户
        Order order = checkOrder(orderCancelDto.getOrderNum(), userId);

        // todo 做一个超时未支付自动取消的功能
        // 已取消的订单不能再次取消
        if(order.getStatus().equals(OrderStatusEnum.ORDER_STATUS_CANCELLED.getCode())) {
            throw new BaseException(ExceptionEnum.ORDER_STATUS_ERROR);
        }

        // 更新订单状态为已取消
        order.setStatus(OrderStatusEnum.ORDER_STATUS_CANCELLED.getCode());
        order.setCancelReason(orderCancelDto.getCancelReason());
        // todo 使用AOP等实现时间字段自动填充
        order.setCancelTime(LocalDateTime.now());
        int updateResult = orderMapper.updateOrderStatus(order);
        if(updateResult == 0) {
            throw new BaseException(ExceptionEnum.DB_UPDATE_FAILED);
        }
    }

    /**
     *  获取订单支付二维码——用户端
     * @param orderNum
     * @return
     */
    @Override
    public String getQrCode(String orderNum) {
        // todo
        // todo 获取订单支付二维码
        return "";
    }

    /**
     * 支付订单——用户端
     * @param orderNum
     * @param userId
     */
    @Override
    public void payOrder(String orderNum, Integer userId) {
        // 检查订单是否存在，以及是否属于当前用户
        Order order = checkOrder(orderNum, userId);

        // 订单状态为待支付的订单才能支付
        if(!order.getStatus().equals(OrderStatusEnum.ORDER_STATUS_UNPAID.getCode())) {
            throw new BaseException(ExceptionEnum.ORDER_STATUS_ERROR);
        }

        // 更新订单状态为已支付
        order.setStatus(OrderStatusEnum.ORDER_STATUS_PAID.getCode());
        // todo 先默认支付方式都为支付宝支付
        order.setPayMethod(PayMethodEnum.PAY_METHOD_ALIPAY.getCode());
        // todo 使用AOP等实现时间字段自动填充
        order.setPayTime(LocalDateTime.now());
        int updateResult = orderMapper.updateOrderStatus(order);
        if(updateResult == 0) {
            throw new BaseException(ExceptionEnum.DB_UPDATE_FAILED);
        }
    }

    /**
     * 完成订单（确认收货）——用户端
     * @param orderNum
     * @param userId
     */
    @Override
    public void completeOrder(String orderNum, Integer userId) {
        // 检查订单是否存在，以及是否属于当前用户
        Order order = checkOrder(orderNum, userId);

        // 订单状态为待支付的订单才能支付
        if(!order.getStatus().equals(OrderStatusEnum.ORDER_STATUS_DELIVERED.getCode())) {
            throw new BaseException(ExceptionEnum.ORDER_STATUS_ERROR);
        }

        // 更新订单状态为已完成
        order.setStatus(OrderStatusEnum.ORDER_STATUS_COMPLETED.getCode());
        // todo 使用AOP等实现时间字段自动填充
        order.setCompleteTime(LocalDateTime.now());
        int updateResult = orderMapper.updateOrderStatus(order);
        if(updateResult == 0) {
            throw new BaseException(ExceptionEnum.DB_UPDATE_FAILED);
        }
    }

    /**
     *  分页查询订单——用户端
     * @param orderPageUserDto
     * @param userId
     * @return
     */
    @Override
    public PageInfo pageQueryForUser(OrderPageUserDto orderPageUserDto, Integer userId) {

        // todo
        // todo
        // todo
        PageHelper.startPage(orderPageUserDto.getPageNum(), orderPageUserDto.getPageSize(), OrderByConstant.ORDER_BY_CREATE_TIME_DESC);
        // 先查询出订单的基本信息
        List<Order> orderList = orderMapper.selectByConditionForUser(orderPageUserDto, userId);

        // 封装为vo
        List<OrderVo> orderVoList = new ArrayList<>();
        for(Order order : orderList) {
            OrderVo orderVo = orderToOrderVo(order); // 将order转为orderVo
            orderVoList.add(orderVo);
        }

        // 封装为分页对象返回
        return new PageInfo(orderVoList);
    }

    /*
        将order对象转换为orderVo对象
     */
    private OrderVo orderToOrderVo(Order order) {
        OrderVo orderVo = new OrderVo();
        // 拷贝基本属性
        BeanUtils.copyProperties(order, orderVo);
        // 将状态和支付方式映射为对应的str
        // 设置订单状态str
        orderVo.setOrderStatusStr(OrderStatusEnum.codeOf(order.getStatus()).getValue());
        // 设置订单支付方式str（如果订单已支付）
        if(order.getPayMethod() != null) {
            orderVo.setPayMethodStr(PayMethodEnum.codeOf(order.getPayMethod()).getValue());
        }

        // 查询订单详情表，设置订单详情
        List<OrderDetailVo> detailVoList = orderDetailMapper.selectByOrderNum(order.getOrderNum());
        orderVo.setOrderDetailVoList(detailVoList);
        return orderVo;
    }

    /*
        检查远程调用是否成功
     */
    private void isRPCSuccess(Result result) {
        if(result == null || !result.getCode().equals(Result.SUCCESS_CODE)) {
            throw new BaseException(ExceptionEnum.REMOTE_CALL_ERROR);
        }
    }

    /*
        检查商品是否存在/是否在售/库存是否足够
     */
    private void checkProduct(Product product, Integer quantity) {
        if(product == null) {
            throw new BaseException(ExceptionEnum.PRODUCT_NOT_EXIST);
        }

        if(product.getStatus().equals(FlagConstant.FLAG_FALSE)) {
            throw new BaseException(ExceptionEnum.PRODUCT_NOT_AVAILABLE);
        }

        if(product.getStock() < quantity) {
            throw new BaseException(ExceptionEnum.PRODUCT_NOT_ENOUGH);
        }
    }

    /*
        检查订单是否存在，以及是否是当前用户的订单
     */
    private Order checkOrder(String orderNum, Integer userId) {
        Order order = orderMapper.selectByOrderNum(orderNum);
        if(order == null) {
            throw new BaseException(ExceptionEnum.ORDER_NOT_EXIST);
        }

        if(!order.getUserId().equals(userId)) {
            throw new BaseException(ExceptionEnum.ORDER_NOT_BELONG_TO_USER);
        }

        return order;
    }
}
