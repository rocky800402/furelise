package com.furelise.ord.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class OrdDTO {
	
	//結帳
	private String pName;
	private byte[] pImage1;
	private Integer quantity;
	private Integer memID;
	private BigDecimal pPrice;
	private Integer payment;
	private Timestamp ordDate;
	private Integer deliver;
	private String address;
	private String cityCode;
	private BigDecimal sum;
	private BigDecimal shipping;
	private BigDecimal total;
	private String coupon;
	
	//查看訂單
	private Integer ordID;
	private Integer pID;
	private Date deliverDate;
	private Integer ordStatus;
	private Date arrival;
	
}