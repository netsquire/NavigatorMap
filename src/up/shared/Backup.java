package up.shared;

import com.google.gwt.maps.client.geom.LatLng;

public class Backup {
	
	
	
	
	/*
	Nmap3.dbService.getDevices(new AsyncCallback<List<Device>>() {			
		@Override
		public void onSuccess(List<Device> result) {				
			for(Device devName: result){
				ids.add(new Vehicle(devName.getName()));
				}	
			}			
		@Override
		public void onFailure(Throwable caught) {}
		});		
	*/
	
	/*
	System.out.println(" Polyline clicked: (" 
			+ cl.getLatitude() 
			+ " : " 
			+ cl.getLongitude() 
			+ ")");
	System.out.println(" Nearest point: (" 
			+ npb.getLatitude() 
			+ " : " 
			+ npb.getLongitude() 
			+ ")");
	System.out.println(" Distance: " + cl.distanceFrom(nll));
	*/
	/*
	public static boolean isStopGeoEdit() {
		return stopGeoEdit;
	}
	public static void setStopGeoEdit(boolean stopGeoEdit) {
		Nmap3.stopGeoEdit = stopGeoEdit;
	}
	*/

	/*
	dbService.initMockTrack(mtName, dev, new AsyncCallback<TrackInfo>() {
		@Override
		public void onSuccess(TrackInfo result) {
			TrackInfo ti = result;
			System.out.println(" Amount: " + ti.getSize());
			pathLen = ti.getSize();
			todo = new Timer() {
				@Override
				public void run() {
					if (--pathLen <= 0) todo.cancel();
					dbService.nextPoint(mtName, new AsyncCallback<PointBean>() {

								@Override
								public void onSuccess(PointBean pb) {
									LatLng np = LatLng.newInstance(pb.getLatitude(), pb.getLongitude());
									System.out.println(" New index: " + ind);
									System.out.println(" New point: "
											+ np.getLatitude() + " - "
											+ np.getLongitude());
									mtViews.get(did).getPline().insertVertex(++ind, np);
								}
								@Override
								public void onFailure(Throwable caught) {
								}
							});
					System.out.println(" --- ");
				}
			};
			//todo.scheduleRepeating(1000);
			}
		@Override
		public void onFailure(Throwable caught) {
			System.out.println("Happens strange: " + caught);
			}
	});	
	*/
	
	
	//LatLng[] ll = {LatLng.newInstance(55.739456, 37.6483424)};
	//mtViews.get(did).setPline(new Polyline(ll, "red") );
	//map.addOverlay(mtViews.get(did).getPline() );
	/*
	for(Device dev: devices.values()){
		System.out.println(" - dev: " + did 
							 + " ( " 
							 + "pline size: " + mtViews.get(did).getPline().getLength() 
							 + " - " 
							 + "path size: " + mtViews.get(did).getMoveTrack().getPath().size() 
							 + " )");
		}			
	*/
	
    /*
     * Select a contact. The selectionModel will select based on the ID because
     * we used a keyProvider.
     */
    /*
    Contact sarah = ids.get(3);
    selectionModel.setSelected(sarah, true);

    // Modify the name of the contact.
    sarah.name = "Sara";
    */
    /*
     * Redraw the CellList. Sarah/Sara will still be selected because we
     * identify her by ID. If we did not use a keyProvider, Sara would not be
     * selected.
     */
    //cellList.redraw();

    // Add the widgets to the root panel.
    //RootPanel.get().add(cellList);
	
	/*
	Marker m = new Marker(np);
	map.addOverlay(m);
	*/
	/*
	if (mtViews.get(did).getPline() == null){				
			LatLng[] zero = {np, np};
			mtViews.get(did).setPline(new Polyline(zero, "red"));
			mtViews.get(did).setLastIndex( 1 );
			map.addOverlay( mtViews.get(did).getPline() );
			}
	*/

	// =================
	/*
	public static void main(String[] args){
		MockTrack m = launchTrack("test timer");
		System.out.println(" start: " + m.look());
		
		try {
		for(int j: Arrays.asList( 1,2,3,4,5,6,7 )){	
			Thread.sleep(2000);
			System.out.println(" work(" + j + ") : " + m.look());
			}
		} catch (InterruptedException e) {
			System.out.println(" InterruptedException: " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println(" work: " + m.look());
		
	}
	*/
	// =================
	
	
	/*
	GWT.log("   ...working visualTrack() for device: " + deviceId);
	GWT.log(" num of points to visualize: " + moveTracks.get(deviceId).getNumPoints());
	
	for (LatLng point: moveTracks.get(deviceId).getLaLoPath())
		GWT.log("visualizing <" + deviceId + "> = " + point.getLatitude() + " - " + point.getLongitude());
	
	LatLng[] llPath = moveTracks.get(deviceId).getLaLoPath();
	if (llPath != null && llPath.length != 0)
		for (LatLng pp : moveTracks.get(deviceId).getLaLoPath()) {
			map.addOverlay(new Marker(pp));
			}
	*/
	
	//PolylineImpl.impl.construct(moveTracks.get(deviceId).getJSpath(), colorIter.next());
	//PolylineOptions po = PolylineOptions.newInstance(true, true);
	
