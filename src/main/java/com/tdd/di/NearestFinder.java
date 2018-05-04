package com.tdd.di;

import java.util.List;
import com.tdd.di.core.Segment;
import com.tdd.di.core.Town;

public interface NearestFinder {
	
	/**
	 * Veritabanından tüm townları city ile birlikte
	 * distinct olarak getir.
	 * 
	 * object[0] = town
	 * object[1] = city
	 * @return
	 */
	public List<Object[]> getAllTowns();
	
	/**
	 * Index oluşturmak için
	 * Veritabanından belirtilen town, belirtilen city için
	 * tüm segment kayıtlarını getir.
	 * @param town
	 * @param city
	 */
	public List<Segment> getAllSegments(Town town);
	
	/*
	 * belirtilen town için nearestleri bul
	 */
	public void findNearest(Town town,List<Segment> segmentList);
}
