package com.furelise.pickupway.model;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furelise.planord.model.PlanOrd;
import com.furelise.planord.model.PlanOrdRepository;

@Service
public class PickupWayService {

	@Autowired
	PickupWayRepository dao;
	@Autowired
	PlanOrdRepository planOrdDao;

	public String addPickupWay(PickupWay req) {
		String result = "";
		if(!dao.existsByWayName(req.getWayName().trim())) {
			PickupWay pickupWay = new PickupWay(req.getWayName().trim());
			dao.save(pickupWay);
			result = "新增成功";
		} else {
			result = "收取方式已存在";
		}
		return result;
	}

	public String updatePickupWay(PickupWay req) {
		String result = "";
		String oldName = dao.findById(req.getWayID()).get().getWayName().trim();
		if(oldName.equals(req.getWayName()) || !dao.existsByWayName(req.getWayName().trim())) {
			PickupWay pickupWay = new PickupWay(req.getWayID(), req.getWayName().trim());
			dao.save(pickupWay);
			result = "更新成功";
		} else {
			result = "收取方式已存在";
		}
		return result;
	}

	public String deletePickupWay(Integer wayID) {
		String result = "";
		List<PlanOrd> list = planOrdDao.findByWayID(wayID);
		if (list.isEmpty()) {
			dao.deleteById(wayID);
			result = "deleted successfully";
		} else
			result = wayID + " is in use!";
		return result;
	}
//	public void deletePickupWay(Integer wayID) {
//		dao.deleteById(wayID);
//	}

	public List<PickupWay> getAllPickupWay() {
		return dao.findAll();
	}

	public PickupWay getPickupWayById(Integer wayID) {
		return dao.findById(wayID).orElse(null);
	}

}
