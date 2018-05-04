package com.tdd.di.DependencyInjection;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import com.paritus.reversegeo.GeoRecord;
import com.paritus.reversegeo.SpatialIndex;
import com.tdd.di.NearestFinder;
import com.tdd.di.NearestFinderImpl;
import com.tdd.di.core.Road;
import com.tdd.di.core.Segment;
import com.tdd.di.core.Town;

public class AppTest
{
	/**
	 *  testler için veritabanındaki verilerden
	 *  sadece kadıköy ilçesi
	 *  import edildi.
	 *  ...
	 *  unit test 'e dönüşmesini ben de isterdim.
	 * @return
	 */
	private List<Segment> createMockSegment(){
		Segment s1 = new Segment();
		s1.setUavtStreetCode("196325");
		s1.setName("Şebnem Sokak");
		s1.setStartLongitude("40.96541");
		s1.setStartLatitute("29.10184");
		s1.setCenterLongitude("40.96518");
		s1.setCenterLatitute("29.10235");
		s1.setEndLongitude("40.96496");
		s1.setEndLatitute("29.10286");
		s1.setTown("Kadıköy");
		s1.setCity("İstanbul");
		
		Segment s2 = new Segment();
		s2.setUavtStreetCode("196325");
		s2.setName("Şebnem Sokak");
		s2.setStartLongitude("40.96464");
		s2.setStartLatitute("29.10370");
		s2.setCenterLongitude("40.96469");
		s2.setCenterLatitute("29.10375");
		s2.setEndLongitude("40.96474");
		s2.setEndLatitute("29.10380");
		s2.setTown("Kadıköy");
		s2.setCity("İstanbul");
		
		Segment s3 = new Segment();
		s3.setUavtStreetCode("196325");
		s3.setName("Şebnem Sokak");
		s3.setStartLongitude("40.96463");
		s3.setStartLatitute("29.10360");
		s3.setCenterLongitude("40.96463");
		s3.setCenterLatitute("29.10365");
		s3.setEndLongitude("40.96464");
		s3.setEndLatitute("29.10370");
		s3.setTown("Kadıköy");
		s3.setCity("İstanbul");
		List<Segment> returnList = new  ArrayList<Segment>();
		returnList.add(s1);
		returnList.add(s2);
		returnList.add(s3);
		return returnList;
	}
	
	private Town createTown(String name,String city){
		Town town = new Town();
		town.setCity(city);
		town.setName(name);
		return town;
	}
	
	private static String toWKT(String centerLongitude, String centerLatitute) {
		return String.format("POINT (%s %s)", centerLongitude,centerLatitute);
	}
       
		
    @Test
	public void testtoGeoRecords() throws Exception {
    	//test center coordinates
    	List<Segment> segmentList =createMockSegment();
		List<GeoRecord> tempGeoRecords = new ArrayList<GeoRecord>();
		tempGeoRecords.add(new GeoRecord(segmentList.get(0).getUavtStreetCode(), segmentList.get(0).getName(),toWKT(segmentList.get(0).getCenterLongitude(),segmentList.get(0).getCenterLatitute())));
		tempGeoRecords.add(new GeoRecord(segmentList.get(1).getUavtStreetCode(), segmentList.get(1).getName(),toWKT(segmentList.get(1).getCenterLongitude(),segmentList.get(1).getCenterLatitute())));
		tempGeoRecords.add(new GeoRecord(segmentList.get(2).getUavtStreetCode(), segmentList.get(2).getName(),toWKT(segmentList.get(2).getCenterLongitude(),segmentList.get(2).getCenterLatitute())));
		assertTrue(tempGeoRecords.get(0).getWkt().equals("POINT (40.96518 29.10235)"));
		assertTrue(tempGeoRecords.get(1).getWkt().equals("POINT (40.96469 29.10375)"));
		assertTrue(tempGeoRecords.get(2).getWkt().equals("POINT (40.96463 29.10365)"));
	}
    
