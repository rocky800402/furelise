package com.furelise.shopcart.model2;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import redis.clients.jedis.Jedis;

@Service
public class ShopCartService {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	// 將商品新增至購物車
	public void addProduct(Integer memID, Integer pID, Integer quantity) {
		try {

			String key = "memCart:" + Integer.toString(memID);
			// 使用 HashOperations 進行操作
			HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
			hashOps.put(key, Integer.toString(pID), Integer.toString(quantity));

		} catch (Exception e) {
			e.printStackTrace();
		}
	} // addProduct

	// 修改購物車內特定商品的數量
	public void updateQuantity(Integer memID, Integer pID, Integer quantity) {
		try {
			String key = "memCart:" + Integer.toString(memID);

			// 使用 HashOperations 進行操作
			HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();

			// 從 Hash 中獲取序列化的值
			String serializedItem = hashOps.get(key, Integer.toString(pID));

			// 反序列化為對象
			ShopCart theCart = deserialize(serializedItem);

			// 更新數量
			theCart.setPID(pID);
			theCart.setQuantity(quantity);

			// 將更新後的對象再次序列化並存入Redis
			hashOps.put(key, Integer.toString(pID), Integer.toString(quantity));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 移除購物車內的特定商品
	public void removeProduct(Integer memID, Integer pID) {
		try {

			String key = "memCart:" + Integer.toString(memID);
			// 使用 HashOperations 進行操作
			HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();

			// 從 Hash 中刪除指定字段
			hashOps.delete(key, Integer.toString(pID));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// removeProduct

	// 結帳後清空會員的整台購物車
	public void clearCart(Integer memID) {
		try {
			// 組合購物車的 key
			String cartKey = "memCart:" + memID;

			// 刪除購物車的 key
			redisTemplate.delete(cartKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 取得該會員購物車內的所有商品
	public Map<String, String> getShopCartProducts(Integer memID) {
		try {
			String key = "memCart:" + Integer.toString(memID);
			// 使用 HashOperations 進行操作
			HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();

			// 印出來看
			System.out.println(hashOps.entries(key));// null

			// 從 Redis 中取得該會員購物車內的所有商品
			return hashOps.entries(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	} // getShopCartProducts


	// 原版取得購物車內特定商品的數量
	public String getOneProduct(Integer memID, Integer pID) {
		try {
			String key = "memCart:" + Integer.toString(memID);

			// 使用 HashOperations 進行操作
			HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
			
			// 印出來看
			System.out.println(hashOps.get(key, Integer.toString(pID)));// null
			
			// 從 Redis 中獲取特定商品的數量
			return hashOps.get(key, Integer.toString(pID));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	} // getOneProduct

	private ShopCart deserialize(String serializedItem) {
		Gson gson = new Gson();
		try {
			return gson.fromJson(serializedItem, ShopCart.class);
		} catch (Exception e) {
			// 如果出現 JSON 解析錯誤，你可以在這裡採取一些措施，例如紀錄錯誤、返回默認值等

			// 在這裡修復把serializedItem回傳的數字，包裝成一個 JSON 字串
			if (serializedItem.matches("-?\\d+")) {

				return gson.fromJson("{\"quantity\":" + serializedItem + "}", ShopCart.class);
			}
			e.printStackTrace();
			return null;
		}
	} // deserialize
	


}// ShopCartService
