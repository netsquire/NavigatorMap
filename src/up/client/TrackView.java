package up.client;

import up.shared.LatLongSerial;
import up.shared.MoveTrack;
import up.shared.PointBean;

import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TrackView {

	private String mtid;
	private Timer timer;
	private int lastIndex = 0;
	private Polyline pline;
	MoveTrack moveTrack;


	public TrackView(final String mtid, final Polyline pline) {
		this.mtid = mtid;
		this.pline = pline;
		this.lastIndex = pline.getVertexCount();
		}

	public TrackView(MoveTrack mt) {
		setMtid(mt.getDev().getDeviceId() + "");
		this.pline = new Polyline(mt.getLaLoPath());
		this.lastIndex = pline.getVertexCount();
		if (mt.isOpen())
			setTimer(timer);
		this.moveTrack = mt;
		}

	public void startAnimate() {
		// TODO 
		//startTimer().scheduleRepeating(500);
		timer = new Timer() {
			public void run() {	
				Nmap.dbService.getLastPoint(mtid, new AsyncCallback<LatLongSerial>() {
					public void onSuccess(LatLongSerial result) {
						System.out.println("Something happens - (" + lastIndex + ") " 
													+ result.getLatitude() + " : " 
													+ result.getLongitude());
						pline.insertVertex(lastIndex, LatLng.newInstance(result.getLatitude(), result.getLongitude()));
						lastIndex++;
						startTimer().schedule(500);	
						}

					public void onFailure(Throwable caught) {
						System.out.println("Something goes wrong! <inside TrackView constructor>");				
						}
					});
				}
			};
		}
	
	public void visualize(MapWidget mwMap) {
		mwMap.addOverlay(pline);
		}
	
	
	public Timer startTimer(){
		timer = new Timer() {
			public void run() {	
				Nmap.dbService.getLastPoint(mtid, cb);
				}
			};
		return timer;
	}
	
	
	AsyncCallback<LatLongSerial> cb = new AsyncCallback<LatLongSerial>() {
		public void onSuccess(LatLongSerial result) {
			System.out.println("Something happens - (" + lastIndex + ") " 
										+ result.getLatitude() + " : " 
										+ result.getLongitude());
			pline.insertVertex(lastIndex, LatLng.newInstance(result.getLatitude(), result.getLongitude()));
			lastIndex++;
			startTimer().schedule(500);	
			}

		public void onFailure(Throwable caught) {
			System.out.println("Something goes wrong! <inside TrackView constructor>");				
			}
		};
	
	
	// Getters & setters
	
	public MoveTrack getMoveTrack() {
		return moveTrack;
	}

	public void setMoveTrack(MoveTrack moveTrack) {
		this.moveTrack = moveTrack;
	}

	public Timer getTimer() {
		return timer;
	}
	
	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public String getMtid() {
		return mtid;
	}

	public void setMtid(String mtid) {
		this.mtid = mtid;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public int setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
		return lastIndex + 1;
	}
	
	public int getIncIndex() {
		setLastIndex ( getLastIndex() + 1 );
		return lastIndex;
	}

	public Polyline getPline() {
		return pline;
	}

	public void setPline(Polyline pline) {
		this.pline = pline;
	}

	

	

}