    @Test
	public void testfindRoadsNearests() throws Exception {
    	/*
    	 * kadıköyün tüm segmentlerini getir.
    	 * segmentlerden index oluştur.
    	 * 
    	 * */
    	NearestFinder nf = new NearestFinderImpl();
    	Town town = createTown("Kadıköy", "İstanbul");
    	List<Segment> segmentList= nf.getAllSegments(town);
    	town.createIndex(segmentList);
    	
    	List<Road> roads= new ArrayList<Road>();
    	roads=town.findRoadsNearests();
		assertTrue(segmentList.size()==6061);
		
    	
	}
    
        
    @Test
	public void containRoad() throws Exception {
		List<Road> roadList = new ArrayList<Road>();
		Road r1 = new Road();
		r1.setId("096325");
		Road r2 = new Road();
		r2.setId("192325");
		Road r3 = new Road();
		r3.setId("296325");
		roadList.add(r1);roadList.add(r2);roadList.add(r3);
		
		boolean contain192325 = false;
		for(int i=0;i<roadList.size();i++){
			if(roadList.get(i).getId().equals("192325")){
				contain192325=true;
				break;
			}
		}
		assertTrue(contain192325);
	}
    
    @Test
	public void normalizeSingleRoadNearest() throws Exception {
    	NearestFinder nf = new NearestFinderImpl();
    	Town town = createTown("Kadıköy", "İstanbul");
    	List<Segment> segmentList= nf.getAllSegments(town);
    	town.createIndex(segmentList);
    	
    	List<Road> roads= new ArrayList<Road>();
    	roads=town.findRoadsNearests();
    	for(int i=0;i<roads.size();i++){
    		roads.get(i).normalizeSingleRoadNearest();
    		if(roads.get(i).getName().equals("Şebnem Sokak")){
    			assertTrue(27 == roads.get(i).getNearestGeoRecords().size());
    			break;
    		}
    	}
    	
    }
    
   
    
    @Test
	public void testsortNearestGeoRecords() throws Exception {
		List<Segment> segmentList =createMockSegment();
		List<GeoRecord> tempGeoRecords = new ArrayList<GeoRecord>();
		tempGeoRecords.add(new GeoRecord("1", segmentList.get(0).getName(),toWKT(segmentList.get(0).getCenterLongitude(),segmentList.get(0).getCenterLatitute())));
		tempGeoRecords.add(new GeoRecord("11", segmentList.get(2).getName(),toWKT(segmentList.get(2).getCenterLongitude(),segmentList.get(2).getCenterLatitute())));
		tempGeoRecords.add(new GeoRecord("11", segmentList.get(1).getName(),toWKT(segmentList.get(1).getCenterLongitude(),segmentList.get(1).getCenterLatitute())));
		
		SpatialIndex index = new SpatialIndex("data/particularIndex");
		index.createIndex(tempGeoRecords.iterator());
		List<GeoRecord>  res = index.searchNearest(40.96518, 29.10235, 0.3);
		
		sortNearestGeoRecords(res);
		assertTrue(res.get(0).getDistance()<res.get(1).getDistance() && res.get(1).getDistance()<res.get(2).getDistance());
		
	}
    
    enum GeoRecordComparator implements Comparator<GeoRecord> {
	    DISTANCE_SORT {
	        public int compare(GeoRecord o1, GeoRecord o2) {
	            return Double.compare(o1.getDistance(),o2.getDistance());
	        }},
	    ID_SORT {
	        public int compare(GeoRecord o1, GeoRecord o2) {
	            return o1.getId().compareTo(o2.getId());
	        }};

	    static Comparator<GeoRecord> ascending(final Comparator<GeoRecord> other) {
	        return new Comparator<GeoRecord>() {
	            public int compare(GeoRecord o1, GeoRecord o2) {
	                return other.compare(o1, o2);
	            }
	        };
	    }

	    static Comparator<GeoRecord> getComparator(final GeoRecordComparator... multipleOptions) {
	        return new Comparator<GeoRecord>() {
	            public int compare(GeoRecord o1, GeoRecord o2) {
	                for (GeoRecordComparator option : multipleOptions) {
	                    int result = option.compare(o1, o2);
	                    if (result != 0) {
	                        return result;
	                    }
	                }
	                return 0;
	            }
	        };
	    }
	}
	
	public static void sortNearestGeoRecords(List<GeoRecord> geoRecordList) {
		Collections.sort(geoRecordList,
				GeoRecordComparator.ascending(GeoRecordComparator.getComparator(GeoRecordComparator.ID_SORT,GeoRecordComparator.DISTANCE_SORT)));
	}
}
