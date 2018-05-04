package com.tdd.di.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.paritus.reversegeo.GeoRecord;
public class Road {
	private String id;
	private String name;
	private List<GeoRecord> nearestGeoRecords= new ArrayList<GeoRecord>();

	public void setId(String id) {
		this.id=id;
	}
	public String getId() {
		return id;
	}
	public void setName(String name) {
		this.name=name;	
	}
	
	public void addNearests(List<GeoRecord> geoRecordList) {
		nearestGeoRecords.addAll(geoRecordList);
	}
	
	public String getName() {
		return name;
	}
		
	public void normalizeSingleRoadNearest() {
		sortNearestGeoRecords();
		String tempId="a";
		List<GeoRecord> optimizedNearestGeoRecords= new ArrayList<GeoRecord>();
		for(int i=0;i<nearestGeoRecords.size();i++){
			if(nearestGeoRecords.get(i).getId().equals(id)){
				continue;
			}
			
			if(!tempId.equals(nearestGeoRecords.get(i).getId())){
				tempId = nearestGeoRecords.get(i).getId();
				optimizedNearestGeoRecords.add(nearestGeoRecords.get(i));
			}
		}
		nearestGeoRecords=new ArrayList<GeoRecord>();
		nearestGeoRecords.addAll(optimizedNearestGeoRecords);
	}
		
	public List<GeoRecord> getNearestGeoRecords() {
		return nearestGeoRecords;
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
	
	private void sortNearestGeoRecords() {
		Collections.sort(nearestGeoRecords,
				GeoRecordComparator.ascending(GeoRecordComparator.getComparator(GeoRecordComparator.ID_SORT,GeoRecordComparator.DISTANCE_SORT)));
	}
	
	

}
