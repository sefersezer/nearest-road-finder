package com.tdd.di;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.tdd.di.core.Road;
import com.tdd.di.core.Segment;
import com.tdd.di.core.Town;
import com.tdd.di.util.HibernateUtil;

public class NearestFinderImpl implements NearestFinder {

	Logger logger = Logger.getLogger(NearestFinderImpl.class);
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllTowns() {
		Session session= HibernateUtil.getSessionFactory().openSession();
		try {
			List<Object[]> returnList = new ArrayList<Object[]>();
			session.beginTransaction();
			Criteria cr = session.createCriteria(Segment.class)
					.setProjection(Projections.projectionList()
							.add(Projections.groupProperty("town"))
							.add(Projections.groupProperty("city")));
			cr.add(Restrictions.not(Restrictions.in("town", new String[]{"Yıldırım","Osmangazi"})));//yıldırım ve osmangazi
//			cr.add(Restrictions.in("town", new String[]{"Kadıköy"}));//yıldırım ve osmangazi
			
			cr.addOrder(Order.asc("town"));
			returnList.addAll(cr.list());
			session.getTransaction().commit();
			session.close();
			return returnList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			if(session.getTransaction().isActive())
			{
				session.getTransaction().rollback();
				session.close();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Segment> getAllSegments(Town town) {
		Session session= HibernateUtil.getSessionFactory().openSession();
		try {
			List<Segment> returnList = new ArrayList<Segment>();
			session.beginTransaction();
			Criteria cr = session.createCriteria(Segment.class);
			cr.add(Restrictions.eq("town", town.getName()));
			cr.add(Restrictions.eq("city", town.getCity()));
			cr.addOrder(Order.asc("uavtStreetCode"));
			returnList.addAll(cr.list());
			session.getTransaction().commit();
			session.close();
			return returnList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			if(session.getTransaction().isActive())
			{
				session.getTransaction().rollback();
			}
			session.close();
		}
		return null;
	}

	public void findNearest(Town town,List<Segment> segmentList) {
		try {
			town.createIndex(segmentList);
			List<Road> roads=town.findRoadsNearests();
			town.normalizeAllNearestandCommit(roads);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
    	
	}
	
}
