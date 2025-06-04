package model;

public class DataBaseModel {

	String name, oldestTab, oldestTabTime, latestTab, latestTabTime;
	int tablesCount;

	public DataBaseModel() {
	}

	public DataBaseModel(String name, String oldestTab, String oldestTabTime, String latestTab, String latestTabTime,
			int tablesCount) {
		this.name = name;
		this.oldestTab = oldestTab;
		this.oldestTabTime = oldestTabTime;
		this.latestTab = latestTab;
		this.latestTabTime = latestTabTime;
		this.tablesCount = tablesCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOldestTab() {
		return oldestTab;
	}

	public void setOldestTab(String oldestTab) {
		this.oldestTab = oldestTab;
	}

	public String getOldestTabTime() {
		return oldestTabTime;
	}

	public void setOldestTabTime(String oldestTabTime) {
		this.oldestTabTime = oldestTabTime;
	}

	public String getLatestTab() {
		return latestTab;
	}

	public void setLatestTab(String latestTab) {
		this.latestTab = latestTab;
	}

	public String getLatestTabTime() {
		return latestTabTime;
	}

	public void setLatestTabTime(String latestTabTime) {
		this.latestTabTime = latestTabTime;
	}

	public int getTablesCount() {
		return tablesCount;
	}

	public void setTablesCount(int tablesCount) {
		this.tablesCount = tablesCount;
	}

}