	/*
	Polyline kLine = new Polyline(				
			mt.getLatLongKalmanPath(), 
			//colorIter.next(),
			"#FF0000",				
			3,
			1,
			PolylineOptions.newInstance(true, true)
			);	
	//pline.insertVertex(index, latlng);
	kLine.setVisible(true);
    map.addOverlay(kLine);
			
	Polyline pline = new Polyline(				
			mt.getLaLoPath(), 
			//colorIter.next(),
			"#0000FF",
			5,
			1,
			PolylineOptions.newInstance(true, true)
			);	
	//pline.insertVertex(index, latlng);
	pline.setVisible(true);
    map.addOverlay(pline);
	*/
    
	/*
	System.out.println("Starting kalmanize: " + path.size() + " points.");
	double dt = 50d;
	double processNoiseStdev = 3;
	double measurementNoiseStdev = 5000;
	KalmanFilter KF;
	double kLat = -1000, kLong = -1000;
	KF = KalmanFilter.buildKF(0, 0, dt, pow(processNoiseStdev, 2) / 2,
			pow(measurementNoiseStdev, 2));
	KF.setX(new Matrix(new double[][] { { startLat }, { 0 },
			{ startLng }, { 0 } }));

	
	for (PointBean pb : path) {
		while (pointLimit > 0) {
		
			KF.predict();
			KF.correct(new Matrix(new double[][] { 
									{ pb.getLatitude() }, 
									{ pb.getLongitude() } 
									}));
			kLat  = (double) KF.getX().get(0, 0);
			kLong = (double) KF.getX().get(1, 0);
			System.out.println(" FROM: (" 
				+ pb.getLatitude() + " : " + pb.getLongitude() + ")"
				+ ") kalmanized TO: ("										
				+  kLat + " : " +  kLong + ")");
			kalmanPath.add(
					new PointBean(pb.getDeviceId(), pb.getHwClock(),
								  //pb.getLatitude(), pb.getLongitude(), 
								  kLat, kLong, 
								  pb.getAltitude(), pb.getAngle(), 
								  pb.getSpeed(),    pb.getSatellites()));					
			}
		}
	*/
	
	/*
	List<LatLng> animPath = moveTrack.get(deviceId).getPath();
	int i = 0;
	for (final LatLng point : animPath) {
		//System.out.println("Trace: " + point.getLatitude() + " - " + point.getLongitude());
		Timer t = new Timer() {
			public void run() {
				MarkerOptions mo = MarkerOptions.newInstance();
				// mo.setAutoPan(true);
				mo.setTitle("Наш паровоз");
				final Marker m = new Marker(point, mo);
				Maps.logWrite(point.getLatitude() + " - "
						+ point.getLongitude());
				map.addOverlay(m);
				Timer r = new Timer() {
					@Override
					public void run() {
						map.removeOverlay(m);
					}
				};
				r.schedule(50);
			}
		};
		t.schedule(++i * 50);
		
	}
*/
	
	
	//String sql = "select get_today_points(" + did + ", 30) from dual";
	String sql2 = "SELECT DEVICE_ID, HW_CLOCK, LATITUDE, LONGITUDE, ALTITUDE, ANGLE, SPEED, SATELLITES"
				+ " FROM ( select * from tracks"
                //+ " WHERE HW_CLOCK > trunc(sysdate, 'HH')"
                + " order by HW_CLOCK desc)"
                //+ " WHERE DEVICE_ID = " + did
                + " AND rownum < (30)";
	
	
	/*
	System.out.println("LaLo Path of (" 
			+ deviceId 
			+ "): " 
			+ " lat: "  + pp.getLatitude()
			+ " long: " + pp.getLongitude());
		
	*/
	
	
	// ---
    // final LinkedList<Marker> tmpPath = new LinkedList<Marker>();

    /*
    LatLng[] __path = {
    		LatLng.newInstance(55.67, 36.34),
    		LatLng.newInstance(55.66, 36.34),
    		LatLng.newInstance(55.65, 36.34),
    		LatLng.newInstance(55.64, 36.34),
    		LatLng.newInstance(55.63, 36.34),
    		LatLng.newInstance(55.63, 36.33),
    		LatLng.newInstance(55.63, 36.35),
    		LatLng.newInstance(55.63, 36.36),
    		LatLng.newInstance(55.67, 36.34),
    		LatLng.newInstance(55.66, 36.34),
    		LatLng.newInstance(55.65, 36.34),
    		LatLng.newInstance(55.64, 36.34),
    		LatLng.newInstance(55.63, 36.34),
    		LatLng.newInstance(55.63, 36.33),
    		LatLng.newInstance(55.63, 36.35),
    		LatLng.newInstance(55.63, 36.36)
    		};
    */
    // ---
	
	//System.out.println(" Size of path: " + tmpPath.size());
	//System.out.println(" Draggable object: " + map.getDragObject());
	
	/*
	for (final Marker m : tmpPath) {
		Timer t = new Timer() {
			public void run() {
				map.removeOverlay(m);
				tmpPath.remove(m);
			}
		};
		t.schedule(++i * 500 + 30);
	}
	*/
	/*
	map.addMapClickHandler(new MapClickHandler() {			
		@Override
		public void onClick(MapClickEvent event) {
			LatLng coord = event.getLatLng();
			System.out.println(" Point of click: " + coord.getLatitude() + " x " + coord.getLongitude());
		}
	});
*/	
    
    
	
}
