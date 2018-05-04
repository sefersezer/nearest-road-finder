package com.tdd.di.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.ao.etl.ExcelFileWriter;
import com.ao.etl.FileWriter;
import com.paritus.reversegeo.GeoRecord;
import com.paritus.reversegeo.SpatialIndex;

public class Town {
	private static List<Segment> allSegments= new ArrayList<Segment>();
	SpatialIndex index;
	private static String name,city;
	
	private static Collection<GeoRecord> toGeoRecords(Segment s) {
		Collection<GeoRecord> tempGeoRecords = new ArrayList<GeoRecord>();
		tempGeoRecords.add(new GeoRecord(s.getUavtStreetCode(), s.getName(),toWKT(s.getCenterLongitude(),s.getCenterLatitute())));
		tempGeoRecords.add(new GeoRecord(s.getUavtStreetCode(), s.getName(),toWKT(s.getStartLongitude(),s.getStartLatitute())));
		tempGeoRecords.add(new GeoRecord(s.getUavtStreetCode(), s.getName(),toWKT(s.getEndLongitude(),s.getEndLatitute())));
		return tempGeoRecords;
	}

	private static String toWKT(String centerLongitude, String centerLatitute) {
		return String.format("POINT (%s %s)", centerLongitude,centerLatitute);
	}

	public void createIndex(List<Segment> segmentList) throws IOException {
		allSegments=segmentList;// new oluştur. ikinci satırda fill et
		Collection<GeoRecord> geoRecordList = new ArrayList<GeoRecord>();
		for(int i=0;i<allSegments.size();i++){
			geoRecordList.addAll(toGeoRecords(allSegments.get(i)));
		}
		index= new SpatialIndex("data/particularIndex");
		index.createIndex(geoRecordList.iterator());
	}
	
	public List<GeoRecord> searchNearestGeoRecords(String lon,String lat) throws IOException {
		return index.searchNearest(Double.parseDouble(lon), Double.parseDouble(lat) , 0.3);
	}

	public List<Road> findRoadsNearests() throws IOException {
		List<Road> roads= new ArrayList<Road>();
		
		for(int i=0;i< allSegments.size();i++){
			Road road = new Road();
			road.setId(allSegments.get(i).getUavtStreetCode());
			road.setName(allSegments.get(i).getName());
			road.addNearests(searchNearestGeoRecords(allSegments.get(i).getStartLongitude(),allSegments.get(i).getStartLatitute()));
			road.addNearests(searchNearestGeoRecords(allSegments.get(i).getCenterLongitude(),allSegments.get(i).getCenterLatitute()));
			road.addNearests(searchNearestGeoRecords(allSegments.get(i).getEndLongitude(),allSegments.get(i).getEndLatitute()));
			
			int pos;
			if((pos=containRoad(roads,road))==-1){
				roads.add(road);
			}
			else{
				roads.get(pos).addNearests(road.getNearestGeoRecords());
			}
		}
		return roads;
	}

	/**
	 * Normalize All Roads, All nearest streets
	 * and
	 * commit to file
	 * @param roads
	 */
	public void normalizeAllNearestandCommit(List<Road> roads) {
		FileWriter fw = new ExcelFileWriter("data/"+name+city+".xlsx");
		for(int i=0;i<roads.size();i++){
			 roads.get(i).normalizeSingleRoadNearest();
			 
			 Object[] row = new Object[7];
			 for(int j=0;j<roads.get(i).getNearestGeoRecords().size();j++){
				row[0] = roads.get(i).getId();
				row[1] = roads.get(i).getName();
				row[2] = roads.get(i).getNearestGeoRecords().get(j).getId();
				row[3] = roads.get(i).getNearestGeoRecords().get(j).getName();
				row[4] = roads.get(i).getNearestGeoRecords().get(j).getDistance();
				row[5] = name;
				row[6] = city;
				fw.writeLine(row);
			 }
		}
		fw.close();
	}

	private int containRoad(List<Road> roadList, Road road) {
		for(int i=0;i<roadList.size();i++){
			if(roadList.get(i).getId().equals(road.getId())){
				return i;
			}
		}
		
		return -1;
	}

	
	public void setName(String string) {
		name=string;
	}

	public void setCity(String string) {
		city=string;
	}

	public String getName() {
		return name;
	}

	public String getCity() {
		return city;
	}

}
