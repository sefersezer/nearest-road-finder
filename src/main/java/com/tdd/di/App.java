package com.tdd.di;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ao.etl.ExcelFileReader;
import com.ao.etl.FileReader;
import com.ao.etl.FileRow;
import com.tdd.di.core.Segment;
import com.tdd.di.core.Town;
import com.tdd.di.util.HibernateUtil;
public class App 
{
	
    public static void main( String[] args ) throws IOException
    {
    	Logger logger = Logger.getLogger(App.class);
    	PropertyConfigurator.configure("log4j.properties");
    	ApplicationContext context = new ClassPathXmlApplicationContext("Spring.xml");
    	Town town =(Town) context.getBean("Town");
    	
    	NearestFinder nf = new NearestFinderImpl();
    	
    	List<Object[]> towns = nf.getAllTowns();
    	String townName,cityName;

    	for(int i =0;i<towns.size();i++){
    		townName=towns.get(i)[0].toString();
    		cityName= towns.get(i)[1].toString();
        	town.setName(townName);
    		town.setCity(cityName);
        	logger.info("Starting process for "+ townName+", "+ cityName);
        	
        	nf.findNearest(town, nf.getAllSegments(town));
        	
        	logger.info("End process for      " + townName+", "+cityName);
    	}
    }

	/*
	@SuppressWarnings("unchecked")
	private static List<Segment> getAllSegments(String town,String city) {
		Session session= HibernateUtil.getSessionFactory().openSession();
		try {
			List<Segment> returnList = new ArrayList<Segment>();
			session.beginTransaction();
			Criteria cr = session.createCriteria(Segment.class);
			cr.add(Restrictions.eq("town", town));
			cr.add(Restrictions.eq("city", city));
			cr.addOrder(Order.asc("uavtStreetCode"));
			returnList.addAll(cr.list());
			session.getTransaction().commit();
			session.close();
			return returnList;
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			if(session.getTransaction().isActive())
			{
				session.getTransaction().rollback();
			}
			session.close();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static List<Object[]> getAllTowns(){
		Session session= HibernateUtil.getSessionFactory().openSession();
		try {
			List<Object[]> returnList = new ArrayList<Object[]>();
			session.beginTransaction();
			Criteria cr = session.createCriteria(Segment.class)
					.setProjection(Projections.projectionList()
							.add(Projections.groupProperty("town"))
							.add(Projections.groupProperty("city")));
//			cr.add(Restrictions.not(Restrictions.in("town", new String[]{"Yıldırım","Osmangazi"})));//yıldırım ve osmangazi
			cr.add(Restrictions.in("town", new String[]{"Kadıköy"}));//yıldırım ve osmangazi
			
			cr.addOrder(Order.desc("town"));
			returnList.addAll(cr.list());
			session.getTransaction().commit();
			session.close();
			return returnList;
		} catch (Exception e) {
			if(session.getTransaction().isActive())
			{
				session.getTransaction().rollback();
			}
			session.close();
		}
		return null;
	}

	public static void main( String[] args ) throws IOException
    {
    	Logger logger = Logger.getLogger(App.class);
    	PropertyConfigurator.configure("log4j.properties");
    	ApplicationContext context = new ClassPathXmlApplicationContext("Spring.xml");
    	Town town =(Town) context.getBean("Town");
    	List<Object[]> towns = getAllTowns();
    	String townName,cityName;
    	for(int i =0;i<towns.size();i++){
    		townName=towns.get(i)[0].toString();
    		cityName= towns.get(i)[1].toString();
        	town.setName(townName);
    		town.setCity(cityName);
        	logger.info("Starting process for "+ townName+", "+ cityName);
        	town.createIndex(getAllSegments(townName,cityName));
        	town.findRoadsNearests();
        	logger.info("End processfor       " + townName+", "+cityName);
    	}
    }
	*/
//	private static void createCsvWithVBScript() {
//		try {
//			Collection<String[]> excelRowList = new ArrayList<String[]>();
//			List<Object[]> towns = getAllTowns();
//			for(int i=0;i<towns.size();i++){
//				Runtime.getRuntime().exec(String.format("wscript a.vbs %s %s",
//						"D:/maven-projects/DependencyInjection/data/"+towns.get(i)[0]+towns.get(i)[1]+".xlsx",
//						"D:\\maven-projects\\DependencyInjection\\data\\"+towns.get(i)[0]+towns.get(i)[1]+".csv"));
//    	   
//			}
//		} catch (IOException e) {
//			System.out.println(e);
//			System.exit(0);
//		}
//	}
//	private static void readAllXlsxFiles() {
//		Collection<String[]> excelRowList = new ArrayList<String[]>();
//		List<Object[]> towns = getAllTowns();
//		for(int i=0;i<towns.size();i++){
//			FileReader fr = new ExcelFileReader(String.format("data/%s%s.xlsx",towns.get(i)[0],towns.get(i)[1]));
//			System.out.println("town: "+ towns.get(i)[0]);
//			for(FileRow row = fr.next();row!=null;fr.next()){
//				excelRowList.add(new String[]{
//						row.get(0),
//						row.get(1),
//						row.get(2),
//						row.get(3),
//						row.get(4),
//						row.get(5),
//						row.get(6)});
//			}
//			fr.close();
//			System.out.println("town: "+ towns.get(i)[0]);
//			if(i==9)break;
//		}
//		System.out.println("Tüm satırların sayısı: "+ excelRowList.size());
//	}
}
