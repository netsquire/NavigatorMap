package up.server;

import java.util.Iterator;
import up.shared.MoveTrack;
import up.shared.PointBean;

public class MockTrack extends MoveTrack{

	String trackName;
	Iterator<PointBean> it;

	public MockTrack(String tname, MoveTrack mt) {
		super();
		setTrackName(tname);
		setStatus("work");			
		setPath(mt.getPath());
		it = super.getPath().iterator();
		}
	
	public PointBean nextPoint(){
		return (PointBean) getIt().next();
	}
	
	public String getTrackName() {
		return trackName;
	}

	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	public Iterator<PointBean> getIt() {
		return it;
	}

}
