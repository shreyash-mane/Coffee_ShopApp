package uk.ac.hw.group20.order.dto;

public class ServerInfoDto {
	String lblServer1;
	String lblServer2;
	String lblServer3;
	String lblServer4;
	
	public ServerInfoDto() {
		
	}
	
	public ServerInfoDto(String lblServer1, String lblServer2, String lblServer3, String lblServer4) {
		this.lblServer1 = lblServer1;
		this.lblServer2 = lblServer2;
		this.lblServer3 = lblServer3;
		this.lblServer4 = lblServer4;
	}
	
	public String getLblServer1() {
		return lblServer1;
	}
	public void setLblServer1(String lblServer1) {
		this.lblServer1 = lblServer1;
	}
	public String getLblServer2() {
		return lblServer2;
	}
	public void setLblServer2(String lblServer2) {
		this.lblServer2 = lblServer2;
	}
	public String getLblServer3() {
		return lblServer3;
	}
	public void setLblServer3(String lblServer3) {
		this.lblServer3 = lblServer3;
	}
	public String getLblServer4() {
		return lblServer4;
	}
	public void setLblServer4(String lblServer4) {
		this.lblServer4 = lblServer4;
	}

	@Override
	public String toString() {
		return "ServerDto [lblServer1=" + lblServer1 + ", lblServer2=" + lblServer2 + ", lblServer3=" + lblServer3
				+ ", lblServer4=" + lblServer4 + "]";
	}
	
}
