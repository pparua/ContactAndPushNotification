package com.codehack.contact.vo;

import java.util.List;

public class GridDataVo {
private String LocationId;
private String BackImage;
private List<GridValVo> gridVals;
public String getLocationId() {
	return LocationId;
}
public void setLocationId(String locationId) {
	LocationId = locationId;
}
public String getBackImage() {
	return BackImage;
}
public void setBackImage(String backImage) {
	BackImage = backImage;
}
public List<GridValVo> getGridVals() {
	return gridVals;
}
public void setGridVals(List<GridValVo> gridVals) {
	this.gridVals = gridVals;
}

}
